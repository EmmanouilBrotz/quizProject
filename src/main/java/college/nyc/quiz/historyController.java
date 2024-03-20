package college.nyc.quiz;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static college.nyc.quiz.loginController.sessionUsername;

public class historyController implements Initializable {
    @FXML
    private TableView<String> gameTableView;
    @FXML
    private TableColumn<String, Integer> gameIDColumn;
    @FXML
    private TableColumn<String, Integer> scoreColumn;
    @FXML
    private TableColumn<String, String> completionDateColumn;
    ArrayList<Integer> gameIds = new ArrayList<>();
    ArrayList<Integer> scores = new ArrayList<>();
    ArrayList<String> completionDates = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        quizController quizCont = new quizController();
        pullGameData(quizCont.retrieveUserId(sessionUsername));
        inputDataIntoColumns();
    }


    public static class Game {
        private int game_id;
        private int user_id;
        private int score;
        private String completionDate;


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
            throw new RuntimeException(e);
        }

    }

    private void inputDataIntoColumns(){
        if(gameTableView != null){
            gameTableView.getColumns().clear();
        }
        ObservableList<Integer> column1Data = FXCollections.observableArrayList(gameIds);
        ObservableList<Integer> column2Data = FXCollections.observableArrayList(scores);
        ObservableList<String> column3Data = FXCollections.observableArrayList(completionDates);

        gameIDColumn.setCellValueFactory(cellData -> {
            int rowIndex = cellData.getTableView().getItems().indexOf(cellData.getValue());
            if (rowIndex >= 0 && rowIndex < column1Data.size()) {
                return new SimpleIntegerProperty(column1Data.get(rowIndex)).asObject();
            } else {
                return new SimpleIntegerProperty().asObject();
            }
        });

    }



}

