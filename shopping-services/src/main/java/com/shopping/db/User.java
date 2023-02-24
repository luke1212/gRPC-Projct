package com.shopping.db;

// This is the User class which defines the user table structure in the database
public class User {

    // Unique identifier for the user
    private int id;

    // Unique username of the user
    private String username;

    // Name of the user
    private String name;

    // Age of the user
    private int age;

    // Gender of the user
    private String gender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
