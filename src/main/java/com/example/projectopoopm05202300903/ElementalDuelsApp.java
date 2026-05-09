package com.example.projectopoopm05202300903;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

public class ElementalDuelsApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                ElementalDuelsApp.class.getResource("/com/example/projectopoopm05202300903/views/main-menu.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F11) {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(!stage.isFullScreen());
            }
            else if (event.getCode() == KeyCode.ESCAPE) {
                if (stage.isFullScreen()) {
                    stage.setFullScreen(false);
                }
            }
        });

        stage.setTitle("Elemental Duels - Menu");
        stage.setScene(scene);
        stage.show();
    }
}
