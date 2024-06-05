package iss.frontend.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iss.Models.AccountsDTO;
import iss.Models.Manager;
import iss.Models.Product;
import iss.Models.SalesAgent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class AdminController {

    public TextField usernameFilterField;
    public VBox productsContainer;
    public ScrollPane scrollPane;
    public TextField nameFilterField;
    @FXML
    private VBox agentsContainer;

    @FXML
    private TabPane tabPane;

    private Stage stage;

    private Manager manager;

    private ScheduledExecutorService executorService;
    private List<Product> currentProducts;

    public void setStage(Stage stage) {
        scrollPane.setFitToHeight(false);
        this.stage = stage;
        this.stage.setTitle("Admin");
        this.stage.setMaximized(true);
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::checkForUpdates, 10, 10, TimeUnit.SECONDS);
        this.stage.setOnCloseRequest(event -> {
            if (executorService != null) {
                executorService.shutdown();
            }
        });
    }

    public void setManager(Manager manager){
        this.manager = manager;
//        changeTab();
        loadTable();
        loadProducts();
    }

    private void checkForUpdates() {
        if(nameFilterField.getText().isEmpty()){
            List<Product> newProducts = getProductsFromServer();
            if (!newProducts.equals(currentProducts)) {
                Platform.runLater(() -> populateProducts(newProducts));
            }
        } else{
            List<Product> newProducts = getFilteredProductsFromServer();
            if (!newProducts.equals(currentProducts)) {
                Platform.runLater(() -> populateProducts(newProducts));
            }
        }
    }

    private List<Product> getProductsFromServer(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/products/" + manager.getAccount().getCompany().getId()))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(responseBody -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        return mapper.readValue(responseBody, new TypeReference<List<Product>>(){});
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).join();
    }

    private List<Product> getFilteredProductsFromServer(){
        if(nameFilterField.getText().isEmpty()){
            loadProducts();
            return null;
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/products/filtered/" + manager.getAccount().getCompany().getId() + "/" + nameFilterField.getText()))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(responseBody -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        return mapper.readValue(responseBody, new TypeReference<List<Product>>(){});
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).join();
    }



    public void loadProducts(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/products/" + manager.getAccount().getCompany().getId()))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        List<Product> products = mapper.readValue(responseBody, new TypeReference<List<Product>>(){});
                        Platform.runLater(() -> {populateProducts(products);});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void populateProducts(List<Product> products){
        this.currentProducts = products;
        productsContainer.getChildren().clear();
        productsContainer.setSpacing(20);
        var produtsCounter = 0;
        var hbox = new HBox();
        for(var product : products){
            try{
                produtsCounter += 1;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/product-admin-card.fxml"));
                Node productCard = fxmlLoader.load();
                ProductAdminCardController controller = fxmlLoader.getController();
                controller.setData(product,this, manager);
                hbox.getChildren().add(productCard);
                hbox.setSpacing(20);
                if(produtsCounter == 4){
                    productsContainer.getChildren().add(hbox);
                    hbox = new HBox();
                    produtsCounter = 0;
                }
            } catch (IOException e){
                System.out.println("Error loading products: " + e.getMessage());
            }
        }
        if(!hbox.getChildren().isEmpty())
            productsContainer.getChildren().add(hbox);
        productsContainer.requestLayout();
    }

    private void loadTable(){
        sendRequest("http://localhost:8080/manager/agents/" + manager.getId());
    }

    private void populateTable(List<SalesAgent> agents){
        agentsContainer.getChildren().clear();
        for(var agent : agents){
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/user_item-view.fxml"));
                Node userItem = fxmlLoader.load();
                UserItemController controller = fxmlLoader.getController();
                controller.setData(agent, this.manager, this);
                agentsContainer.getChildren().add(userItem);
            } catch (IOException e){
                System.out.println("Error loading agents: " + e.getMessage());
            }
        }
    }

    private void changeTab(){
        tabPane.getSelectionModel().select(1);
    }

    public void handleLogout(ActionEvent actionEvent) {
        try{
            stage.setMaximized(false);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/login-view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            LoginController controller = fxmlLoader.getController();
            controller.setStage(stage);

            if(executorService != null){
                executorService.shutdown();
            }
        } catch (IOException e){
            System.out.println("Error loading login: " + e.getMessage());
        }
    }

    public void handleCreateAccount(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/edit-create-acc-view.fxml"));
            var stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Create Account");
            EditCreateAccController controller = fxmlLoader.getController();
            controller.setStage(stage, this);
            controller.setManager(manager);
            stage.show();

        } catch (IOException e) {
            System.out.println("Error loading create account: " + e.getMessage());
        }
    }

    public void sendRequest(String url){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        List<SalesAgent> agents = mapper.readValue(responseBody, new TypeReference<List<SalesAgent>>(){});
                        Platform.runLater(() -> {populateTable(agents);});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void handleFilerAgents(KeyEvent keyEvent) {
        var username = usernameFilterField.getText();
        if(username.isEmpty())
            sendRequest("http://localhost:8080/manager/agents/" + manager.getId());
        else
             sendRequest("http://localhost:8080/manager/agentsFiltered/" + username + "/" + manager.getId());
    }

    public void handleAddItem(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iss/frontend/views/add-edit-product.fxml"));
            var stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            AddEditProductController controller = loader.getController();
            controller.setData(null, stage, manager, this);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void handleFilterProducts(KeyEvent event) {
        if(nameFilterField.getText().isEmpty()){
            loadProducts();
            return;
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/products/filtered/" + manager.getAccount().getCompany().getId() + "/" + nameFilterField.getText()))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        List<Product> products = mapper.readValue(responseBody, new TypeReference<List<Product>>(){});
                        Platform.runLater(() -> {populateProducts(products);});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
