package iss.frontend.gui;

import iss.Models.Product;
import iss.Models.SalesAgent;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class CartController {
    public Label orderTotalLabel;
    public Label orderAndTaxLabel;
    public Label itemsCountLabel;
    public VBox productsContainer;

    private Stage stage;
    private SalesAgent salesAgent;
    private Map<Product, Integer> cart;

    public void setData(Stage stage, SalesAgent salesAgent, Map<Product, Integer> cart){
        productsContainer.setSpacing(10);
        this.salesAgent = salesAgent;
        this.stage = stage;
        this.cart = cart;
        this.stage.setTitle("Cart");
        itemsCountLabel.setText(cart.keySet().size() + " items");
        loadCart();
    }

    private void loadCart(){
        productsContainer.getChildren().clear();
        for (Product product : cart.keySet()) {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/iss/frontend/views/cart-item-view.fxml"));
                Node cartItem = loader.load();
                CartItemController controller = loader.getController();
                controller.setData(product, this, cart.get(product));
                productsContainer.getChildren().add(cartItem);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        calculateTotal();
    }

    public void handleGoToDelivery(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/select-address-view.fxml"));
            var deliveryStage = new Stage();
            deliveryStage.setScene(new Scene(fxmlLoader.load()));
            SelectAddressController controller = fxmlLoader.getController();
            controller.setData(deliveryStage, salesAgent, cart, this);
            stage.hide();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void handleGoBack(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/agent-view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            AgentViewController controller = fxmlLoader.getController();
            controller.setData(stage, salesAgent);
            controller.setCart(cart);
        } catch (IOException e) {
            System.out.println("Error loading agent-view.fxml " + e.getMessage());
        }
    }

    private void calculateTotal(){
        double total = 0;
        for (Product product : cart.keySet()) {
            total += product.getProductPrice() * cart.get(product);
        }
        float tax = 2.0f;
        orderTotalLabel.setText("$" + total);
        orderAndTaxLabel.setText("$" + (total + tax));
    }

    public void updateCart(Product product, int quantity){
        cart.put(product, quantity);
        itemsCountLabel.setText(cart.keySet().size() + " items");
        calculateTotal();
    }

    public void show(){
        checkAndUpdateCartQuantities();
        stage.show();
        loadCart();
    }
    private void checkAndUpdateCartQuantities() {
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantityInCart = entry.getValue();

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/products/quantity/" + product.getId()))
                    .build();

            String responseBody = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .join();

            int stockQuantity = Integer.parseInt(responseBody);

            if (quantityInCart > stockQuantity) {
                cart.keySet().remove(product);
                product.setProductQuantity(stockQuantity);
                cart.put(product, stockQuantity);
            }
        }
    }

}
