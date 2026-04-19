module com.example.projectopoopm05202300903 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.projectopoopm05202300903 to javafx.fxml;
    exports com.example.projectopoopm05202300903;
    exports com.example.projectopoopm05202300903.controllers;
    opens com.example.projectopoopm05202300903.controllers to javafx.fxml;
    exports com.example.projectopoopm05202300903.Exceptions;
    opens com.example.projectopoopm05202300903.Exceptions to javafx.fxml;
    exports com.example.projectopoopm05202300903.interfaces;
    opens com.example.projectopoopm05202300903.interfaces to javafx.fxml;
    exports com.example.projectopoopm05202300903.Enums;
    opens com.example.projectopoopm05202300903.Enums to javafx.fxml;
}