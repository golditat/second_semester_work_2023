package com.example.printme.server;

import com.example.printme.helpers.ChatRoom;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<ChatRoom> rooms;
    private Map<ServerHandlerClient, ChatRoom> clientRooms;

    public Server() throws IOException {
        serverSocket = new ServerSocket(1099);
        rooms = new ArrayList<>();
        clientRooms = new HashMap<>();
    }

    private ChatRoom assignRoom(ServerHandlerClient client) throws IOException {
        ChatRoom availableRoom = null;
        for (ChatRoom room : rooms) {
            if(room.getClientsCount() < 4){
                availableRoom = room;
                break;
            }
        }
        if (availableRoom != null) {
            clientRooms.put(client, availableRoom);
            availableRoom.addClient(client);
            System.out.println("Joined room: " + availableRoom);
            return availableRoom;
        } else {
            ChatRoom newRoom = new ChatRoom("Room "+rooms.size()+1);
            clientRooms.put(client, newRoom);
            newRoom.addClient(client);
            rooms.add(newRoom);
            System.out.println("Added new room for this Client: " + newRoom);
            System.out.println(newRoom.getClientsCount());
            return newRoom;
        }
    }


    public void start() {
        System.out.println("Server is running...");
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("dfvsgbsedg");
                ServerHandlerClient handler = new ServerHandlerClient(clientSocket);
                handler.setClientRoom(assignRoom(handler));
                while (true) {
                    String username = (String) handler.getReader().readObject();
                    if (username == null) {
                        continue;
                    }else{
                        handler.setUsername(username);
                        break;
                    }
                }
                new Thread(handler).start();
                System.out.println("client connected");
            }
    } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            //executorService.shutdown();
        }
    }
    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}