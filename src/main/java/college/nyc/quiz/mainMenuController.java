package college.nyc.quiz;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class mainMenuController {
    @FXML
    private Label welcomeLabel;

    public void welcomeUser(String username){
        welcomeLabel.setText("Welcome to Project Quiz, "+ username+"!");
    }
}
