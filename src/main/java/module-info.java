module project {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires lombok;
//    requires json.simple;
    requires xstream;

    opens project.fx.controllers to javafx.fxml;
    opens project.fx.functions to javafx.fxml;
    opens project.fx to javafx.fxml;
    opens project.fx.images to javafx.fxml;
    opens project.fx.fxml to javafx.fxml;
    opens project.abstractClasses to xstream;
    opens project.classes to xstream;
    opens project.enums to xstream;


    exports project.classes;
    exports project.abstractClasses;
    exports project.enums;
    exports project.functions;
    exports project.fx.controllers;
    exports project.fx.functions;
    exports project.fx;
}
