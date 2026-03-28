module com.example.projectopoopm05202300903 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.projectopoopm05202300903 to javafx.fxml;
    exports com.example.projectopoopm05202300903;
}