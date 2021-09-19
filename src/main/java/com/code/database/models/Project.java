package com.code.database.models;

public class Project  {
    public int id;
    public String department;
    public String name;
    public Status status;

    public static Project newInstance() { return new Project(); }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.valueOf(id)+'/'+department+'/'+name+'/'+status;
    }
}
