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

    public void welcomeUser(String username){
        welcomeLabel.setText("Welcome to Project Quiz, "+ username+"!");

    }
    @FXML
    private void onSelectionClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();

        if (event.getSource() == quizButton) {
            loader.setLocation(getClass().getResource("quiz.fxml"));
        } else if (event.getSource() == historyButton) {
            loader.setLocation(getClass().getResource("history.fxml"));
        } else if (event.getSource() == leaderboardsButton) {
            loader.setLocation(getClass().getResource("leaderboards.fxml"));
        }
        try {
            Parent root = loader.load();
            Scene quizScene = new Scene(root);
            stage.setScene(quizScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

