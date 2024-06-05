package iss.frontend.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iss.Models.Product;
import iss.Models.SalesAgent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AgentViewController {

    @FXML
    private TextField nameFilterField;

    @FXML
    private VBox productsContainer;

    private Stage stage;

    private SalesAgent salesAgent;

    private Map<Product, Integer> cart;

    private List<Product> currentProducts;
    private ScheduledExecutorService executorService;

    public void setData(Stage stage, SalesAgent salesAgent){
        this.salesAgent = salesAgent;
        this.stage = stage;
        cart = new HashMap<>();
        this.stage.setTitle("Welcome " + salesAgent.getFullName());
        this.stage.setMaximized(true);
        loadProducts();
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::checkForUpdates, 10, 10, TimeUnit.SECONDS);
        this.stage.setOnCloseRequest(event -> {
            if (executorService != null) {
                executorService.shutdown();
            }
        });
    }

    public void setCart(Map<Product, Integer> cart){
        this.cart = cart;
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
                .uri(URI.create("http://localhost:8080/products/" + salesAgent.getAccount().getCompany().getId()))
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
                .uri(URI.create("http://localhost:8080/products/filtered/" + salesAgent.getAccount().getCompany().getId() + "/" + nameFilterField.getText()))
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

    private void loadProducts(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/products/" + salesAgent.getAccount().getCompany().getId()))
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

    public void populateProducts(List<Product> products){
        currentProducts = products;
        productsContainer.getChildren().clear();
        productsContainer.setSpacing(20);
        var produtsCounter = 0;
        var hbox = new HBox();
        for(var product : products){
            try{
                produtsCounter += 1;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/product-card.fxml"));
                Node productCard = fxmlLoader.load();
                ProductCardController controller = fxmlLoader.getController();
                controller.setData(product, this);
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
    }

    public void handleFilerProducts(KeyEvent keyEvent) {
        if(nameFilterField.getText().isEmpty()){
            loadProducts();
            return;
        }


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/products/filtered/" + salesAgent.getAccount().getCompany().getId() + "/" + nameFilterField.getText()))
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

    public void handleLogout(ActionEvent actionEvent) {
        try{
            stage.setMaximized(false);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/login-view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            LoginController controller = fxmlLoader.getController();
            controller.setStage(stage);
            if (executorService != null) {
                executorService.shutdown();
            }
        } catch (IOException e){
            System.out.println("Error loading login: " + e.getMessage());
        }
    }

    public void addToCart(Product product, int quantity){
        cart.put(product, quantity);
    }

    public void handleGoToCart(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/cart-view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            CartController controller = fxmlLoader.getController();
            controller.setData(stage, salesAgent, cart);
            if (executorService != null) {
                executorService.shutdown();
            }
        } catch (IOException e){
            System.out.println("Error loading cart: " + e.getMessage());
        }
    }
}
