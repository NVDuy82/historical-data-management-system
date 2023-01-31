package gui;

import crawler.*;
import database.dao.*;
import database.jdbc.JDBCUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

public class ControllerMenu {
    /**
     * Thu thập dữ liệu trên web và thêm vào database
     * @param event
     * @throws IOException
     */
    public void crawlAndUpdateData(ActionEvent event) throws IOException {
        // crawl
        Crawl.run();

        // insert database
        try {
            Connection connectData = JDBCUtil.getConnection();

            Statement statement = connectData.createStatement();

            String urlString = "CREATE DATABASE history";

            statement.executeUpdate(urlString);

            JDBCUtil.closeConnection(connectData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ConnectDAO.getInstance().createTable();
        DynastyDAO.getInstance().createTable();
        DynastyDAO.getInstance().insertData("data/DynastyNKS.json");
        CharacterDAO.getInstance().createTable();
        CharacterDAO.getInstance().insertData("data/CharacterNKS.json");
        EventDAO.getInstance().createTable();
        EventDAO.getInstance().insertData("data/EventNKS.json");
        PlaceDAO.getInstance().createTable();
        PlaceDAO.getInstance().insertData("data/PlaceWiki.json");
        PlaceDAO.getInstance().insertData("data/PlaceNKS.json");
        FestivalDAO.getInstance().createTable();
        FestivalDAO.getInstance().insertData("data/FestivalWiki.json");

    }

    /**
     * Mở giao diện tra cứu thông tin
     * @param event
     * @throws IOException
     */
    public void start(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Main.class.getResource("stylesheet.css").toExternalForm());

        Stage stage = new Stage();
        stage.setTitle("Lịch Sử Việt Nam");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
