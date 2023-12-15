package com.example.printme.controllers;

import com.example.printme.helpers.Message;
import com.example.printme.helpers.MessageListener;
import com.example.printme.server.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;

import java.io.IOException;

public class GameController {

    @FXML
    private Canvas canvas;
    
    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TextField brushSize;

    @FXML
    private CheckBox eraser;

    @FXML
    private Label senderName;

    @FXML
    private Label messageText;

    @FXML
    private TextField textFild;

    private MessageListener messageListener;

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
        System.out.println("listener done");
    }

    public void initialize() {
        GraphicsContext g = canvas.getGraphicsContext2D();

        canvas.setOnMouseDragged(e -> {
            double size = Double.parseDouble(brushSize.getText());
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;

            if (eraser.isSelected()) {
                g.clearRect(x, y, size, size);
            } else {
                g.setFill(colorPicker.getValue());
                g.fillRect(x, y, size, size);
            }
        });
    }

    public void addmessage() throws IOException {
        senderName.setText("user");
        messageText.setText(textFild.getText());
        System.out.println("message");
        if(messageListener != null){
            messageListener.onMessageAdded("user", textFild.getText());
        }
    }

    public void onGettingMessage(Message message){
        Platform.runLater(() -> {
            senderName.setText(message.userName);
            messageText.setText(message.text);
            System.out.println("get message");
        });
    }
}
