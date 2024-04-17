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

public class leaderboardsController implements Initializable {
    @FXML
    private Button returnButton;
    @FXML
    private TableView<historyController.GameData> leaderboardsTableView;
    @FXML
    private TableColumn<String, Integer> gameIDColumn;
    @FXML
    private TableColumn<String, String> userColumn;
    @FXML
    private TableColumn<String, Integer> scoreColumn;
    @FXML
    private TableColumn<String, String> completionDateColumn;
    ArrayList<Integer> gameIds = new ArrayList<>();
    ArrayList<String> users = new ArrayList<>();
    ArrayList<Integer> scores = new ArrayList<>();
    ArrayList<String> completionDates = new ArrayList<>();
    errorHandler errorHandlerObserver = new errorHandler();

@Override
public void initialize(URL url, ResourceBundle resourceBundle) {
    pullLeaderboardData();
    fillLeaderboards();

}

    protected void pullLeaderboardData() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/accounts", "root", "root")) {
            String query = "SELECT games.game_id, users.username, games.score, games.completion_time FROM games " +
                    "JOIN users ON games.user_id = users.user_id " +
                    "ORDER BY games.score DESC " +
                    "LIMIT 10";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet rs = preparedStatement.executeQuery();

                while(rs.next()){
                    gameIds.add(rs.getInt("game_id"));
                    users.add(rs.getString("username"));
                    scores.add(rs.getInt("score"));
                    completionDates.add(rs.getString("completion_time"));
                }
            }
        } catch (SQLException e) {
            String errorMessage = "An Error occured: " + e.getMessage();
            errorHandlerObserver.update(errorMessage);
        }
    }

    private void fillLeaderboards(){

        ObservableList<Integer> column1Data = FXCollections.observableArrayList(gameIds);
        ObservableList<String> column2Data = FXCollections.observableArrayList(users);
        ObservableList<Integer> column3Data = FXCollections.observableArrayList(scores);
        ObservableList<String> column4Data = FXCollections.observableArrayList(completionDates);

        // Create a list of GameData instances by combining the data from the three lists
        List<historyController.GameData> gameDataList = new ArrayList<>();
        int maxSize = Math.min(gameIds.size(), Math.min(scores.size(), completionDates.size()));
        for (int i = 0; i < maxSize; i++) {
            int gameId = gameIds.get(i);
            int score = scores.get(i);
            String completionDate = completionDates.get(i);
            String user = users.get(i);
            gameDataList.add(new historyController.GameData(gameId, score, completionDate, user));
        }

        // Create an observable list of GameData instances
        ObservableList<historyController.GameData> gameData = FXCollections.observableArrayList(gameDataList);

// Set the items in the TableView to display the combined data
        leaderboardsTableView.setItems(gameData);

// Set the cell value factories for each column to extract the data from the GameData instances
        gameIDColumn.setCellValueFactory(new PropertyValueFactory<>("gameId"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        completionDateColumn.setCellValueFactory(new PropertyValueFactory<>("completionDate"));

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

}
