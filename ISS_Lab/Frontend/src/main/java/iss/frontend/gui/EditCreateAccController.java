package iss.frontend.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import iss.Models.Account;
import iss.Models.Manager;
import iss.Models.SalesAgent;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;

public class EditCreateAccController {

    private SalesAgent salesAgent;
    private Manager manager;

    public TextField passwordField;
    public TextField usernameField;
    public TextField fullNameField;

    private AdminController adminController;

    private Stage stage;

    public void setStage(Stage stage, AdminController adminController) {
        this.stage = stage;
        this.adminController = adminController;
    }

    public void setManager(Manager manager){
        this.manager = manager;
    }

    public void setSalesAgent(SalesAgent salesAgent) {
        this.salesAgent = salesAgent;
        usernameField.setText(salesAgent.getAccount().getUsername());
        fullNameField.setText(salesAgent.getFullName());
        passwordField.setText(salesAgent.getAccount().getPasswordHash());
    }

    public void handleSaveChanges(ActionEvent actionEvent) {
        var username = usernameField.getText();
        var password = passwordField.getText();
        var fullName = fullNameField.getText();
        if(username.isEmpty() || password.isEmpty() || fullName.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields!");

            alert.showAndWait();
        }else{
            if(this.salesAgent == null){
                var account = new Account(null, username, password, manager.getAccount().getCompany());
                var salesAgent = new SalesAgent(null, fullName, account, manager);
                try{
                    var objectMapper = new ObjectMapper();
                    var requestBody = objectMapper.writeValueAsString(salesAgent);
                    var request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8080/manager/agents/create"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                            .build();
                    var client = HttpClient.newHttpClient();
                    var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                }catch (Exception e) {
                    e.printStackTrace();
                }
            } else{
                this.salesAgent.getAccount().setUsername(username);
                this.salesAgent.getAccount().setPasswordHash(password);
                this.salesAgent.setFullName(fullName);
                try{
                    var objectMapper = new ObjectMapper();
                    var requestBody = objectMapper.writeValueAsString(this.salesAgent);
                    var request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8080/manager/agents/update"))
                            .header("Content-Type", "application/json")
                            .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                            .build();
                    var client = HttpClient.newHttpClient();
                    var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            stage.close();
            adminController.handleFilerAgents(null);
        }
    }
}
