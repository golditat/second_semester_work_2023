package com.example.printme.helpers;

import com.example.printme.server.Server;
import com.example.printme.server.ServerHandlerClient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
    private String roomName;
    private List<ServerHandlerClient> clients;
    private ObjectOutputStream roomWriter;
    public Integer getClientsCount(){
        return clients.size();
    }

    public ChatRoom(String roomName) throws IOException {
        this.roomName = roomName;
        this.clients = new ArrayList<>();
    }

    public void broadcastMessage(Message message) throws IOException {
        for (ServerHandlerClient client : clients) {
            // Не отправлять сообщение отправителю
            //if (!client.getUsername().equals(message.userName)) {
                client.sendMessage(message);
            //}
        }
        // Записать сообщение в лог комнаты
    }
    public void broadcastCanvasState(CanvasState canvasState) throws IOException {
        for (ServerHandlerClient client : clients) {
            // Не отправлять сообщение отправителю
            //if (!client.getUsername().equals(message.userName)) {
            client.sendCanvasState(canvasState);
            //}
        }
        // Записать сообщение в лог комнаты
    }

    public void addClient(ServerHandlerClient client) {
        clients.add(client);
    }

    public void removeClient(ServerHandlerClient client) {
        clients.remove(client);
    }

    public String getRoomName() {
        return roomName;
    }
}
