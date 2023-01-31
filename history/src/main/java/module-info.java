module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires json.simple;
    requires com.jfoenix;
    requires org.jsoup;


    opens gui to javafx.fxml;
    exports gui;
}