package college.nyc.quiz;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class errorHandler {
    private final String LOG_FILE = "error.log";
    private FileWriter logFile;
    private BufferedWriter writer;

    public errorHandler() {
        try {
            logFile = new FileWriter(LOG_FILE, true); // Append mode
            writer = new BufferedWriter(logFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(Object arg) {
        try {
            writer.write(arg.toString());
            writer.newLine();
            writer.flush(); // Ensure data is written to the file
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing log file: " + e.getMessage());
        }
    }
}



