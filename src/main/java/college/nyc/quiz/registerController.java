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
        String registeredUsername = usernameField.getText();
        String registeredEmail = emailField.getText();
        String registeredPassword = passwordField.getText();
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/accounts";
        String username = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String insertQuery = "INSERT INTO users (username, email, password_hash) VALUES (?,?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, registeredUsername);
                preparedStatement.setString(2, registeredEmail);
                // Hash and salt the password before storing it in the database
                String hashedPassword = hashAndSaltPassword(registeredPassword);
                preparedStatement.setString(3, hashedPassword);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    confirmationLabel.setText("Account Registered Successfully.");
                } else {
                    confirmationLabel.setText("Failed to register account.");
                }
            }
        } catch (SQLException e) {
           if (e.getSQLState().equals("23000")){
               confirmationLabel.setText("Cannot Register account; Same username has been used.");
           }
           else{
               e.printStackTrace();
           }
        }
    }

    private String hashAndSaltPassword(String password) {
        // Implement a secure hashing algorithm (e.g., BCrypt) to hash and salt the password
        // Return the hashed and salted password
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
