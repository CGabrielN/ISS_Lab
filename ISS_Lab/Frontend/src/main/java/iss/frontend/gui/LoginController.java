package iss.frontend.gui;

import iss.Models.AccountsDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.atomic.AtomicReference;

public class LoginController {

    public TextField usernameField;
    public PasswordField passwordField;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Login");
    }


    public void handleLogin(ActionEvent actionEvent) {
        var username = usernameField.getText();
        var password = passwordField.getText();

        AtomicReference<AccountsDTO> account = new AtomicReference<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/login/" + username + "/" + password))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    if (responseBody == null || responseBody.isEmpty()) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Login Failed");
                            alert.setHeaderText(null);
                            alert.setContentText("Invalid credentials. Please try again.");
                            alert.showAndWait();
                        });
                    } else {
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            account.set(mapper.readValue(responseBody, AccountsDTO.class));
                            Platform.runLater(() -> {
                                if (account.get().getRole().equals("Manager")) {
                                    var manager = account.get().toManager();
                                    try {
                                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/admin-view.fxml"));
                                        stage.setScene(new Scene(fxmlLoader.load()));
                                        AdminController controller = fxmlLoader.getController();
                                        controller.setStage(stage);
                                        controller.setManager(manager);
                                    } catch (IOException e) {
                                        System.out.println("Error loading admin-view.fxml " + e.getMessage());
                                    }
                                } else {
                                    var salesAgent = account.get().toSalesAgent();
                                    try {
                                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/agent-view.fxml"));
                                        stage.setScene(new Scene(fxmlLoader.load()));
                                        AgentViewController controller = fxmlLoader.getController();
                                        controller.setData(stage, salesAgent);
                                    } catch (IOException e) {
                                        System.out.println("Error loading agent-view.fxml " + e.getMessage());
                                    }
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .join();
    }
}