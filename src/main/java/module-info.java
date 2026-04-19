module com.example.projectopoopm05202300903 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.projectopoopm05202300903 to javafx.fxml;
    exports com.example.projectopoopm05202300903;
    exports com.example.projectopoopm05202300903.controllers;
    opens com.example.projectopoopm05202300903.controllers to javafx.fxml;
    exports com.example.projectopoopm05202300903.models.Exceptions;
    opens com.example.projectopoopm05202300903.models.Exceptions to javafx.fxml;
    exports com.example.projectopoopm05202300903.models.interfaces;
    opens com.example.projectopoopm05202300903.models.interfaces to javafx.fxml;
    exports com.example.projectopoopm05202300903.models.Enums;
    opens com.example.projectopoopm05202300903.models.Enums to javafx.fxml;
}