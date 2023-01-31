package gui;

import database.dao.*;
import database.models.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControllerView implements Initializable {
    @FXML
    private ListView<Models> listView;

    @FXML
    private ListView<Models> searchSuggestions;

    @FXML
    private TextField searchTextField;

    private ArrayList<Models> resultSuggestionsModels;

    private ObservableList<Models> modelsObservableList = FXCollections.observableArrayList();
    private ObservableList<Models> listSuggestions = FXCollections.observableArrayList();

    private ArrayList<CharacterModels> listCharacter = CharacterDAO.getInstance().selectAll();
    private ArrayList<DynastyModels> listDynasty = DynastyDAO.getInstance().selectAll();
    private ArrayList<EventModels> listEvent = EventDAO.getInstance().selectAll();
    private ArrayList<FestivalModels> listFestival = FestivalDAO.getInstance().selectAll();
    private ArrayList<PlaceModels> listPlace = PlaceDAO.getInstance().selectAll();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setItems(modelsObservableList);

        searchSuggestions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        searchSuggestions.setItems(listSuggestions);

        searchTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldb, Boolean newb) {
                if (newb) {
                    // show suggestions for search
                    searchSuggestions.setVisible(true);
                    if (resultSuggestionsModels != null) {
                        listSuggestions.removeAll(listSuggestions);
                        listSuggestions.addAll(resultSuggestionsModels);
                    }
                    searchSuggestions.getSelectionModel().clearSelection();
                    searchSuggestions.setPrefHeight(listSuggestions.size() * 24);
                    searchSuggestions.setMaxHeight(200);
                } else {
                    // hide suggestions
                    if (!searchSuggestions.isFocused()) {
                        listSuggestions.removeAll(listSuggestions);
                        searchSuggestions.setVisible(false);
                    }
                }
            }
        });

        searchSuggestions.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldb, Boolean newb) {
                if (oldb) {
                    // hide suggestions
                    listSuggestions.removeAll(listSuggestions);
                    searchSuggestions.setVisible(false);
                }
            }
        });
    }

    /**
     * Hiển thị tất cả nhân vật lịch sử
     * @param event
     */
    public void searchCharacter(ActionEvent event) {
        modelsObservableList.removeAll(modelsObservableList);
        modelsObservableList.addAll(listCharacter);
        listView.scrollTo(0);
    }

    /**
     * Hiển thị tất cả triều đại lịch sử
     * @param event
     */
    public void searchDynasty(ActionEvent event) {
        modelsObservableList.removeAll(modelsObservableList);
        modelsObservableList.addAll(listDynasty);
        listView.scrollTo(0);
    }

    /**
     * Hiển thị tất cả sự kiện lịch sử
     * @param event
     */
    public void searchEvent(ActionEvent event) {
        modelsObservableList.removeAll(modelsObservableList);
        modelsObservableList.addAll(listEvent);
        listView.scrollTo(0);
    }

    /**
     * Hiển thị tất cả lễ hội
     * @param event
     */
    public void searchFestival(ActionEvent event) {
        modelsObservableList.removeAll(modelsObservableList);
        modelsObservableList.addAll(listFestival);
        listView.scrollTo(0);
    }

    /**
     * Hiển thị tất cả di tích lịch sử
     * @param event
     */
    public void searchPlace(ActionEvent event) {
        modelsObservableList.removeAll(modelsObservableList);
        modelsObservableList.addAll(listPlace);
        listView.scrollTo(0);
    }

    /**
     * Hiển thị thông tin đối tượng được chọn
     * Trong trường hợp không có đối tượng được chọn, hiển thị ra thông báo
     * @param event
     * @throws IOException
     */
    public void detailModel(Event event) throws IOException {
        // get stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // create scene
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("detail.fxml"));
        Parent detailParent = fxmlLoader.load();
        Scene scene = new Scene(detailParent);
        scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());

        // change scene
        ControllerDetail controllerDetail = fxmlLoader.getController();
        Models selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            controllerDetail.setModel(selected);

            stage.setTitle("Detail");
            stage.setScene(scene);
        } else {
            // object has not been selected
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("XEM THÔNG TIN");
            alert.setHeaderText("Notification");
            alert.setContentText("Bạn chưa chọn đối tượng nào\nHãy chọn 1 đối tượng để xem thông tin");
            alert.show();
        }
    }

    /**
     * Tìm các đối tượng có từ khóa tương ứng theo cột
     * @param key từ khóa
     * @param columns cột chứa từ khóa cần tìm
     * @return các đối tượng có chứa từ khóa
     */
    public ArrayList<Models> listSearchResult(String key, String... columns) {
        ArrayList<Models> result = new ArrayList<>();
        StringBuilder avoidRepeating = new StringBuilder();
        for (String column : columns) {
            String condition = column + " LIKE " + "\"%" + key + "%\" " + avoidRepeating;

            result.addAll(CharacterDAO.getInstance().selectByCondition(condition));
            result.addAll(DynastyDAO.getInstance().selectByCondition(condition));
            result.addAll(EventDAO.getInstance().selectByCondition(condition));
            result.addAll(FestivalDAO.getInstance().selectByCondition(condition));
            result.addAll(PlaceDAO.getInstance().selectByCondition(condition));

            avoidRepeating.append("AND " + column + " NOT LIKE " + "\"%" + key + "%\" ");
        }

        return result;
    }

    /**
     * Tìm các đối tượng có thông tin chứa từ khóa và hiển thị ra listView
     * @param key từ khóa
     */
    public void search(String key) {
        ArrayList<Models> result = listSearchResult(key, "id", "name", "information");

        modelsObservableList.removeAll(modelsObservableList);
        modelsObservableList.addAll(result);
        listView.scrollTo(0);
    }

    /**
     * Tìm kiếm với thanh tìm kiếm
     * @param event
     */
    public void searchButton(ActionEvent event) {
        String key = searchTextField.getText();

        listSuggestions.removeAll(listSuggestions);
        searchSuggestions.setVisible(false);

        listView.requestFocus();
        search(key.trim());

        if (modelsObservableList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("TÌM KIẾM");
            alert.setHeaderText("Notification");
            alert.setContentText("Không tìm thấy " + "\"" + key + "\"" + " trong dữ liệu");
            alert.show();
        }
    }

    /**
     * Gợi ý tìm kiếm
     * @param event
     */
    public void pressKeyTextField(KeyEvent event) {
        String key = searchTextField.getText().trim();
        if (key.length() > 0) {
            resultSuggestionsModels = listSearchResult(key,  "name");

            listSuggestions.removeAll(listSuggestions);
            listSuggestions.addAll(resultSuggestionsModels);
        } else {
            listSuggestions.removeAll(listSuggestions);
        }
        searchSuggestions.setPrefHeight(listSuggestions.size() * 24);
    }

    /**
     * Lựa chọn gợi ý tìm kiếm với phím mũi tên
     * @param event
     */
    public void textFieldUpDown(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            if (searchSuggestions.getSelectionModel().getSelectedItem() != null) {
                searchSuggestions.getSelectionModel().selectPrevious();
            } else {
                searchSuggestions.getSelectionModel().selectLast();
            }
            searchTextField.setText(searchSuggestions.getSelectionModel().getSelectedItem().getName());
        } else if (event.getCode() == KeyCode.DOWN) {
            if (searchSuggestions.getSelectionModel().getSelectedItem() != null) {
                searchSuggestions.getSelectionModel().selectNext();
            } else {
                searchSuggestions.getSelectionModel().selectFirst();
            }
            searchTextField.setText(searchSuggestions.getSelectionModel().getSelectedItem().getName());
        }
    }

    /**
     * Tìm kiếm với từ khóa gợi ý
     */
    public void clickSuggestion() {
        Models selected = searchSuggestions.getSelectionModel().getSelectedItem();

        listSuggestions.removeAll(listSuggestions);
        searchSuggestions.setVisible(false);

        String key = selected.getName();
        searchTextField.setText(key);

        listView.requestFocus();
        search(key.trim());
    }

    /**
     * Làm mới thanh tìm kiếm khi ấn nút trên gợi ý tìm kiếm
     * Nếu ấn ENTER, tìm kiếm với gợi ý đã chọn
     * @param event
     */
    public void refreshSearchTextField(KeyEvent event) {
        Models selected = searchSuggestions.getSelectionModel().getSelectedItem();
        searchTextField.setText(selected.getName());
        if (event.getCode() == KeyCode.ENTER) {
            search(selected.getName());

            listSuggestions.removeAll(listSuggestions);
            searchSuggestions.setVisible(false);
            listView.requestFocus();
        }
    }

    /**
     * Hiện thị thông tin đối tượng khi ấn ENTER trên danh sách tìm kiếm
     * @param event
     * @throws IOException
     */
    public void checkEnterListView(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            detailModel(event);
        }
    }

    /**
     * Chọn kết quả tìm kếm đầu tiên
     * @param event
     */
    public void selectFirst(ActionEvent event) {
        listView.getSelectionModel().selectFirst();
    }

    /**
     * Chọn kết quả tìm kếm cuối cùng
     * @param event
     */
    public void selectLast(ActionEvent event) {
        listView.getSelectionModel().selectLast();
    }

    /**
     * Chọn kết quả tìm kếm phía trên
     * @param event
     */
    public void selectPrevious(ActionEvent event) {
        listView.getSelectionModel().selectPrevious();
    }

    /**
     * Chọn kết quả tìm kếm phía dưới
     * @param event
     */
    public void selectNext(ActionEvent event) {
        listView.getSelectionModel().selectNext();
    }

    /**
     * Bỏ chọn đối tượng tại danh sách tìm kiếm
     * @param event
     */
    public void clearSelection(ActionEvent event) {
        listView.scrollTo(0);
        listView.getSelectionModel().clearSelection();
    }
}
