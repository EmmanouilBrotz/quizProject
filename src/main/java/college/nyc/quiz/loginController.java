package college.nyc.quiz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.*;

public class loginController {
    @FXML
    private Button returnButton;
    @FXML
    private TextField usernameEmailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginUserButton;
    @FXML
    private void onReturnButtonClick(){
        Stage stage = (Stage) returnButton.getScene().getWindow();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("starter-screen.fxml")); // loading the FXML file that we want to see
            Parent root = loader.load();
            Scene registerScene = new Scene(root); // setting the different scene on the same stage

            stage.setScene(registerScene);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void loginUser(){
        String usernameEmail = usernameEmailField.getText();
        String password = passwordField.getText();

        String storedHashedPassword = retrieveHashedPassword(usernameEmail) ; //Holding the hashed password that will be retrieved from the database, to compare with the password given

        if (storedHashedPassword != null && BCrypt.checkpw(password, storedHashedPassword)){ // Comparing the two passwords
            try {
                Stage stage = (Stage) loginUserButton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("main-menu.fxml")); //
                Parent root = loader.load();
                Scene mainMenuScene = new Scene(root);

                stage.setScene(mainMenuScene);

            } catch (IOException e){
                e.printStackTrace();
            }
        } else { errorLabel.setText("Invalid Username/Email or Password."); }

    }

    private String retrieveHashedPassword(String usernameEmail){
        String hashedpassword = null;
        String url = "jdbc:mysql://localhost:3306/accounts"; // The next 3 LoC are for connecting to the SQL database
        String username = "root";
        String password = "root";
        try (Connection connection = DriverManager.getConnection(url, username, password);){ // making the connection to the database
            String sqlQuery = "SELECT password_hash FROM users WHERE username = ? OR email = ?"; // Setting the SQL query for returning the password based on username/email given
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)){
                statement.setString(1, usernameEmail);
                statement.setString(2, usernameEmail);
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()){
                        hashedpassword = resultSet.getString("password_hash");
                    }
                }
            }


        } catch (SQLException e){
            e.printStackTrace();
        }
        return hashedpassword;
    }

}
