package college.nyc.quiz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.commons.text.StringEscapeUtils;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static java.sql.Timestamp.valueOf;

public class quizController implements Initializable {

    @FXML
    private Label questionLabel;

    @FXML
    private Button option1Button;

    @FXML
    private Button option2Button;

    @FXML
    private Button option3Button;

    @FXML
    private Button option4Button;
    @FXML
    private Label scoreLabel;
    @FXML
    private Button returnButton;
    private String url = "jdbc:mysql://localhost:3306/accounts"; // The next 3 LoC are for connecting to the SQL database
    private String dbUsername = "root"; // Playing a lot with usernames here so I changed the name of the variable
    private String password = "root";

    private JSONArray resultsArray;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private Stage quizStage;
    errorHandler errorHandlerObserver = new errorHandler();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            URL apiUrl = new URL(pickCategoryController.categoryURL); // Open the URL with the JSON file
            InputStreamReader reader = new InputStreamReader(apiUrl.openStream()); // Create reader
            JSONParser parser = new JSONParser(); // Create parser
            JSONObject questionApi = (JSONObject) parser.parse(reader); // Read the parser into the Object
            resultsArray = (JSONArray) questionApi.get("results"); // This array contains the question, correct answer, incorrect answers and some other data

            // Display the first question
            displayQuestion(currentQuestionIndex);

        } catch (IOException | ParseException e) {
            String errorMessage = "An Error occured: " + e.getMessage();
            errorHandlerObserver.update(errorMessage);
        }

        // Set action for answer buttons
        setOptionButtonHandler(option1Button, 0);
        setOptionButtonHandler(option2Button, 1);
        setOptionButtonHandler(option3Button, 2);
        setOptionButtonHandler(option4Button, 3);
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

    private void setOptionButtonHandler(Button button, int index) { // Added to clean code a bit, if I don't have this the optionButtonHandlers look ugly (the 4 LoC right above)
        button.setOnAction(event -> {
            try {
                handleAnswer(index);
            } catch (SQLException e) {
                String errorMessage = "An Error occured: " + e.getMessage();
                errorHandlerObserver.update(errorMessage);
            }
        });
    }

    private void displayQuestion(int index) {
        JSONObject questionObject = (JSONObject) resultsArray.get(index);
        String question = (String) questionObject.get("question");
        question = StringEscapeUtils.unescapeHtml4(question);
        questionLabel.setText(question);

        // Check the type of question
        String questionType = (String) questionObject.get("type");
        if ("multiple".equals(questionType)) {
            // Multiple choice question
            JSONArray incorrectAnswers = (JSONArray) questionObject.get("incorrect_answers");
            String correctAnswer = (String) questionObject.get("correct_answer");

            // Create a list to hold all answers (incorrect and correct)
            List<String> answers = new ArrayList<>(incorrectAnswers);
            answers.add(correctAnswer);

            // Shuffle answers to display randomly
            Collections.shuffle(answers);

            // Set answers to buttons
            setButtonDynamicWidth(option1Button, answers.get(0));
            setButtonDynamicWidth(option2Button, answers.get(1));

            option3Button.setVisible(true);
            option4Button.setVisible(true);
            setButtonDynamicWidth(option3Button, answers.get(2));
            setButtonDynamicWidth(option4Button, answers.get(3));
        } else if ("boolean".equals(questionType)) {
            // True/false question
            option1Button.setText("True");
            option2Button.setText("False");
            option3Button.setVisible(false);
            option4Button.setVisible(false);
        }
    }

    private void setButtonDynamicWidth(Button button, String text) {
        button.setText(StringEscapeUtils.unescapeHtml4(text));

        double textWidth = new Text(text).getLayoutBounds().getWidth();
        double newWidth = textWidth + 20;

        button.setPrefWidth(newWidth);
    }


    private void handleAnswer(int selectedOptionIndex) throws SQLException {
        // Check if the selected option is correct
        JSONObject questionObject = (JSONObject) resultsArray.get(currentQuestionIndex);
        String correctAnswer = (String) questionObject.get("correct_answer");
        String selectedAnswer = switch (selectedOptionIndex) {
            case 0 -> option1Button.getText();
            case 1 -> option2Button.getText();
            case 2 -> option3Button.getText();
            case 3 -> option4Button.getText();
            default -> null;
        };

        boolean isCorrect = StringEscapeUtils.unescapeHtml4(selectedAnswer).equals(StringEscapeUtils.unescapeHtml4(correctAnswer));

        // Update score if the answer is correct
        if (isCorrect) {
            score++;
            scoreLabel.setText("SCORE: " + score);
        }

        // Move to the next question
        currentQuestionIndex++;
        if (currentQuestionIndex < resultsArray.size()) {
            displayQuestion(currentQuestionIndex);
        } else {
            // No more questions, quiz finished, logging it on database
            insertIntoDatabase();
            showQuizResult(score);
        }
    }

    public int retrieveUserId(String username) {
        try (Connection connection = DriverManager.getConnection(url, dbUsername, password)) {
            String selectQuery = "SELECT user_id FROM users WHERE username = ?";  // Corrected SQL query, removed single quotes around '?'
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("user_id");  // Retrieve the user_id from the result set
                    }
                }
            }
        } catch (SQLException e) {
            String errorMessage = "An Error occured: " + e.getMessage();
            errorHandlerObserver.update(errorMessage);
        }
        return 0; // Can't find User_id
    }

    private void insertIntoDatabase() throws SQLException {
        String storedUsername = loginController.sessionUsername;
        LocalDateTime completionDatetime = LocalDateTime.now();
        try (Connection connection = DriverManager.getConnection(url, dbUsername, password)) { // Here we make the connection and insert the data to the database
            String insertQuery = "INSERT INTO games (user_id, score, completion_time) VALUES (?,?,?)"; // SQL Query to insert into games table
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, retrieveUserId(storedUsername));
                preparedStatement.setInt(2, score);
                preparedStatement.setTimestamp(3, valueOf(completionDatetime));

                preparedStatement.executeUpdate(); // runs the SQL query

            } catch (SQLException e) {
                String errorMessage = "An Error occured: " + e.getMessage();
                errorHandlerObserver.update(errorMessage);
            }
        }

    }

    private void showQuizResult(int score) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz-result.fxml"));
            Parent root = loader.load();
            quizResultController popupController = loader.getController();
            popupController.setScore(score);

            quizStage.getScene().setRoot(root);

        } catch (IOException e) {
            String errorMessage = "An Error occured: " + e.getMessage();
            errorHandlerObserver.update(errorMessage);
        }
    }
    public void setStage(Stage stage){
        this.quizStage = stage;
    }
}