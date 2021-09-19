package com.code.database.server;

import com.code.database.dao.DaoFactory;
import com.code.database.dao.ProjectDAO;
import com.code.database.dao.UserDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ServletRequestHandler implements Runnable {
    private  Socket socket;
    private  PrintWriter output;
    private  BufferedReader input;
    static ObjectMapper objectMapper;
    public ServletRequestHandler(Socket socket) throws IOException {
        this.socket = socket;
        output = new PrintWriter(socket.getOutputStream(),true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        objectMapper = new ObjectMapper();
    }

    public void run() {
        try {
            String req = input.readLine();
            Request reqBody =objectMapper.readValue(req, Request.class);
            ProjectDAO projectDAO = ProjectDAO.getInstance();
            UserDAO userDAO = UserDAO.getInstance();
            if(reqBody.table.equals("user")){
                switch (reqBody.operation){
                    case "create":
                        userDAO.create(reqBody.data);
                        System.out.println("user created");
                        break;
                    case "read":
                        String s = objectMapper.writeValueAsString(userDAO.readAll());
                        output.println(s);
                        System.out.println("all users read");
                        break;
                    case "update":
                        userDAO.update(reqBody.data,(Integer) reqBody.data.get("id"));
                        System.out.println("user updated");
                        break;
                    case "delete":
                        userDAO.delete((Integer) reqBody.data.get("id"));
                        System.out.println("user deleted");
                        break;
                    case "getByProject":
                        userDAO.getByProject((Integer) reqBody.data.get("id"));
                        System.out.println("user list for specific project");
                        break;
                }


            }
            else if (reqBody.table.equals("project")){
                switch (reqBody.operation){
                    case "create":
                        projectDAO.create(reqBody.data);
                        System.out.println("project created");
                        break;
                    case "read":
                        String s = objectMapper.writeValueAsString(projectDAO.readAll());
                        output.println(s);
                        System.out.println("projects read");
                        break;
                    case "update":
                        projectDAO.update(reqBody.data,(Integer) reqBody.data.get("id"));
                        System.out.println("project update");
                        break;
                    case "delete":
                        projectDAO.delete((Integer) reqBody.data.get("id"));
                        System.out.println("project deleted");
                        break;
                }
            }
            socket.close();

        } catch (IOException e) {
            System.out.println("error 2");
            System.out.println(e.toString());
        }
    }
}
