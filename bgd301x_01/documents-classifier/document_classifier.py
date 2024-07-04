from pyspark.sql import SparkSession
from collections import Counter
from pyspark.mllib.feature import Word2Vec, Word2VecModel
from pyspark.mllib.linalg import Vectors
from pyspark.mllib.tree import RandomForest, RandomForestModel
from pyspark.mllib.regression import LabeledPoint
from pyspark.mllib.evaluation import MulticlassMetrics

import os
import numpy as np
import re

dataDirPath = '/content/drive/MyDrive/ColabDrive/20news-18828'
word2VecPath = '/content/drive/MyDrive/ColabDrive/models/word2VecModel/word2Vec'

# Replace value of numTrees, maxDepth, maxBins to find the best hyperparameters
params = {
  'numTrees': 80,
  'maxDepth': 10,
  'maxBins': 64,
  'outputPath': '/content/drive/MyDrive/ColabDrive/doc-classifier/submission_final',
  'docClassifierModelPath': '/content/drive/MyDrive/ColabDrive/models/document_classifier_model'
}

# ==============================================================================
def transform_and_split_data(data_dir):
  '''
    Transform and split documents
    return a tuple (<training data files>: array, <validation data files>: array, <testing data files>: array)
  '''
  # extract folder name (label) from directory path
  folder_name = data_dir[0].split('/')[-1]

  # sort the data files by name
  data_dir[2].sort(key=lambda x: int(x))

  # each file should be converted to ("<label>, <file name>", <file path>)
  data_dict = []
  for file_name in data_dir[2]:
    path = data_dir[0] + "/" + file_name
    data_dict.append((folder_name + ", " + file_name, path))

  # calculate and split data files into 3 sets for training, testing, and validation
  total_count = len(data_dir[2])
  training_count = int(total_count * 0.6)
  val_count = int(total_count * 0.2)

  training_arr, val_arr, test_arr = np.split(data_dict, [training_count, training_count + val_count])

  return (training_arr, val_arr, test_arr)

# ==============================================================================
def process_file(path):
  '''
    Read content of a file.
  '''
  with open(path, 'r', encoding="ISO-8859-1") as file:
    content = file.read()
  return content

# ==============================================================================
def remove_special_chars(s):
  '''
    Remove special characters from document
  '''
  return re.sub(r'\W+', ' ', s)

# ==============================================================================
def generate_bigrams(document):
  '''
    Function to generate bigrams from a document.
  '''
  words = document.split()
  bigrams = []
  for i in range(len(words) - 1):
    bigrams.append(words[i] + ' ' + words[i + 1])
  return bigrams

# ==============================================================================
def parse_doc_to_labeledpoint(data):
  label, vec = data
  folder, filename = label.split(',')

  # Ensure the vector is converted to a list with a fixed length
  features = vec.toArray().tolist()

  # Pad or truncate the feature list to a fixed length if necessary
  fixed_length = 400
  features = features[:fixed_length] + [0] * (fixed_length - len(features))
  return LabeledPoint(folder_to_label[folder], features)

# ==============================================================================
def transform_to_vector(document, broadcast_word_vectors):
  '''
    Function transform document to vector.
  '''
  docWords = document.split(" ")
  bigrams = generate_bigrams(document)

  # create the first vector - word vector
  wordVec = np.asarray([1 if word in docWords else 0 for word in topMostAppearanceWords])

  # create the second vector - bigram vector
  bigramVec = np.asarray([1 if bigram in bigrams else 0 for bigram in topBigramMostAppearance])

  # combine 2 vectors
  combined_bi_word_vecs = np.append(wordVec, bigramVec)

  # create word2Vec vector
  most_common_words = Counter(docWords).most_common(100)
  wordsVectors = broadcast_word_vectors.value

  vectors = [wordsVectors[word] for word, _ in most_common_words if word in wordsVectors]
  avg_vector = np.mean(vectors, axis=0)

  combined_word_vec = np.append(combined_bi_word_vecs, np.asarray(avg_vector))
  return Vectors.dense(combined_word_vec)

