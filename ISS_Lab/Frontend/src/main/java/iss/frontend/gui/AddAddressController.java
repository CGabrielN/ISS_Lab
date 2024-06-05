package iss.frontend.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import iss.Models.DeliveryAddress;
import iss.Models.SalesAgent;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import iss.Models.Product;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class AddAddressController {
    public TextField countryInput;
    public TextField cityInput;
    public TextField streetAndNrInput;
    public TextField buildingInput;
    public TextField suiteInput;

    private Stage stage;

    private SalesAgent salesAgent;

    private Map<Product, Integer> cart;

    private CartController cartController;

    public void setData(Stage stage, SalesAgent salesAgent, Map<Product, Integer> cart, CartController cartController) {
        this.stage = stage;
        this.salesAgent = salesAgent;
        this.cart = cart;
        this.cartController = cartController;
        this.stage.setTitle("Add Address");
        this.stage.setMaximized(false);
        this.stage.centerOnScreen();
    }

    public void handleAddAddress(ActionEvent actionEvent) {
        try{
            var country = countryInput.getText();
            var city = cityInput.getText();
            var streetAndNr = streetAndNrInput.getText();
            var street = streetAndNr.split(",")[0];
            var number = Integer.parseInt(streetAndNr.split(",")[1]);
            var building = Integer.parseInt(buildingInput.getText());
            var suite = Integer.parseInt(suiteInput.getText());
            var deliveryAddress = new DeliveryAddress(null, street, number, city, "nu", country, building, suite, salesAgent);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ObjectMapper objectMapper = new ObjectMapper();
            String deliveryAddressJson = objectMapper.writeValueAsString(deliveryAddress);
            HttpEntity<String> entity = new HttpEntity<>(deliveryAddressJson, headers);
            DeliveryAddress response = restTemplate.postForObject("http://localhost:8080/addresses/add", entity, DeliveryAddress.class);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/select-address-view.fxml"));
            this.stage.setScene(new Scene(fxmlLoader.load()));
            SelectAddressController controller = fxmlLoader.getController();
            controller.setData(stage, salesAgent, cart, cartController);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleBack(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iss/frontend/views/select-address-view.fxml"));
            this.stage.setScene(new Scene(fxmlLoader.load()));
            SelectAddressController controller = fxmlLoader.getController();
            controller.setData(stage, salesAgent, cart, cartController);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
