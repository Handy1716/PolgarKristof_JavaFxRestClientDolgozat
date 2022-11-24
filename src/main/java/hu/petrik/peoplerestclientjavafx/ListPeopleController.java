package hu.petrik.peoplerestclientjavafx;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ListPeopleController extends Controller {

    @FXML
    private Button hozzaadButton;
    @FXML
    private Button valztoztatasButton;
    @FXML
    private Button torlesButton;
    @FXML
    private TableView<Rendeles> peopleTable;
    @FXML
    private TableColumn<Rendeles, String> nevCol;
    @FXML
    private TableColumn<Rendeles, String> cimCol;
    @FXML
    private TableColumn<Rendeles, Integer> meretCol;
    @FXML
    private TableColumn<Rendeles, String> PizzaCol;


    @FXML
    private void initialize() {
        // getName() függvény eredményét írja ki
        nevCol.setCellValueFactory(new PropertyValueFactory<>("nev"));
        cimCol.setCellValueFactory(new PropertyValueFactory<>("cim"));
        meretCol.setCellValueFactory(new PropertyValueFactory<>("meret"));
        Platform.runLater(() -> {
            try {
                loadPeopleFromServer();
            } catch (IOException e) {
                error("Couldn't get data from server", e.getMessage());
                Platform.exit();
            }
        });
    }

    private void loadPeopleFromServer() throws IOException {
        Response response = RequestHandler.get(App.BASE_URL);
        String content = response.getContent();
        Gson converter = new Gson();
        Rendeles[] people = converter.fromJson(content, Rendeles[].class);
        peopleTable.getItems().clear();
        for (Rendeles person : people) {
            peopleTable.getItems().add(person);
        }
    }

    @FXML
    public void insertClick(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("create-people-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            Stage stage = new Stage();
            stage.setTitle("Create People");
            stage.setScene(scene);
            stage.show();
            hozzaadButton.setDisable(true);
            valztoztatasButton.setDisable(true);
            torlesButton.setDisable(true);
            stage.setOnCloseRequest(event -> {
                hozzaadButton.setDisable(false);
                valztoztatasButton.setDisable(false);
                torlesButton.setDisable(false);
                try {
                    loadPeopleFromServer();
                } catch (IOException e) {
                    error("An error occurred while communicating with the server");
                }
            });
        } catch (IOException e) {
            error("Could not load form", e.getMessage());
        }
    }

    @FXML
    public void updateClick(ActionEvent actionEvent) {
        int selectedIndex = peopleTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            warning("Please select a person from the list first");
            return;
        }
        Rendeles selected = peopleTable.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("update-people-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            Stage stage = new Stage();
            stage.setTitle("Update People");
            stage.setScene(scene);
            UpdatePeopleController controller = fxmlLoader.getController();
            controller.setPerson(selected);
            stage.show();
            hozzaadButton.setDisable(true);
            valztoztatasButton.setDisable(true);
            torlesButton.setDisable(true);
            stage.setOnHidden(event -> {
                hozzaadButton.setDisable(false);
                valztoztatasButton.setDisable(false);
                torlesButton.setDisable(false);
                try {
                    loadPeopleFromServer();
                } catch (IOException e) {
                    error("An error occurred while communicating with the server");
                }
            });
        } catch (IOException e) {
            error("Could not load form", e.getMessage());
        }
    }

    @FXML
    public void deleteClick(ActionEvent actionEvent) {
        int selectedIndex = peopleTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            warning("Please select a person from the list first");
            return;
        }

        Rendeles selected = peopleTable.getSelectionModel().getSelectedItem();
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText(String.format("Are you sure you want to delete %s?", selected.getNev()));
        Optional<ButtonType> optionalButtonType = confirmation.showAndWait();
        if (optionalButtonType.isEmpty()) {
            System.err.println("Unknown error occurred");
            return;
        }
        ButtonType clickedButton = optionalButtonType.get();
        if (clickedButton.equals(ButtonType.OK)) {
            String url = App.BASE_URL + "/" + selected.getId();
            try {
                RequestHandler.delete(url);
                loadPeopleFromServer();
            } catch (IOException e) {
                error("An error occurred while communicating with the server");
            }
        }
    }
}