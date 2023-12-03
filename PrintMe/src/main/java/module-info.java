module com.example.printme {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.printme to javafx.fxml;
    exports com.example.printme;
}