module project.javafx.harrypotter {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires xstream;
    requires json.simple;
    requires org.jetbrains.annotations;


    opens project.javafx to javafx.fxml;
    exports project.javafx;
}