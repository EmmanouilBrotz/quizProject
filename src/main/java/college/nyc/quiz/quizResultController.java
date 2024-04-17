package college.nyc.quiz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class quizResultController {
    @FXML
    private Label scoreLabel;
    private Stage stage;
    errorHandler errorHandlerObserver = new errorHandler();

    protected void setScore(int score){
        scoreLabel.setText(String.valueOf(score));
    }
    protected void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    private void returnToMainMenu(ActionEvent event) throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-menu.fxml"));
            Parent root = loader.load();
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.setScene(new Scene(root));

            Scene mainMenuScene = new Scene(root);

            stage.setScene(mainMenuScene);
        } catch (IllegalArgumentException e){
            String errorMessage = "An Error occured: " + e.getMessage();
            errorHandlerObserver.update(errorMessage);
        }


    }
}
