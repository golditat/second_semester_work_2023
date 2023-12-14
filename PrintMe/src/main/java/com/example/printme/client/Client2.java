package com.example.printme.client;

import com.example.printme.application.PrintMeApplication;
import javafx.application.Application;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client2 extends Application {
    private Socket socket;
    private BufferedReader reader;
    private OutputStreamWriter writer;

    private TextArea chatArea;
    private TextField inputField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        initConnection();


        PrintMeApplication app = new PrintMeApplication();
        app.start(primaryStage);

        // Запускаем поток для чтения сообщений от сервера
        Thread readerThread = new Thread(this::readMessages);
        readerThread.setDaemon(true);
        readerThread.start();
    }

    private void initConnection() {
        try {
            socket = new Socket("localhost", 1099); // Укажите IP и порт сервера
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new OutputStreamWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибки подключения к серверу
        }
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            try {
                writer.write(message + "\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
                // Обработка ошибки отправки сообщения
            }
            inputField.clear();
        }
    }

    private void readMessages() {
        try {
            while (true) {
                String message = reader.readLine();
                if (message == null) {
                    break; // Сервер закрыл соединение
                }
                // Обработка полученных сообщений, например, отображение в чате
                // chatArea.appendText(message + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибки чтения сообщения
        } finally {
            closeConnection();
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
