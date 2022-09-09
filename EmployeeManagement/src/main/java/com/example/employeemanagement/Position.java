package com.example.employeemanagement;

public class Position {
    public String position;
    public float salary;


    public Position() {
    }

    public Position(String position, float salary) {
        this.position = position;
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }
}
