package models;

import java.util.Date;

/**
 * Presents an Person object.
 */
public class Person {
    private String id;
    private String name;
    private Date dateOfBirth;
    private String birthPlace;

    public Person(String id, String name, Date dateOfBirth, String birthPlace) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.birthPlace = birthPlace;
    }

    public Person() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }
    
    /**
     * Overriding this to convert this person to String object using for printing or writing
     */
    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", dateOfBirth=" + dateOfBirth + ", birthPlace=" + birthPlace + "]";
    }
}
