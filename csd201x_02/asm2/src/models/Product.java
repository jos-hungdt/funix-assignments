package models;

public class Product {
    private int id;
    private String name;
    private float price;
    private int number;

    public Product() {}

    public Product(int id, String name, float price, int number) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", price=" + price + ", number=" + number + "]";
    }

    public String toCsv() {
        return Integer.toString(id)
            + "," + this.name
            + "," + Float.toString(this.price)
            + "," + Integer.toString(this.number);
    }
}
