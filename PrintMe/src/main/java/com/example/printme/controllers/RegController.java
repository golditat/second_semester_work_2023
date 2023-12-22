package com.example.printme.controllers;

import com.example.printme.application.PrintMeApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegController {
    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Button register;

    private PrintMeApplication app;

    public void setLoader(PrintMeApplication app) {
        this.app = app;
    }

    public void onButtonClick(){
        if(username.getText() != null) {
            String enteredUsername = username.getText();
            String enteredPassword = password.getText();
            app.username = enteredUsername;
            app.password = enteredPassword;
            openGameScreen();
        }
    }

    private void openGameScreen() {
        app.openNextScreen();
    }
}
