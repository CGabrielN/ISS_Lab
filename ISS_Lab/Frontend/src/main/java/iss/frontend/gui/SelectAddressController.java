package iss.frontend.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import iss.Models.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectAddressController {
    public VBox addressesContainer;
    private Stage stage;

    private SalesAgent salesAgent;

    private Map<Product, Integer> cart;

    private CartController cartController;

    private List<DeliveryAddress> addresses;

    public void setData(Stage stage, SalesAgent salesAgent, Map<Product, Integer> cart, CartController cartController) {
        this.stage = stage;
        this.salesAgent = salesAgent;
        this.cart = cart;
        this.cartController = cartController;
        this.stage.setTitle("Select Address");
        this.stage.setMaximized(false);
        this.stage.centerOnScreen();
        this.stage.show();
        loadAddresses();
    }

    private void loadAddresses(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/addresses/" + salesAgent.getAccount().getCompany().getId()))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        List<DeliveryAddress> addresses = mapper.readValue(responseBody, new TypeReference<List<DeliveryAddress>>(){});
                        Platform.runLater(() -> {populateAddresses(addresses);});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void populateAddresses(List<DeliveryAddress> addresses){
        addressesContainer.getChildren().clear();
        this.addresses = addresses;
        ToggleGroup group = new ToggleGroup();
        for (DeliveryAddress address : addresses) {
            try{
                RadioButton radioButton = new RadioButton(address.toString());
                radioButton.setToggleGroup(group);
                radioButton.setWrapText(true);
                addressesContainer.getChildren().add(radioButton);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        var addButton = new Button("Add delivery address  +");
        addButton.setOnAction(event -> handleAddAddress());
        addressesContainer.getChildren().add(addButton);
    }

    private void handleAddAddress(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/add_address_view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            AddAddressController controller = fxmlLoader.getController();
            controller.setData(stage, salesAgent, cart, cartController);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleGoBack(ActionEvent actionEvent) {
        cartController.show();
        this.stage.close();
    }

    public void handlePlaceOrder(ActionEvent actionEvent) {
        RadioButton selectedRadioButton = (RadioButton) addressesContainer.getChildren().stream()
                .filter(node -> node instanceof RadioButton)
                .map(node -> (RadioButton) node)
                .filter(RadioButton::isSelected)
                .findFirst()
                .orElse(null);
        if(selectedRadioButton == null){
            System.out.println("No address selected");
            return;
        }
        String selectedAddressText = selectedRadioButton.getText();
        DeliveryAddress selectedAddress = null;

        for (DeliveryAddress address : addresses) {
            if (address.toString().equals(selectedAddressText)) {
                selectedAddress = address;
                break;
            }
        }

        if (selectedAddress != null) {

            if(!checkStockQuantities()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Stock Information");
                alert.setHeaderText(null);
                alert.setContentText("Not enough stock for one or more products");

                alert.showAndWait();
                handleGoBack(null);
                return;
            }

            double total = 0;
            for (Product product : cart.keySet()) {
                total += product.getProductPrice() * cart.get(product);
            }
            var order = new Order(null, LocalDateTime.now().plusDays(2), total + 2.0f, 2.0f, "processing" , salesAgent, selectedAddress);
            try{
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                String orderJSON = objectMapper.writeValueAsString(order);
                HttpEntity<String> entity = new HttpEntity<>(orderJSON, headers);
                Order response = restTemplate.postForObject("http://localhost:8080/orders/add", entity, Order.class);
                List<OrderItem> orderItems = new ArrayList<>();
                for (Product product : cart.keySet()) {
                    var orderItem = new OrderItem(null, cart.get(product), response, product);
                    orderItems.add(orderItem);
                }

                for (OrderItem orderItem : orderItems) {
                    String orderItemJSON = objectMapper.writeValueAsString(orderItem);
                    HttpEntity<String> orderItemEntity = new HttpEntity<>(orderItemJSON, headers);
                    restTemplate.postForObject("http://localhost:8080/orders/addItem", orderItemEntity, OrderItem.class);
                }

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/agent-view.fxml"));
                stage.setScene(new Scene(fxmlLoader.load()));
                AgentViewController controller = fxmlLoader.getController();
                controller.setData(stage, salesAgent);


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("No address selected");
        }
    }

    private boolean checkStockQuantities() {
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
                return false;
            }
        }

        return true;
    }
}
