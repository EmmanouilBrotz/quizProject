package college.nyc.quiz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class projectApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(projectApplication.class.getResource("starter-screen.fxml")); // FXML Loader is used to load the FXML file. FXML files look a lot like HTML files.
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720); // Sets the Scene, which includes the contents of what we're going to see. The Scene runs the FXML file
        stage.setTitle("Project: Quiz!"); // Title of application
        stage.setScene(scene); // Stage is the main window.
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}