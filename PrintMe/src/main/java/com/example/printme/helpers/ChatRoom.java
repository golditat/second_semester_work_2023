package com.example.printme.helpers;

import com.example.printme.server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
    private String roomName;
    private List<Server.ClientHandler> clients;
    private PrintWriter roomWriter;
    public Integer getClientsCount(){
        return clients.size();
    }

    public ChatRoom(String roomName) throws IOException {
        this.roomName = roomName;
        this.clients = new ArrayList<>();
        this.roomWriter = new PrintWriter("Room_" + roomName + "_Log.txt");
    }

    public void broadcastMessage(String message, Server.ClientHandler sender) {
        for (Server.ClientHandler client : clients) {
            // Не отправлять сообщение отправителю
            if (client != sender) {
                client.sendMessage(message);
            }
        }
        // Записать сообщение в лог комнаты
        roomWriter.println(message);
        roomWriter.flush();
    }

    public void addClient(Server.ClientHandler client) {
        clients.add(client);
    }

    public void removeClient(Server.ClientHandler client) {
        clients.remove(client);
    }

    public String getRoomName() {
        return roomName;
    }
}