# ==============================================================================
def process_document(doc_id, doc_content, broadcast_word_vectors):
  '''
    In case of using 200 words and 100 bigrams most appearance,
    use the following code line marked as #1, this will be used by default as assignment requirement.

    The #2 is is alternative method to convert documents to vector,
    this method will use the word2Vec model only for the process.

    Uncomment the #1 code line or the #2 code line depend on which method you want to use.
    NOTE: #1 is much better than #2 after testing.
  '''
  #1
  vector = transform_to_vector(doc_content.lower(), broadcast_word_vectors)

  #2
  # vector = doc_to_word2vec(doc_content.lower(), broadcast_word_vectors)
  return (doc_id, vector)

# ==============================================================================
def doc_to_word2vec(data, broadcast_word_vectors):
  '''
    Transform document to vector 400 length, only use word2vec model.
    - Find 400 most common words in the document
    - Use word2vec model to get the vector of each word and calculate the average vector of the words.
  '''
  doc_words = data.split(" ")
  # create word2Vec vector
  most_common_words = Counter(doc_words).most_common(400)
  word_vectors = broadcast_word_vectors.value

  vectors = [Vectors.dense(word_vectors[word]) for word, _ in most_common_words if word in word_vectors]

  if vectors:
    avg_vector = sum(vectors) / len(vectors)
    return avg_vector
  else:
    return Vectors.dense([0.0] * 400)

# ==============================================================================
# >>> Main program
# ==============================================================================
print(">>> Initialize spark session. Parameters will be using: \n" \
      f"numTrees: {params['numTrees']}\n" \
      f"maxDepth: {params['maxDepth']}\n" \
      f"maxBins: {params['maxBins']}\n" \
      f"output files will be exported at: {params['outputPath']}\n" \
      f"Trained model will be saved to (if available): {params['docClassifierModelPath']}\n" \
      "------------------------------------------------------------------------- \n"
)

spark = SparkSession \
    .builder \
    .appName("document_classifier") \
    .getOrCreate()
sc = spark.sparkContext
dataDirs = [x for x in os.walk(dataDirPath)]
dataDirs.sort(key=lambda x: x[0])

# load all data folders and for each of folder, get all file names and split into 3 arrays
trainingArr = []
valArr = []
testArr = []

#*****************************************
print('ASM-3: read and separate data set;')
#*****************************************

for dataDir in dataDirs:
  if len(dataDir[2]) < 1:
    continue
  trainingDataDirs, valDataDirs, testDataDirs = transform_and_split_data(dataDir)

  trainRDD = sc.parallelize(trainingDataDirs).map(lambda x: (x[0], process_file(x[1])))
  valRDD = sc.parallelize(valDataDirs).map(lambda x: (x[0], process_file(x[1])))
  testRDD = sc.parallelize(testDataDirs).map(lambda x: (x[0], process_file(x[1])))

  trainingArr.append(trainRDD)
  valArr.append(valRDD)
  testArr.append(testRDD)

#*****************************************
print('ASM-3: union sets of RDD and cache to memory. \n')
#*****************************************
trainRDDs = sc.union(trainingArr)
valRDDs = sc.union(valArr)
testRDDs = sc.union(testArr)

trainRDDs.cache()
valRDDs.cache()
testRDDs.cache()

#*****************************************
print('ASM-4: converts all words to lower case and remove special characters;')
#*****************************************
# converts all words to lower case and remove special characters because they are not a word.
lowerCaseRdd = trainRDDs.map(lambda x: remove_special_chars(x[1].lower()))

#*****************************************
print('ASM-4: find top 200 words most appearance;')
#*****************************************
excludedWords = ['in','on','of','out','by','from','to','over','under','the','a','an','when','where','what','who','whom','you','thou','go', \
                 'must','i','me','my','myself','for','and','x','it','are', '0','1','2','3','4','5','6','7','8','9','be','thi','with','this', \
                 'that','or','if','have','t','an','db','but','at','wa','they','will','can','a','b','c','d','e','f','g','h','i','j','k','l', \
                 'm','n', 'o','p','q','r','s','t','u','v','w','x','y','z','one','zero','one','two','three','four','five','six','seven', \
                 'eight','nine','ten','do','did','here','there','all','subject','about','we','other', 'no','re','ha','which','your','so', \
                 'would','some','their','he','any','more','how','only','may','might','also','new','should','up','hi','dear','them','then', \
                 'first','second','third','don','doe', 'were','know','than','less','most','get','year','like','been','use','many', 'few', \
                 'little','just','make','these','those','because','not','into']

