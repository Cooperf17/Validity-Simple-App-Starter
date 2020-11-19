package com.validity.monolithstarter;

public class Record {
    //Fields
    //id,first_name,last_name,company,email,address1,address2,zip,city,state_long,state,phone
    private int id;
    private String firstName;
    private String lastName;
    private String company;
    private String email;
    private String address1;
    private String address2;
    private int zip;
    private String city;
    private String state_long;
    private String state;
    private String phone;

    //constructor
    public Record(int id, String firstName, String lastName, String company, String email, String address1, String address2, int zip, String city, String state_long, String state, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.zip = zip;
        this.city = city;
        this.state_long = state_long;
        this.state = state;
        this.phone = phone;
    }

    //Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState_long() {
        return state_long;
    }

    public void setState_long(String state_long) {
        this.state_long = state_long;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
