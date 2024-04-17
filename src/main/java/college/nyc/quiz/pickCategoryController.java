package college.nyc.quiz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class pickCategoryController {
    public static String categoryURL;
    @FXML
    private Button multipleChoiceButton;
    @FXML
    private Button trueFalseButton;
    @FXML
    private Button anyButton;
    @FXML
    private Button returnButton;
    errorHandler errorHandlerObserver = new errorHandler();

    @FXML
    private void categorySelection(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();
        if(clickedButton.getId().equals("multipleChoiceButton")){
            categoryURL = "https://opentdb.com/api.php?amount=10&category=9&type=multiple";
        } else if (clickedButton.getId().equals("trueFalseButton")) {
            categoryURL = "https://opentdb.com/api.php?amount=10&category=9&type=boolean";
        } else if(clickedButton.getId().equals("anyButton")){
            categoryURL = "https://opentdb.com/api.php?amount=10&category=9";
        }
        // Once category is chosen, proceed to the Quiz by setting scene to it
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz.fxml"));
        Parent root = loader.load();
        quizController quizController = loader.getController();
        quizController.setStage(stage);
        Scene quizScene = new Scene(root);
        stage.setScene(quizScene);

    }

    @FXML
    private void returnToMainMenu(){
        Stage stage = (Stage) returnButton.getScene().getWindow();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-menu.fxml")); // loading the FXML file that we want to see
            Parent root = loader.load();
            Scene registerScene = new Scene(root); // setting the different scene on the same stage

            stage.setScene(registerScene);

        } catch (IOException e){
            String errorMessage = "An Error occured: " + e.getMessage();
            errorHandlerObserver.update(errorMessage);
        }
    }


}
