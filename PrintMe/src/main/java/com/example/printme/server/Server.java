package com.example.printme.server;

import com.example.printme.client.Client;
import com.example.printme.controllers.GameController;
import com.example.printme.helpers.ChatRoom;
import com.example.printme.helpers.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<ChatRoom> rooms;
    private Map<ClientHandler, ChatRoom> clientRooms;

    public Server() throws IOException {
        serverSocket = new ServerSocket(1099);
        rooms = new ArrayList<>();
        clientRooms = new HashMap<>();
    }

    private void assignRoom(ClientHandler client) throws IOException {
        ChatRoom availableRoom = null;
        for (ChatRoom room : rooms) {
            if(room.getClientsCount() < 10){
                availableRoom = room;
                break;
            }
        }

        if (availableRoom != null) {
            clientRooms.put(client, availableRoom);
            availableRoom.addClient(client);
            System.out.println("Joined room: " + availableRoom);
        } else {
            ChatRoom newRoom = new ChatRoom("Room "+rooms.size()+1);
            clientRooms.put(client, newRoom);
            newRoom.addClient(client);
            rooms.add(newRoom);
            System.out.println("Added new room for this Client: " + newRoom);
            System.out.println(newRoom.getClientsCount());
        }
    }


    public void start() {
        System.out.println("Server is running...");
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");
                ClientHandler handler = new ClientHandler(clientSocket);
                assignRoom(handler);
                // Запуск отдельного потока для каждого клиента
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //executorService.shutdown();
        }
    }


    public class ClientHandler implements Runnable {
        private Socket clientSocket;
        private String username;
        private PrintWriter writer;
        private BufferedReader reader;
        private String roomName;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
                this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void closeConnection() {
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Обработка ошибки закрытия соединения
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = reader.readLine();
                    if (message == null || message.equals("/exit")) {
                        break;
                    } else {
                        ChatRoom room = clientRooms.get(this);
                        room.broadcastMessage(message, this);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Обработка отключения клиента
            }
        }

        public String getRoomName() {
            return roomName;
        }

        public void sendMessage(String message) {
            writer.println(message);
        }

        private void addMessage(Message message){
           writer.print(message);
        }

        private void readMessages() {
            try {
                while (true) {
                    String message = reader.readLine();
                    if (message == null) {
                        break;
                    }
                    // Обработка полученных сообщений, например, отображение в чате
                    // chatArea.appendText(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Обработка ошибки чтения сообщения
            } finally {
                //closeConnection();
            }
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