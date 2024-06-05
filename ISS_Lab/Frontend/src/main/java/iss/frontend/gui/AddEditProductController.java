package iss.frontend.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import iss.Models.DeliveryAddress;
import iss.Models.Manager;
import iss.Models.Product;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class AddEditProductController {
    public Label windowLabel;
    public TextField pathField;
    public TextField nameField;
    public TextField priceField;
    public TextField quantityField;
    public TextArea descriptionField;

    private Product product;
    private Stage stage;
    private Manager manager;
    private AdminController adminController;

    public void setData(Product product, Stage stage, Manager manager, AdminController adminController){
        this.product = product;
        this.stage = stage;
        this.manager = manager;
        this.adminController = adminController;
        if (product != null) {
            windowLabel.setText("Edit Product");
            pathField.setText(product.getProductImage());
            nameField.setText(product.getProductName());
            priceField.setText(String.valueOf(product.getProductPrice()));
            quantityField.setText(String.valueOf(product.getProductQuantity()));
            descriptionField.setText(product.getProductDescription());
        } else {
            windowLabel.setText("Add Product");
        }
    }

    public void handleSave(ActionEvent actionEvent) {
        var imagePath = pathField.getText();
        var name = nameField.getText();
        var price = Double.parseDouble(priceField.getText());
        var quantity = Integer.parseInt(quantityField.getText());
        var description = descriptionField.getText();
        product = new Product(product != null ? product.getId(): null, name, description, price, quantity, imagePath, manager.getAccount().getCompany());
        try{
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ObjectMapper objectMapper = new ObjectMapper();
            String deliveryAddressJson = objectMapper.writeValueAsString(product);
            HttpEntity<String> entity = new HttpEntity<>(deliveryAddressJson, headers);
            Product response = restTemplate.postForObject("http://localhost:8080/products/add", entity, Product.class);

            adminController.loadProducts();
            stage.close();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleClose(ActionEvent actionEvent) {
        stage.close();
    }
}
