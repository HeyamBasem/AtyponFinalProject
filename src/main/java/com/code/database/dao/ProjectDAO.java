package com.code.database.dao;

import com.code.database.cache.Cache;
import com.code.database.models.Project;
import com.code.database.models.Status;
import com.code.database.models.User;
import com.code.database.services.ProjectFileService;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectDAO {
    private static final int CACHE_SIZE = 10;
    private ProjectFileService projectFileService;
    Cache<Integer, Project > cache = Cache.getInstance(CACHE_SIZE);

    {
        try {
            projectFileService = new ProjectFileService();
        } catch (FileNotFoundException e) {
            System.out.println("error4");
            e.printStackTrace();
        }
    }

    //    static factory effective java
    public static ProjectDAO getInstance(){
        return new ProjectDAO();
    }
    public Project ExtractProject(HashMap<String,Object> map){
        Project project = Project.newInstance();
        project.setId(generateId());
        project.setName((String) map.get("name"));
        project.setDepartment((String) map.get("department"));
        return project;

    }
    private int generateId(){
        return projectFileService.numOfLines();
    }

    public void create(HashMap<String,Object> projectMap){
        Project project = ExtractProject(projectMap);
        project.status=Status.active;
        projectFileService.write(project,generateId());
        if ((cache.size() == cache.getSize())) {
            Project projectFromCache = cache.entrySet().iterator().next().getValue();
            projectFileService.write(projectFromCache,projectFromCache.getId());
        }
        cache.put(project.getId(), project);
    }

    public void update(HashMap<String,Object> projectMap, int id) {
        Project project = projectFileService.read(id);
        cache.put(id, project);
        for (String key : projectMap.keySet()) {
            try {
                Field field = project.getClass().getDeclaredField(key);
                field.set(project, projectMap.get(key));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }

        }
        projectFileService.write(project,id);
    }
    public ArrayList<Project> readAll(){
        ArrayList<Project> projects = new ArrayList<>();
        for(int i=0; i < projectFileService.numOfLines();i++){
            if (!cache.containsKey(i)) {
                Project project = projectFileService.read(i);
                if (project.getStatus() != Status.deleted)
                    projects.add(project);
            }
        }
        return projects;
    }
    public void delete(int id){
        if (cache.containsKey(id)) {
            cache.remove(id);
        }
        Project project = projectFileService.read(id);
        project.setStatus(Status.deleted);
        projectFileService.write(project,id);
    }


}
