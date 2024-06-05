package iss.frontend;

import iss.frontend.gui.AdminController;
import iss.frontend.gui.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
        stage.show();
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/admin-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
////        LoginController controller = fxmlLoader.getController();
////        controller.setStage(stage);
//        stage.setScene(scene);
//        stage.setMaximized(true);
//        stage.show();
//        stage.setY(0);
//        stage.setX(0);
    }

    public static void main(String[] args) {
        launch();
    }
}