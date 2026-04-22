module com.example.projectopoopm05202300903 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.projectopoopm05202300903 to javafx.fxml;
    opens com.example.projectopoopm05202300903.controllers to javafx.fxml;
    exports com.example.projectopoopm05202300903;
    exports com.example.projectopoopm05202300903.controllers;
    exports com.example.projectopoopm05202300903.models;
    exports com.example.projectopoopm05202300903.models.card;
    exports com.example.projectopoopm05202300903.models.player;
    exports com.example.projectopoopm05202300903.models.enums;
    exports com.example.projectopoopm05202300903.models.exceptions;
    exports com.example.projectopoopm05202300903.models.interfaces;
    exports com.example.projectopoopm05202300903.views;
}