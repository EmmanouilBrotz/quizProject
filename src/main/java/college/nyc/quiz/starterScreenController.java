package college.nyc.quiz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class starterScreenController {

    private Label registerText;
    private Label loginText;
    @FXML
    private Button registerButton;
    private Button loginButton;

    @FXML
    protected void onRegisterButtonClick() {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            Parent root = loader.load();
            Scene registerScene = new Scene(root);

            stage.setScene(registerScene);
            stage.show();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}