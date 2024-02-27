package college.nyc.quiz;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class registerController {
    @FXML
    private Button registerUserButton;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label confirmationLabel;

    @FXML
    protected void registerUser() throws ClassNotFoundException {
        String registeredUsername = usernameField.getText(); // The next 3 LoC are for getting the details that the user will be inputting.
        String registeredEmail = emailField.getText();
        String registeredPassword = passwordField.getText();
        Class.forName("com.mysql.cj.jdbc.Driver"); // Checking connection for the driver for SQL
        String url = "jdbc:mysql://localhost:3306/accounts"; // The next 3 LoC are for connecting to the SQL database
        String username = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, username, password)) { // Here we make the connection and insert the data to the database
            String insertQuery = "INSERT INTO users (username, email, password_hash) VALUES (?,?,?)"; // SQL Query to insert into the users table
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, registeredUsername);
                preparedStatement.setString(2, registeredEmail);
                // Hash and salt the password before storing it in the database
                String hashedPassword = hashAndSaltPassword(registeredPassword); // Hashing the password through the function called hashAndSaltPassword which is defined below
                preparedStatement.setString(3, hashedPassword);

                int rowsAffected = preparedStatement.executeUpdate(); // runs the SQL query
                if (rowsAffected > 0) { // Once the query is ran, it checks for how many rows changed, and if it's more than 0, that means the account has been registered.
                    confirmationLabel.setText("Account Registered Successfully.");
                } else {
                    confirmationLabel.setText("Failed to register account."); // I kinda had to add an else statement but I don't think we will ever reach this point.
                }
            }
        } catch (SQLException e) { // If the username has already been registered, return a label that gives them that error
           if (e.getSQLState().equals("23000")){
               confirmationLabel.setText("Cannot Register account; Same username has been used.");
           }
           else{
               e.printStackTrace();
           }
        }
    }

    private String hashAndSaltPassword(String password) {  // Return the hashed and salted password
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
