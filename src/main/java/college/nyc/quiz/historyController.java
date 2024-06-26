package college.nyc.quiz;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static college.nyc.quiz.loginController.sessionUsername;

public class historyController implements Initializable {
    @FXML
    private TableView<GameData> gameTableView;
    @FXML
    private TableColumn<String, Integer> gameIDColumn;
    @FXML
    private TableColumn<String, Integer> scoreColumn;
    @FXML
    private TableColumn<String, String> completionDateColumn;
    @FXML
    private Button returnButton;
    ArrayList<Integer> gameIds = new ArrayList<>();
    ArrayList<Integer> scores = new ArrayList<>();
    ArrayList<String> completionDates = new ArrayList<>();
    errorHandler errorHandlerObserver = new errorHandler();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        quizController quizCont = new quizController();
        pullGameData(quizCont.retrieveUserId(sessionUsername));
        inputDataIntoColumns();
    }

    @FXML
    private void onReturnButtonClick(){
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




    protected void pullGameData(int userId){

        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/accounts", "root", "root")){
            String query = "SELECT game_id, score, completion_time FROM games WHERE user_id = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setInt(1, userId);
                ResultSet rs = preparedStatement.executeQuery();

                while(rs.next()){
                    gameIds.add(rs.getInt("game_id"));
                    scores.add(rs.getInt("score"));
                    completionDates.add(rs.getString("completion_time"));
                }
            }


        } catch (SQLException e) {
            String errorMessage = "An Error occured: " + e.getMessage();
            errorHandlerObserver.update(errorMessage);
        }

    }

    private void inputDataIntoColumns(){

        ObservableList<Integer> column1Data = FXCollections.observableArrayList(gameIds);
        ObservableList<Integer> column2Data = FXCollections.observableArrayList(scores);
        ObservableList<String> column3Data = FXCollections.observableArrayList(completionDates);

// Create a list of GameData instances by combining the data from the three lists
        List<GameData> gameDataList = new ArrayList<>();
        int maxSize = Math.min(gameIds.size(), Math.min(scores.size(), completionDates.size()));
        for (int i = 0; i < maxSize; i++) {
            int gameId = gameIds.get(i);
            int score = scores.get(i);
            String completionDate = completionDates.get(i);
            gameDataList.add(new GameData(gameId, score, completionDate));
        }

// Create an observable list of GameData instances
        ObservableList<GameData> gameData = FXCollections.observableArrayList(gameDataList);

// Set the items in the TableView to display the combined data
        gameTableView.setItems(gameData);

// Set the cell value factories for each column to extract the data from the GameData instances
        gameIDColumn.setCellValueFactory(new PropertyValueFactory<>("gameId"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        completionDateColumn.setCellValueFactory(new PropertyValueFactory<>("completionDate"));
    }

    // Define a custom data model to represent the combined data
    protected static class GameData {
        private final int gameId;
        private final int score;
        private final String completionDate;
        private final String user;

        public GameData(int gameId, int score, String completionDate) {
            this.gameId = gameId;
            this.score = score;
            this.completionDate = completionDate;
            this.user = null;
        }
        public GameData(int gameId, int score, String completionDate, String username){
            this.gameId = gameId;
            this.score = score;
            this.completionDate = completionDate;
            this.user = username;
        }

        public int getGameId() {
            return gameId;
        }

        public int getScore() {
            return score;
        }

        public String getCompletionDate() {
            return completionDate;
        }
        public String getUser(){
            return user;
        }
    }



}

