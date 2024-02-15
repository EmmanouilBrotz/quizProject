module college.nyc.quiz {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens college.nyc.quiz to javafx.fxml;
    exports college.nyc.quiz;
}