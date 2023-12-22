package com.example.printme.controllers;

import com.example.printme.helpers.CanvasState;
import com.example.printme.helpers.ColorSerial;
import com.example.printme.helpers.Message;
import com.example.printme.helpers.MessageListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;



public class GameController {

    @FXML
    private Canvas canvas;

    @FXML
    private ScrollPane messagePane;
    
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

    @FXML
    private Label word;

    @FXML
    private Button sendingButton;

    @FXML
    private VBox content;

    private MessageListener messageListener;

    private Boolean isCanvasClicable = true;

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
        System.out.println("listener done");
    }

    public void initialize() {
        GraphicsContext g = canvas.getGraphicsContext2D();

        canvas.setOnMouseDragged(e -> {
            if(isCanvasClicable) {
                double size = Double.parseDouble(brushSize.getText());
                double x = e.getX() - size / 2;
                double y = e.getY() - size / 2;

                CanvasState canvasState = new CanvasState(size, x, y, eraser.isSelected(), new ColorSerial(colorPicker.getValue()));

                if (eraser.isSelected()) {
                    g.clearRect(x, y, size, size);
                } else {
                    g.setFill(colorPicker.getValue());
                    g.fillRect(x, y, size, size);
                }
                try {
                    sendCanvasState(canvasState);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    private void sendCanvasState(CanvasState canvasState) throws IOException {
//        try {
//            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//            out.writeObject(canvasState);
//            out.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        messageListener.onCanvasStateAdded(canvasState);
    }

    public void addmessage() throws IOException {


        Label sender = new Label("user");
        sender.getStyleClass().add("label1");

        Label messText = new Label(textFild.getText());
        messText.getStyleClass().add("label2");

       content.getChildren().addAll(sender, messText);
        //senderName.setText("user");
        //messageText.setText(textFild.getText());
        System.out.println("message");
        if(messageListener != null){
            messageListener.onMessageAdded("user", textFild.getText());
        }
    }

    public void onGettingMessage(Message message){
        Platform.runLater(() -> {


            Label sender = new Label(message.userName);
            sender.getStyleClass().add("label1");

            Label messText = new Label(message.text);
            messText.getStyleClass().add("label2");

            content.getChildren().addAll(sender, messText);
             if(message.userName.equals("SERVER")){
                 canvas.getGraphicsContext2D().clearRect(1, 1, 1000, 1000);
             }
            //senderName.setText(message.userName);
            //messageText.setText(message.text);
            System.out.println("get message");
        });
    }
    public void onGettingCanvasState(CanvasState canvasState){
        GraphicsContext g = canvas.getGraphicsContext2D();
        if (canvasState.isEraser()) {
            g.clearRect(canvasState.getX(), canvasState.getY(), canvasState.getSize(), canvasState.getSize());
        } else {
            g.setFill(new Color(canvasState.getColor().getRed(), canvasState.getColor().getGreen(), canvasState.getColor().getBlue(), canvasState.getColor().getOpacity()));
            g.fillRect(canvasState.getX(), canvasState.getY(), canvasState.getSize(), canvasState.getSize());
        }
    }
    public void onTellerRole(){
        Platform.runLater(() -> {
            isCanvasClicable = false;
            colorPicker.setVisible(false);
            brushSize.setVisible(false);
            eraser.setVisible(false);
            word.setText("You are teller");
            textFild.setVisible(true);
            sendingButton.setVisible(true);
        });
    }

    public void onArterRole(String entword){
        Platform.runLater(() -> {
            isCanvasClicable = true;
            colorPicker.setVisible(true);
            brushSize.setVisible(true);
            eraser.setVisible(true);
            this.word.setText("You are drowing " + entword);
            textFild.setVisible(false);
            sendingButton.setVisible(false);
        });
    }

}
