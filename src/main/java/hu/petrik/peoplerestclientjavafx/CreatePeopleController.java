package hu.petrik.peoplerestclientjavafx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class CreatePeopleController extends Controller {
    @FXML
    private TextField nevField;
    @FXML
    private TextField emailField;
    @FXML
    private Spinner<Integer> PizzameretField;
    @FXML
    private Button submitButton;

    @FXML
    private void initialize() {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 200, 30);
        PizzameretField.setValueFactory(valueFactory);
    }

    @FXML
    public void submitClick(ActionEvent actionEvent) {
        String name = nevField.getText().trim();
        String email = emailField.getText().trim();
        int age = PizzameretField.getValue();
        if (name.isEmpty()) {
            warning("Név szükséges");
            return;
        }
        if (email.isEmpty()) {
            warning("Email is required");
            return;
        }
        // TODO: validate email format
        Rendeles newPerson = new Rendeles(0, name, email, age);
        Gson converter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = converter.toJson(newPerson);
        try {
            Response response = RequestHandler.post(App.BASE_URL, json);
            if (response.getResponseCode() == 201) {
                warning("Rendelés hozzáadva!");
                nevField.setText("");
                emailField.setText("");
                PizzameretField.getValueFactory().setValue(32);
            } else {
                String content = response.getContent();
                error(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
