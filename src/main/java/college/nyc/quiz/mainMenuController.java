package college.nyc.quiz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class mainMenuController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private Button quizButton;
    @FXML
    private Button historyButton;
    @FXML
    private Button leaderboardsButton;
    @FXML
    private Button returnButton;
    errorHandler errorHandlerObserver = new errorHandler();

    public void welcomeUser(String username){
        welcomeLabel.setText("Welcome to Project Quiz, "+ username+"!");

    }
    @FXML
    private void onSelectionClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;

        if (event.getSource() == quizButton) {
            loader.setLocation(getClass().getResource("pick-category.fxml"));
            root = loader.load();
        } else if (event.getSource() == historyButton) {
            loader.setLocation(getClass().getResource("history.fxml"));
            root = loader.load();
        } else if (event.getSource() == leaderboardsButton) {
            loader.setLocation(getClass().getResource("leaderboards.fxml"));
            root = loader.load();
        }
        if (stage.getScene() == null) {
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(root); // Set the new root for the existing scene
        }
    }
    @FXML
    private void onReturnButtonClick(){
        Stage stage = (Stage) returnButton.getScene().getWindow();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("starter-screen.fxml")); // loading the FXML file that we want to see
            Parent root = loader.load();
            Scene registerScene = new Scene(root); // setting the different scene on the same stage

            stage.setScene(registerScene);

        } catch (IOException e){
            String errorMessage = "An Error occured: " + e.getMessage();
            errorHandlerObserver.update(errorMessage);
        }
    }
    }

