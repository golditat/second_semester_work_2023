package com.example.printme.controllers;

import com.example.printme.helpers.Message;
import com.example.printme.server.Server;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;

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
    private ListView chatPane;

    @FXML
    private Label username;

    @FXML
    private Label messageText;

    @FXML
    private TextArea textArea;


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

    public void message(){
        username.setText("user");
        messageText.setText(textArea.getText());
    }

}
