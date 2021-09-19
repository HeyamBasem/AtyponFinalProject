package com.code.database;

import com.code.database.server.Request;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTest
{
    private static Socket servletSocket;
    private static PrintWriter output;
    private static BufferedReader input;
    static ObjectMapper objectMapper;

    private ClientTest(){
        try{

            servletSocket = new Socket("localhost",5000);
            output = new PrintWriter(servletSocket.getOutputStream(),true);
            input = new BufferedReader(new InputStreamReader(servletSocket.getInputStream()));
            objectMapper = new ObjectMapper();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static ClientTest getInstance(){
        return new ClientTest();
    }
    public String requestRunner(Request requestBody){

        try{
            String req = objectMapper.writeValueAsString(requestBody);
            output.println(req);
            String res = input.readLine();
            servletSocket.close();
            return res;

        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("No data to show");
        return "";
    }
}
