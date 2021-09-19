package com.code.database.services;

import com.code.database.Exceptions.DeletedFileException;
import com.code.database.dao.FileService;
import com.code.database.models.Project;
import com.code.database.models.Role;
import com.code.database.models.Status;
import com.code.database.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ProjectFileService implements FileService<Project> {
    private static final int CHARACTERS_PER_LINE = 60;
    private static ProjectFileService instance;
    private final ReentrantReadWriteLock lock;
    private final RandomAccessFile file;

    public ProjectFileService() throws FileNotFoundException {
        lock = new ReentrantReadWriteLock();
        file = new RandomAccessFile("project.dat","rw");
    }

    public static ProjectFileService getInstance() throws FileNotFoundException {
        if (instance == null){
            instance = new ProjectFileService();
        }
        return instance;
    }

    public Project read( int line){
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
//        if (Info[3].equals(Status.deleted.toString())){
//            throw new DeletedFileException();
//        }

        int id = Integer.parseInt(Info[0]);
        String name = Info[1].trim();
        String department = Info[2].trim();
        String status = Info[3].trim();

        Project project = Project.newInstance();
        project.setId(id);
        project.setName(name);
        project.setDepartment(department);
        project.setStatus(Status.valueOf(status));
        lock.readLock().unlock();

        return project;
    }

    public void write( Project project, int line)  {
        lock.writeLock().lock();


      try{
          String data = project.toString();
          // Adding two bytes to the total length because of the '\n' character at the end of the line
          int bytesPerLine = CHARACTERS_PER_LINE + 2;
          long cursor;

          if (line == -1){
              cursor = file.length();
          }else{
              cursor = (long) bytesPerLine * line;
          }

          file.seek(cursor);
          file.write(data.getBytes());
          file.writeBytes(System.getProperty("line.separator"));
//        file.close();
          lock.writeLock().unlock();
      } catch (IOException e) {
          e.printStackTrace();
      }

    }


    public int numOfLines() {
        Path path = Paths.get("project.dat");
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
