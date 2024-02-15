package college.nyc.quiz;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class projectController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}