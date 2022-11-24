package hu.petrik.peoplerestclientjavafx;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdatePeopleController extends Controller {
    @FXML
    private TextField nevField;
    @FXML
    private TextField cimField;
    @FXML
    private Spinner<Integer> meretField;
    @FXML
    private Button frissitesButton;

    private Rendeles person;

    public void setPerson(Rendeles person) {
        this.person = person;
        nevField.setText(this.person.getNev());
        cimField.setText(this.person.getCim());
        meretField.getValueFactory().setValue(this.person.getMeret());
    }

    @FXML
    private void initialize() {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 200, 30);
        meretField.setValueFactory(valueFactory);
    }

    @FXML
    public void updateClick(ActionEvent actionEvent) {
        String name = nevField.getText().trim();
        String email = cimField.getText().trim();
        int age = meretField.getValue();
        if (name.isEmpty()) {
            warning("Name is required");
            return;
        }
        if (email.isEmpty()) {
            warning("Email is required");
            return;
        }
        // TODO: validate email format
        this.person.setNev(name);
        this.person.setCim(email);
        this.person.setMeret(age);
        Gson converter = new Gson();
        String json = converter.toJson(this.person);
        try {
            String url = App.BASE_URL + "/" + this.person.getId();
            Response response = RequestHandler.put(url, json);
            if (response.getResponseCode() == 200) {
                Stage stage = (Stage) this.frissitesButton.getScene().getWindow();
                stage.close();
            } else {
                String content = response.getContent();
                error(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
