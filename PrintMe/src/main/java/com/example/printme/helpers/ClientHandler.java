package com.example.printme.helpers;

import com.example.printme.controllers.GameController;
import javafx.fxml.FXMLLoader;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable, MessageListener, Serializable {
    private Socket socket;
    private String username;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private FXMLLoader loader;

    public ObjectOutputStream getWriter() {
        return writer;
    }

    public String getUsername() {
        return username;
    }
    public ObjectInputStream getReader(){
        return reader;
    }

    public ClientHandler(Socket clientSocket, FXMLLoader loader) {
        try {
            this.socket = clientSocket;
            this.loader = loader;
            this.writer = new ObjectOutputStream(clientSocket.getOutputStream());
            this.reader = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = (Message) reader.readObject();
                if (message == null || message.equals("/exit")) {
                    continue;
                } else {
                    onMessageGetting(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // Обработка отключения клиента
        }
    }

    public void sendMessage(Message message) throws IOException {
        System.out.println("message done");
        writer.writeObject(message);
    }

    @Override
    public void onMessageAdded(String sender, String message) throws IOException {
        System.out.println("message " + message);
        Message mess = new Message(message, sender);
        sendMessage(mess);
    }

    @Override
    public void onMessageGetting(Message message) throws IOException {
        GameController controller = loader.getController();
        controller.onGettingMessage(message);
        System.out.println("handler get message");
    }
}
