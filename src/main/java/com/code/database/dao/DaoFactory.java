package com.code.database.dao;

public class DaoFactory {
    public static Object getInstance(String table){
        switch (table){
            case "user":
                return new UserDAO();
            case "project":
                return new ProjectDAO();
            default:
                return null;
        }
    }
}
