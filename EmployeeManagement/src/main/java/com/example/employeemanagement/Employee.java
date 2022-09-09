package com.example.employeemanagement;

import java.io.Serializable;

public class Employee extends Position implements Serializable, Comparable<Employee> {
    public String id;
    public String name;
    public String phone;
    public String email;
    public String dateOfBirth;
    public String gender;

    public Employee() {
    }

    public Employee(String id, String name, String phone, String email, String dateOfBirth, String gender,String position,
                    float salary) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.position = position;
        this.salary = salary;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name=" + name +
                ", phone=" + phone +
                ", email=" + email +
                ", dateOfBirth=" + dateOfBirth +
                ", salary=" + salary +
                ", position=" + position +
                '}';
    }

    @Override
    public int compareTo(Employee o) {
        return 0;
    }
}
