package com.code.database;

import com.code.database.dao.ProjectDAO;
import com.code.database.dao.UserDAO;
import com.code.database.models.Project;
import com.code.database.models.Role;
import com.code.database.models.Status;
import com.code.database.models.User;
import com.code.database.server.Request;
import com.code.database.services.ProjectFileService;
import com.code.database.services.UserFileService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {

        UserDAO userDAO = UserDAO.getInstance();
        HashMap<String,Object> map =new HashMap<String,Object>();
        map.put("username","heyam");
        map.put("password","8888888");
//        map.put("role","user");

        HashMap<String,Object> map2 =new HashMap<String,Object>();
        map2.put("name","final");
        map2.put("department","cis");


        map.put("projectID",2);
        userDAO.create(map);
//        projectDAO.create(map);
        System.out.println(userDAO.readAll());
        userDAO.update(map,0);
        System.out.println(userDAO.readAll());
//        userDAO.update(map,1);
//        userDAO.update(map,3);
        userDAO.delete(2);
        System.out.println(userDAO.readAll());
//
//        System.out.println(userDAO.getByProject(1));
//        ObjectMapper objectMapper= new ObjectMapper();
//        Project project = Project.newInstance();
//        List<Project> projects = null;
//
//        Request req = new Request();
//        Request req2 = new Request();
//        req.table="user";
//        req.operation="read";
//        req.data=map;
//        req2.table="project";
//        req2.operation="create";
//        req2.data=map2;
////        req.data.put("password","456");
//        System.out.println("-------------");
//        System.out.println(req.data);
//        ClientTest.getInstance().requestRunner(req);
//        System.out.println(req.table=="user");
//        System.out.println(req.table.equals("user"));
//         ClientTest.getInstance().requestRunner(req);
//        ClientTest.getInstance().requestRunner(req2);
//        projects= Arrays.asList(objectMapper.readValue(result, Project[].class));
//        System.out.println(result);
//        System.out.println(result2);



    }
}
