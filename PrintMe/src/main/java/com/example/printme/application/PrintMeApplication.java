package com.example.printme.application;

import com.example.printme.client.Client;
import com.example.printme.controllers.GameController;
import com.example.printme.controllers.RegController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PrintMeApplication extends Application {
    private FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/printme/reg-view.fxml"));
    private Parent root;

    public String username;

    public String password;

    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    public PrintMeApplication() throws IOException {
        root = loader.load();
        RegController controller = loader.getController();
        controller.setLoader(this);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(root);
        stage.setTitle("Print Me");
        stage.setScene(scene);
        stage.show();
    }

    public void openNextScreen() {
        try {
            FXMLLoader nextLoader = new FXMLLoader(getClass().getResource("/com/example/printme/game-view.fxml"));
            Stage currentStage = (Stage) root.getScene().getWindow();
            loader = nextLoader;
            Parent nextRoot = nextLoader.load();
            root = nextRoot;
            GameController controller =loader.getController();
            controller.username = username;

            currentStage.setScene(new Scene(nextRoot));

            client.handleNextScreenEvent(username);
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибок при загрузке следующего экрана
        }
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
