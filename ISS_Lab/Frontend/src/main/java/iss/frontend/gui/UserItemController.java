package iss.frontend.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iss.Models.Manager;
import iss.Models.SalesAgent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class UserItemController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label passwordLabel;

    private SalesAgent agent;

    private Manager manager;

    private AdminController adminController;

    public void setData(SalesAgent salesAgent, Manager manager, AdminController adminController){
        this.agent = salesAgent;
        this.manager = manager;
        this.adminController = adminController;
        setLabels(agent.getAccount().getUsername(), agent.getFullName(), agent.getAccount().getPasswordHash());
    }

    private void setLabels(String username, String name, String password) {
        usernameLabel.setText(username);
        nameLabel.setText(name);
        passwordLabel.setText(password);
    }

    public void handleEdit(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/edit-create-acc-view.fxml"));
            var stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Create Account");
            EditCreateAccController controller = fxmlLoader.getController();
            controller.setStage(stage, adminController);
            controller.setManager(manager);
            controller.setSalesAgent(agent);
            stage.show();

        } catch (IOException e) {
            System.out.println("Error loading create account: " + e.getMessage());
        }
    }

    public void handleDelete(ActionEvent actionEvent) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/manager/agents/delete/" + agent.getId()))
                .DELETE()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(responseBody -> {
                    Platform.runLater(() -> {
                        adminController.handleFilerAgents(null);
                    });
                });
    }
}
