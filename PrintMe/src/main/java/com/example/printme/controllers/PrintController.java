package com.example.printme.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PrintController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}