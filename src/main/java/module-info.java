module college.nyc.quiz {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jbcrypt;
    requires mysql.connector.j;

    opens college.nyc.quiz to javafx.fxml;
    exports college.nyc.quiz;
}