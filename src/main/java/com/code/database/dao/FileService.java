package com.code.database.dao;

public interface FileService<T> {
    public T read(int line);
    public void write(T record,int line);
    public int numOfLines();
}
