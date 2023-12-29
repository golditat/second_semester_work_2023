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
     private PrintMeApplication app;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        PrintMeApplication app = new PrintMeApplication();
        app.setClient(this);
        this.app = app;
        Platform.runLater(()-> {
            try {
                app.start(primaryStage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("done start");
        });
    }

    public void handleNextScreenEvent(String username) {
        System.out.println("Переход на следующий экран выполнен");
        Platform.runLater(()-> {
            initConnection();
            ClientHandler handler = new ClientHandler(socket, app.getLoader());
            handler.setUsername(username);
            try {
                handler.getWriter().writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            GameController controller = app.getLoader().getController();
            controller.setMessageListener(handler);
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