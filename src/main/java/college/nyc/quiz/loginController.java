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

import javax.xml.transform.Result;
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
    private String url = "jdbc:mysql://localhost:3306/accounts"; // The next 3 LoC are for connecting to the SQL database
    private String username = "root";
    private String password = "root";
    public static String sessionUsername = ""; // Will be used to later record user_id after the quiz

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
                username = retrieveUsername(usernameEmail);
                sessionUsername = username;

                FXMLLoader loader = new FXMLLoader(getClass().getResource("main-menu.fxml"));
                Parent root = loader.load();
                mainMenuController controller = loader.getController();
                Stage mainMenuStage = (Stage) loginUserButton.getScene().getWindow();
                Scene mainMenuScene = new Scene(root);
                controller.welcomeUser(username);
                mainMenuStage.setScene(mainMenuScene);

            } catch (IOException e){
                e.printStackTrace();
            }
        } else { errorLabel.setText("Invalid Username/Email or Password."); }

    }

    private String retrieveHashedPassword(String usernameEmail){
        String hashedpassword = null;
        try (Connection connection = DriverManager.getConnection(url, username, password)){ // making the connection to the database
            String sqlPasswordQuery = "SELECT password_hash FROM users WHERE username = ? OR email = ?"; // Setting the SQL query for returning the password based on username/email given
            try (PreparedStatement statement = connection.prepareStatement(sqlPasswordQuery)){
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

    private String retrieveUsername(String usernameOrEmail){
        if(isValidEmail(usernameOrEmail)){
            String email = usernameOrEmail;
            try (Connection connection = DriverManager.getConnection(url, username, password)){
                String sqlUsernameQuery = "SELECT username FROM users WHERE email = ?";
                try (PreparedStatement statement = connection.prepareStatement(sqlUsernameQuery)){
                    statement.setString(1, email);
                    try (ResultSet resultSet = statement.executeQuery()){
                        if (resultSet.next()){
                            return resultSet.getString("username");
                        }
                    }

                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        else{
            try (Connection connection = DriverManager.getConnection(url, username, password)){
                String sqlUsernameQuery = "SELECT username FROM users WHERE username = ?";
                try (PreparedStatement statement = connection.prepareStatement(sqlUsernameQuery)){
                    statement.setString(1, usernameOrEmail);
                    try (ResultSet resultSet = statement.executeQuery()){
                        if (resultSet.next()){
                            return resultSet.getString("username");
                        }
                    }

                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean isValidEmail(String email){
        return email.contains("@");
    }

}
