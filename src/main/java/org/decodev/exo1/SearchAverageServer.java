package org.decodev.exo1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class SearchAverageServer {

    private final HashMap<String, Double> table;
    public SearchAverageServer(){
        table = new HashMap<String, Double>();
        table.put("badr", 13.5);
        table.put("saleh", 15.5);
        table.put("youcef", 9.5);
        table.put("anes", 8.5);
    }

    public double getAverage(String name){
        if(table.containsKey(name)){
            return table.get(name);
        }
        return -1;
    }
    public boolean isAdmit(String name){
        if(table.containsKey(name)){
            return table.get(name) >= 10;
        }
        return false;
    }


    public static void main(String[] args){
        SearchAverageServer server = new SearchAverageServer();

        System.out.println("Server started, Waiting for connection...");
        try(ServerSocket serverSocket = new ServerSocket(7777)){
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                String name = null;

                while((name = dis.readUTF()) != null){
                    System.out.println(name);
//                    dos.writeDouble(server.getAverage(name));
                    dos.writeBoolean(server.isAdmit(name));
                    dos.flush();
                }
                socket.close();
                dos.close();
                dis.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