# split each document to arrays of words, filter out the excluded words, then create frequency tuple
wordsRdd = lowerCaseRdd.flatMap(lambda x: x.split(' ')) \
  .filter(lambda x: x not in excludedWords and x) \
  .map(lambda x: (x, 1))

frequencyWordRdd = wordsRdd.reduceByKey(lambda a, b: a + b)
topMostAppearanceWords = frequencyWordRdd.top(200, key=lambda x: x[1])
# print("2. Top 200 words most appearance = ", topMostAppearanceWords)
print('ASM-4: complete finding most appearance words;')

#*****************************************
print('ASM-4: find top 100 bigram most appearance;')
#*****************************************
excludedBigrams = ['of the','x x','in the','to the','it i','on the','to be','for the','i a','subject re','and the','if you','don t','that the', \
                   'in article','0 1','1 1','from the','thi i','with the','i not','it ','i the','the same','in a','of a','that i','for a','by the', \
                   'will be','i m','i have','there i','the first','you are','with a','n x','a a','what i','doe not','to a','at the','do not', \
                   'would be','can be','there are','1 0','i am','they are','are not','you can','on a','and i','should be','may be','and a','have a', \
                   'have been','such a','number of','that you','i ve','about the',' want to','that it','which i','the following','x printf','but i', \
                   'i don','can t','x if','file x','to do','to get','you have','one of','and other','a the','doesn t','1 2','i can','that they', \
                   'out of','i to','i that','all the','the other','how to','of thi','into the','be a','to have','c si','i think','are the','to make', \
                   'ha been','isn t','0 0','x char','ha a','must be','mov bh','it wa','have to','in thi','for example','if the',' a','not a','that ', \
                   'x the','of course','at least','a good','you re','write in','not to','part of','i one','2 2','your entry','but the','a few','the u', \
                   'the only','i would','i an','a well','u ','and that','i wa','sort of','lot of','0 2','but it','if i','the most','and at','all of', \
                   'to use','seem to','and it','i know','bl bh','to see','want to']

bigramRdd = lowerCaseRdd.flatMap(lambda d: generateBigrams(d))
filteredBigramRdd = bigramRdd.filter(lambda x: x and x not in excludedBigrams) \
  .map(lambda x: (x, 1))

frequencyBigramRdd = filteredBigramRdd.reduceByKey(lambda a, b: a + b)
topBigramMostAppearance = frequencyBigramRdd.top(100, key=lambda x: x[1])
# print("3. Top 100 bigrams most appearance = ", topBigramMostAppearance)
print('ASM-4: complete finding most appearance bigrams. \n')

#*****************************************
print('ASM-5: train Word2Vec by documents in train RDD;')
#*****************************************
word2vec = Word2Vec()
inp = lowerCaseRdd.map(lambda x: x.split(" "))
inp.cache()
word2VecModel = word2vec.fit(inp)

#*****************************************
print('ASM-5: save model for the next use;')
#*****************************************
word2VecModel.save(sc, word2VecPath)

# Training is expensive! Load the trained model if it's exist by uncomment the code line below.
# word2VecModel = Word2VecModel.load(sc, word2VecPath)

#*****************************************
print('ASM-5: broadcast word vectors for transform document to vector process;')
#*****************************************
wordVectors = word2VecModel.getVectors()
wordVectorsDict = {word: list(vector) for word, vector in wordVectors.items()}
broadcastWordVectors = sc.broadcast(wordVectorsDict)

#*****************************************
print('ASM-5: transforms documents to vectors. \n')
#*****************************************
trainVectorRdd = trainRDDs.map(lambda x: process_document(x[0], x[1], broadcastWordVectors))
trainVectorRdd.cache()

