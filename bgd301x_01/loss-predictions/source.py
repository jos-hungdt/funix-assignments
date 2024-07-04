from pyspark.sql import SparkSession
from pyspark.ml.feature import StringIndexer
from pyspark.mllib.regression import LabeledPoint
from pyspark.mllib.tree import RandomForest, RandomForestModel
from pyspark.mllib.evaluation import RegressionMetrics

#===============================================================================
# Transform each RDD item to LabeledPoint
#===============================================================================
def row_to_labeled_point(row, feature_columns, labelCol):
  '''
    Convert DataFrame row to LabeledPoint
  '''
  features = [row[col] for col in feature_columns]
  label = row[labelCol]
  return LabeledPoint(label, features)

#===============================================================================
# Transform data RDD to LabeledPoints RDD.
#===============================================================================
def transform_data_to_labeledpoints_rdd(data, labelCol):
  '''
    Transform RDD data input to LabeledPoint RDD.
  '''
  #*****************************************
  print("-> log the schema to verify the csv")
  #*****************************************
  data.printSchema()

  #*****************************************
  print('-> Handle categorical features - convert categorical columns to numerical values')
  #*****************************************
  # List of categorical columns
  categorical_columns = [f"cat{i}" for i in range(1, 117)]

  # Apply StringIndexer to each categorical column
  indexers = [StringIndexer(inputCol=column, outputCol=column + "_index").fit(data) for column in categorical_columns]

  # Transform data
  for indexer in indexers:
    data = indexer.transform(data)

  # Drop original categorical columns
  data = data.drop(*categorical_columns)

  # List of feature columns
  indexed_categorical_columns = [f"cat{i}_index" for i in range(1, 117)]
  continuous_columns = [f"cont{i}" for i in range(1, 15)]
  feature_columns = indexed_categorical_columns + continuous_columns

  return data.rdd.map(lambda x: row_to_labeled_point(x, feature_columns, labelCol))

#===============================================================================
# Train Random Forest Regression model tuner.
#===============================================================================
def rfm_tuner(params):
  '''
    Random Forest Regression model training and evaluation tuner.
  '''
  if params['trainInput'] is None \
    or params['modelPath'] is None \
    or params['numTrees'] is None \
    or params['maxDepth'] is None \
    or params['maxBins'] is None:
    raise ValueError("Missing tuner parameters. Please have a double-check")

  spark = SparkSession \
    .builder \
    .appName("assignment2_AllstateClaimsSeverityRandomForestRegressor") \
    .getOrCreate()

  #****************************
  print('-> BGD301x_o37 - Loading training data from ', params['trainInput'])
  #****************************

  data = spark.read.format('csv') \
    .option('header', 'true') \
    .option('inferschema', 'true') \
    .load(params['trainInput']) \
    .cache() \
    .withColumnRenamed("loss", "label")

  #****************************
  print('-> BGD301x_o38 - Convert data to LabeledPoint RDD')
  #****************************
  rdd = transform_data_to_labeledpoints_rdd(data, 'label')

  #*****************************************
  print("-> Preparing data - Split the data into training and test sets, part 1 - 70% data is for training, remain 30% is testing")
  #*****************************************
  (trainingData, valData) = rdd.randomSplit([0.7, 0.3])
  trainingData.cache()
  valData.cache()

  #*****************************************
  print("-> Train RandomForest regressor")
  #*****************************************
  model = RandomForest.trainRegressor(
      trainingData,
      categoricalFeaturesInfo={},
      numTrees=params['numTrees'],
      featureSubsetStrategy="auto",
      impurity="variance",
      maxDepth=params['maxDepth'],
      maxBins=params['maxBins']
  )

  #*****************************************
  print("-> Complete training Random Forest model, Saving model for the next use")
  #*****************************************
  model.save(spark.sparkContext, params['modelPath'])

  #*****************************************
  print("-> Evaluating model on train and val data and calculating RMSE")
  #*****************************************
  predictions = model.predict(valData.map(lambda x: x.features))

  # Zip predictions with actual values for evaluation
  prediction_and_label = predictions.zip(valData.map(lambda lp: lp.label))

  # Calculate evaluation metrics (example: RMSE)
  metrics = RegressionMetrics(prediction_and_label)
  print("- Root Mean Squared Error (RMSE) = ", metrics.rootMeanSquaredError)
  print("- Validation data MSE = ", metrics.meanSquaredError)
  print("- Validation data RMSE = ", metrics.rootMeanSquaredError)
  print("- Validation data R-squared = ", metrics.r2)
  print("- Validation data MAE = ", metrics.meanAbsoluteError)
  print("- Validation data Explained variance = ", metrics.explainedVariance)

  # Stop the Spark session
  spark.stop()

#===============================================================================
# Run Random Forest Regressor on the test data and export submission file.
#===============================================================================
def process_test_data(params):
  spark = SparkSession \
    .builder \
    .appName("assignment2_AllstateClaimsSeverityRandomForestRegressor") \
    .getOrCreate()

  testInput = spark.read.format('csv') \
    .option('header', 'true') \
    .option('inferschema', 'true') \
    .load(params['testInput']) \
    .cache()

  # Convert DataFrame to RDD[LabeledPoint]
  testRdd = transform_data_to_labeledpoints_rdd(testInput, "id")
  testRdd.cache()

  model = RandomForestModel.load(spark.sparkContext, params['modelPath'])

  #*****************************************
  print("-> Run the model on data test.")
  #*****************************************
  predictions = model.predict(testRdd.map(lambda x: x.features))

  print("-> Zip predictions with actual values for evaluation")
  prediction_and_label = testRdd.map(lambda lp: int(lp.label)).zip(predictions)

  # Convert predictions to DataFrame
  results = prediction_and_label.toDF(["id", "loss"])

  #*****************************************
  print("-> Save the results to a CSV file.")
  #*****************************************

  results.write.csv(params['outputFile'], header=True)

  spark.stop()

#===============================================================================
# Train Random Forest with different param sets to determine the best params 
# configuration.
#
# Tested with the following values:
# - numTrees: 3, 10, 15, 20, 40
# - maxDepth: 4, 5, 7, 9
# - maxBins: 32, 48#
#===============================================================================
params = {
  'trainInput': '/content/drive/MyDrive/ColabDrive/allstates/resources/train.csv',
  'numTrees': 40,
  'maxDepth': 9,
  'maxBins': 32,
  'modelPath': '/content/drive/MyDrive/ColabDrive/models/allstates_rf40_9_32'
}

rfm_tuner(params)

#===============================================================================
# Run best model on the test data and export submission file.
#===============================================================================
params = {
  'testInput': '/content/drive/MyDrive/ColabDrive/allstates/resources/test.csv',
  'outputFile': '/content/drive/MyDrive/ColabDrive/allstates/submission_rf40_9_32',
  'modelPath': '/content/drive/MyDrive/ColabDrive/models/allstates_rf40_9_32'
}

process_test_data(params)