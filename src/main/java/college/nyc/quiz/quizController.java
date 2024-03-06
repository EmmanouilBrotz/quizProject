package college.nyc.quiz;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.commons.text.StringEscapeUtils;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

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

    private JSONArray resultsArray;
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            URL apiUrl = new URL("https://opentdb.com/api.php?amount=10&category=9"); // Open the URL with the JSON file
            InputStreamReader reader = new InputStreamReader(apiUrl.openStream()); // Create reader
            JSONParser parser = new JSONParser(); // Create parser
            JSONObject questionApi = (JSONObject) parser.parse(reader); // Read the parser into the Object
            resultsArray = (JSONArray) questionApi.get("results"); // This array contains the question, correct answer, incorrect answers and some other data

            // Display the first question
            displayQuestion(currentQuestionIndex);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        // Set action for answer buttons
        option1Button.setOnAction(event -> handleAnswer(0));
        option2Button.setOnAction(event -> handleAnswer(1));
        option3Button.setOnAction(event -> handleAnswer(2));
        option4Button.setOnAction(event -> handleAnswer(3));
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

    private void setButtonDynamicWidth(Button button, String text){
        button.setText(StringEscapeUtils.unescapeHtml4(text));

        double textWidth = new Text(text).getLayoutBounds().getWidth();
        double newWidth = textWidth + 20;

        button.setPrefWidth(newWidth);
    }


    private void handleAnswer(int selectedOptionIndex) {
        // Check if the selected option is correct
        JSONObject questionObject = (JSONObject) resultsArray.get(currentQuestionIndex);
        String correctAnswer = (String) questionObject.get("correct_answer");
        String selectedAnswer = null;
        switch (selectedOptionIndex) {
            case 0:
                selectedAnswer = option1Button.getText();
                break;
            case 1:
                selectedAnswer = option2Button.getText();
                break;
            case 2:
                selectedAnswer = option3Button.getText();
                break;
            case 3:
                selectedAnswer = option4Button.getText();
                break;
        }

        boolean isCorrect = selectedAnswer.equals(correctAnswer);
        System.out.println("Selected answer: " + selectedAnswer);
        System.out.println("Is correct: " + isCorrect);

        // Update score if the answer is correct
        if (isCorrect) {
            score++;
            System.out.println("Score: " + score);
        }

        // Move to the next question
        currentQuestionIndex++;
        if (currentQuestionIndex < resultsArray.size()) {
            displayQuestion(currentQuestionIndex);
        } else {
            // No more questions, quiz finished
            System.out.println("Quiz finished");
            System.out.println("Final score: " + score);
            // You can add your logic here for what happens when the quiz is finished, such as storing the score in a database
        }
    }
}
