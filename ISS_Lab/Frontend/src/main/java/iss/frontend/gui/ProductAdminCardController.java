package iss.frontend.gui;

import iss.Models.Manager;
import iss.Models.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ProductAdminCardController {

    public Label nameLabel;
    public Label priceLabel;
    public Label quantityLabel;
    public ImageView imageview;
    private Product product;
    private AdminController adminController;
    private Manager manager;

    public void setData(Product product, AdminController adminController, Manager manager) {
        this.product = product;
        this.manager = manager;
        this.adminController = adminController;
        nameLabel.setText(product.getProductName());
        priceLabel.setText("$" + product.getProductPrice());
        quantityLabel.setText("Available quantity: " + product.getProductQuantity());
        imageview.setImage(new Image(product.getProductImage()));
    }

    public void handleEdit(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iss/frontend/views/add-edit-product.fxml"));
            var stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            AddEditProductController controller = loader.getController();
            controller.setData(product, stage, manager, adminController);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void handleDelete(ActionEvent actionEvent) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/products/delete/" + product.getId()))
                .DELETE()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    adminController.loadProducts();
                });
    }
}