valVectorRdd = valRDDs.map(lambda x: process_document(x[0], x[1], broadcastWordVectors))
valVectorRdd.cache()

testVectorRdd = testRDDs.map(lambda x: process_document(x[0], x[1], broadcastWordVectors))
testVectorRdd.cache()

#*****************************************
print('ASM-6: convert documents to LabeledPoints;')
#*****************************************
trainLabeledPointRdd = trainVectorRdd.map(parse_doc_to_labeledpoint)
valLabeledPointRdd = valVectorRdd.map(parse_doc_to_labeledpoint)
testLabeledPointRdd = testVectorRdd.map(parse_doc_to_labeledpoint)

#*****************************************
print('ASM-6: create a dictionary to map folder names to labels and calculate numClasses for the training;')
#*****************************************
folder_names = [dataDir[0].split('/')[-1] for dataDir in dataDirs]
folder_names.pop(0)
folder_to_label = {name: i for i, name in enumerate(folder_names)}
numClasses = len(folder_names) + 1

#*****************************************
print('ASM-6: training Random Forest classifier model;')
#*****************************************
classifier_model = RandomForest.trainClassifier(trainLabeledPointRdd, \
                                                numClasses=numClasses, \
                                                categoricalFeaturesInfo={}, \
                                                numTrees=params['numTrees'], \
                                                featureSubsetStrategy="auto", \
                                                impurity='gini', \
                                                maxDepth=params['maxDepth'], \
                                                maxBins=params['maxBins'])

#*****************************************
print('ASM-6: complete training Random Forest classifier model, saving for the next use;')
#*****************************************
# training is expensive, save for later!
classifier_model.save(sc, params['docClassifierModelPath'])

# The below code line is for loading the saved model if exist. This will save time for testing.
classifier_model = RandomForestModel.load(sc, params['docClassifierModelPath'])

#*****************************************
print('ASM-6: start evaluating the model on validation data;')
#*****************************************
predictions = classifier_model.predict(valLabeledPointRdd.map(lambda x: x.features))

validLabelRdd = valLabeledPointRdd.map(lambda x: x.label)
valid_prediction_and_labels = validLabelRdd.zip(predictions)
valid_multiclass_metrics = MulticlassMetrics(valid_prediction_and_labels)

print('ASM-6: validation data evaluation results:')
print("-> Accuracy:", valid_multiclass_metrics.accuracy)
print("-> Weighted Precision:", valid_multiclass_metrics.weightedPrecision)
print("-> Weighted Recall:", valid_multiclass_metrics.weightedRecall)
print("-> Weighted F1 Score:", valid_multiclass_metrics.weightedFMeasure())
print("-> Confusion Matrix:\n", valid_multiclass_metrics.confusionMatrix().toArray())

#*****************************************
print('ASM-6: Run model with test data and write to output file.')
#*****************************************
test_predictions = classifier_model.predict(testLabeledPointRdd.map(lambda x: x.features))
test_numeric_labels = testLabeledPointRdd.map(lambda x: int(x.label))
test_prediction_and_numeric_labels = test_numeric_labels.zip(test_predictions)
test_prediction_and_labels = testRDDs.map(lambda x: x[0]).zip(test_predictions.map(lambda x: folder_names[int(x)]))

# Convert to DataFrame with 3 columns Origin folder, document name and the model prediction
results = spark.createDataFrame(test_prediction_and_labels.map(lambda x: (x[0].split(',')[0], x[0].split(',')[1], x[1]))).toDF("origin-folder", "doc-name", "prediction")

# Show DataFrame to output for quick checking.
results.show()

# Save results to CSV file(s).
# Using coalesce(1) help to merge all partitions into a single DataFrame and so, only 1 CSV file will be created with all data. BUT it's expensive to do so.
# For testing, remove the .coalsesce(1) and just left the Spark decided by itself.
results.coalesce(1).write.csv(params['outputPath'], header=True)

#*****************************************
print('ASM-6: Completed writing output file. Check it out.')
#*****************************************
spark.stop()
