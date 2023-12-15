package com.example.printme.application;

import com.example.printme.controllers.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PrintMeApplication extends Application {
    private FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/printme/game-view.fxml"));
    private Parent root;

    public PrintMeApplication() throws IOException {
        root = loader.load();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(root);
        stage.setTitle("Print Me");
        stage.setScene(scene);
        stage.show();
    }
    public Parent getRoot(){
        return root;
    }

    public void setController(GameController controller){
        loader.setController(controller);
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
