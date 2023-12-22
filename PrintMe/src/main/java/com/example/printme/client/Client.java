package com.example.printme.client;

import com.example.printme.application.PrintMeApplication;
import com.example.printme.controllers.GameController;
import com.example.printme.helpers.ClientHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Client extends Application {

    private Socket socket;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println("get connection...");
        initConnection();
        PrintMeApplication app = new PrintMeApplication();
        Platform.runLater(()-> {
            ClientHandler handler = new ClientHandler(socket, app.getLoader());
            GameController controller = app.getLoader().getController();
            controller.setMessageListener(handler);
            try {
                app.start(primaryStage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("done start");
            try {
                handler.getWriter().writeObject("user");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            new Thread(handler).start();
        });
    }

    private void initConnection() {
        try {
            socket = new Socket("localhost", 1099);
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибки подключения к серверу
        }
    }

    private void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибки закрытия соединения
        }
    }
}