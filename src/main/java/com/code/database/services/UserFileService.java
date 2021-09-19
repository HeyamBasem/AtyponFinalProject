package com.code.database.services;

import com.code.database.Exceptions.DeletedFileException;
import com.code.database.dao.FileService;
import com.code.database.models.*;
import com.code.database.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UserFileService implements FileService<User> {
    private static final int CHARACTERS_PER_LINE = 50;
    private static UserFileService instance;
    private final ReentrantReadWriteLock lock;
    private final RandomAccessFile file;

    public UserFileService() throws FileNotFoundException {
        lock = new ReentrantReadWriteLock();
        file = new RandomAccessFile("user.dat", "rw");

    }

    public static UserFileService getInstance() throws FileNotFoundException {
        if (instance == null) {
            instance = new UserFileService();
        }
        return instance;
    }

    public User read(int line) {
        lock.readLock().lock();
        String data = "";

        // Adding two bytes to the total length because of the '\n' character at the end of the line
        int bytesPerLine = CHARACTERS_PER_LINE + 2;

        try {
            file.seek((long) bytesPerLine * line);
            data = file.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String[] Info = data.split("/");
//        for(String s:Info){
//            System.out.println(s);
//        }
        if (Info[4].equals(Status.deleted.toString())) {
            throw new DeletedFileException();
        }

        int id = Integer.parseInt(Info[0]);
        String username = Info[1].trim();
        String password = Info[2].trim();
        int projectID = Integer.parseInt(Info[3]);
        String role = Info[4].trim();
        String status = Info[5].trim();

        User user = User.newInstance();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setProjectID(projectID);
        user.setRole(Role.valueOf(role));
        user.setStatus(Status.valueOf(status));

        lock.readLock().unlock();

        return user;
    }

    public void write(User user, int line) {
        try {
            lock.writeLock().lock();


            String data = user.toString();
            // Adding two bytes to the total length because of the '\n' character at the end of the line
            int bytesPerLine = CHARACTERS_PER_LINE + 2;
            long cursor;

            if (line == -1) {
                cursor = file.length();
            } else {
                cursor = (long) bytesPerLine * line;
            }

            file.seek(cursor);
            file.write(data.getBytes());
            file.writeBytes(System.getProperty("line.separator"));
//
            lock.writeLock().unlock();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public int numOfLines() {
        Path path = Paths.get("user.dat");
        int lines = 0;
        try {

            // much slower, this task better with sequence access
            //lines = Files.lines(path).parallel().count();

            lines = (int) Files.lines(path).count();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;

    }
}
