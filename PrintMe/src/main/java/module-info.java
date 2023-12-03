module com.example.printme {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.printme to javafx.fxml;
    exports com.example.printme.controllers;
    exports com.example.printme.application;
    opens com.example.printme.application to javafx.fxml;
    opens com.example.printme.controllers to javafx.fxml;
}