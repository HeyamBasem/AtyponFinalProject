package com.code.database.dao;

import com.code.database.cache.Cache;
import com.code.database.cache.CacheUsers;
import com.code.database.models.Project;
import com.code.database.models.Role;
import com.code.database.models.Status;
import com.code.database.models.User;
import com.code.database.services.UserFileService;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class UserDAO {
    private static final int CACHE_SIZE = 10;
    private UserFileService userFileService;
    CacheUsers<Integer, User > cache = CacheUsers.getInstance(CACHE_SIZE);


    {
        try {
            userFileService = new UserFileService();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //    static factory effective java
    public static UserDAO getInstance(){
        return new UserDAO();
    }
    public User Extractuser(HashMap<String,Object> map){
        User user = User.newInstance();
        user.setId(generateId());
        user.setUsername((String) map.get("username"));
        user.setPassword((String) map.get("password"));
        user.setRole(Role.valueOf((String) map.get("role")));
        return user;

    }
    private int generateId(){
        return userFileService.numOfLines();
    }

    public void create(HashMap<String,Object> userMap){
        User user = Extractuser(userMap);
        user.status= Status.active;
        user.role=Role.user;
        userFileService.write(user,generateId());
        if ((cache.size() == cache.getSize())) {
            User userFromCache = cache.entrySet().iterator().next().getValue();
            userFileService.write(userFromCache,userFromCache.getId());
        }
        cache.put(user.getId(), user);
    }

    public void update(HashMap<String,Object> userMap, int id) {
        User user = userFileService.read(id);
        cache.put(id, user);
        for (String key : userMap.keySet()) {
            try {
                Field field = user.getClass().getDeclaredField(key);
                field.set(user, userMap.get(key));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }

        }
        userFileService.write(user,id);
    }
    public ArrayList<User> readAll(){
        ArrayList<User> users = new ArrayList<>();
        for(int i=0; i < userFileService.numOfLines();i++){
            if (!cache.containsKey(i)) {
                User user = userFileService.read(i);
                if(user.getStatus()!= Status.deleted)
                    users.add(user);
            }

        }
        return users;
    }
    public ArrayList<User> getByProject(int id){
        ArrayList<User> users = new ArrayList<>();
        for(int i=0; i < userFileService.numOfLines();i++){
            User user = userFileService.read(i);
            if(user.getStatus()!= Status.deleted && user.getProjectID() == id)
                users.add(user);
        }
        return users;
    }
    public void delete(int id){
        if (cache.containsKey(id)) {
            cache.remove(id);
        }
        User user = userFileService.read(id);
        user.setStatus(Status.deleted);
        userFileService.write(user,id);
    }

}
