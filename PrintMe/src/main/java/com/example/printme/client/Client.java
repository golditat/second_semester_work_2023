package com.example.printme.client;

import com.example.printme.application.PrintMeApplication;
import com.example.printme.controllers.GameController;
import com.example.printme.helpers.ClientHandler;
import com.example.printme.helpers.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Client extends Application {

    private Socket socket;
    private ClientHandler handler;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println("get connection...");
        initConnection();
        System.out.println("done socket");
        PrintMeApplication app = new PrintMeApplication();
        System.out.println("done app child");
        Platform.runLater(()-> {
            System.out.println("in rulLater");
            ClientHandler handler = new ClientHandler(socket, app.getLoader());
            this.handler = handler;
            System.out.println("done hendler");
            GameController controller = app.getLoader().getController();
            controller.setMessageListener(handler);
            System.out.println("done controller");
            System.out.println("done thread");
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
            socket = new Socket("localhost", 1099); // Укажите IP и порт сервера
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