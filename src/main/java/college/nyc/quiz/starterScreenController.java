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

    @FXML // @FXML is used to connect the following line to the FXML file, in this case it is done to connect buttons.
    private Button registerButton;
    @FXML
    private Button loginButton;

    @FXML
    protected void onRegisterButtonClick() { // When the Register Button is clicked, we change the Scene to the register scene, by loading register.fxml
        Stage stage = (Stage) registerButton.getScene().getWindow();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml")); // loading the FXML file that we want to see
            Parent root = loader.load();
            Scene registerScene = new Scene(root); // setting the different scene on the same stage
            stage.setScene(registerScene);

        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    protected void onLoginButtonClick(){ // Same as Register Button but for Login scene
        Stage stage = (Stage) loginButton.getScene().getWindow();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Scene loginScene = new Scene(root);

            stage.setScene(loginScene);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}