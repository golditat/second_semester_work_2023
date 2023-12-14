package com.example.printme.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private Map<String, PrintWriter> clients;
    private Map<String, String> clientRooms;
    private Map<String, Integer> clientCount;

    public ChatServer() throws IOException {
        serverSocket = new ServerSocket(1099);
        executorService = Executors.newCachedThreadPool();
        clients = new ConcurrentHashMap<>();
        clientRooms = new ConcurrentHashMap<>();
        clientCount = new ConcurrentHashMap<>();
    }

    public void start() {
        System.out.println("Server is running...");
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client done");
                executorService.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String username;


        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
            } catch (IOException e) {
                e.printStackTrace();
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
                        broadcastMessage(username , message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                handleDisconnect();
            }
        }

        private void assignRoom() {
            String availableRoom = clientCount.entrySet().stream()
                    .filter(entry -> clientCount.getOrDefault(entry.getKey(), 0) < entry.getValue())
                    .findFirst()
                    .map(Map.Entry::getKey)
                    .orElse(null);

            if (availableRoom != null) {
                clientRooms.put(username, availableRoom);
                clientCount.put(availableRoom, clientCount.getOrDefault(availableRoom, 0) + 1);
                writer.println("Joined room: " + availableRoom);
            } else {
                writer.println("All rooms are full. Cannot join a room at the moment.");
            }
        }

        private void broadcastMessage(String usernsme, String message) {
            String room = clientRooms.get(username);
            clients.entrySet().stream()
                    .filter(entry -> entry.getValue() != writer && (room == null || room.equals(clientRooms.get(entry.getKey()))))
                    .forEach(entry -> entry.getValue().println(message));
        }

        private void handleDisconnect() {
            System.out.println(username + " disconnected.");
            clients.remove(username);
            clientRooms.remove(username);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            ChatServer server = new ChatServer();
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
