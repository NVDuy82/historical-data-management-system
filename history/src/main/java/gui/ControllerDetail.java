package gui;

import database.dao.ConnectDAO;
import database.models.Models;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerDetail {
    @FXML
    private Button homeButton;
    @FXML
    private Label idLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private TextArea infoText;

    @FXML
    private ListView<String> connectListView;

    private ObservableList<String> connectObservableList;

    /**
     * Thiết lập thông tin cho đối tượng được hiển thị
     * @param model đối tượng muốn hiển thị
     */
    public void setModel(Models model) {
        this.idLabel.setText(model.getId());
        this.nameLabel.setText(model.getName());
        this.infoText.setText(model.getInfo());

        ArrayList<String > connectList = ConnectDAO.getInstance().selectById(model.getName());
        connectObservableList = FXCollections.observableArrayList(connectList);
        this.connectListView.setItems(connectObservableList);
        connectListView.getSelectionModel().clearSelection();
    }

    /**
     * Trở về trang tìm kiếm đối tượng
     * @param event
     * @throws IOException
     */
    public void backHome(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view.fxml"));
        Parent homeParent = loader.load();
        Scene scene = new Scene(homeParent);
        scene.getStylesheets().add(Main.class.getResource("stylesheet.css").toExternalForm());

        stage.setTitle("Lịch Sử Việt Nam");
        stage.setScene(scene);
    }

    /**
     * Hiển thị thông tin đối tượng được chọn
     */
    public void showConnect() {
        String selected = connectListView.getSelectionModel().getSelectedItem();
        connectListView.getSelectionModel().clearSelection();
        homeButton.requestFocus();

        try {
            Models connect = ConnectDAO.getInstance().search(selected);
            setModel(connect);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Notification");
            alert.setContentText("Không có thông tin để hiển thị hoặc đã xảy ra LỖI");
            alert.show();
        }
    }
}
