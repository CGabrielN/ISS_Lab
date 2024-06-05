package iss.frontend.gui;

import iss.Models.Product;
import iss.Models.SalesAgent;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.Map;

public class ProductCardController {
    public Label productName;
    public Label productPrice;
    public Label productQuantity;

    public ImageView imageView;
    public TextField quantityTextField;

    private Product product;

    private AgentViewController parent;

    public void setData( Product product, AgentViewController parent){
        this.product = product;
        this.parent = parent;
        changeProduct();
    }

    private void changeProduct() {
        productName.setText(product.getProductName());
        productPrice.setText("$" + (Double)(product.getProductPrice()));
        productQuantity.setText("Available quantity: " + (Integer)(product.getProductQuantity()));
        imageView.setImage(new Image(product.getProductImage()));
    }


    public void handleDecreaseQuantity(ActionEvent actionEvent) {
        var quantity = Integer.parseInt(quantityTextField.getText());
        quantity -= quantity >= 1 ? 1 : 0;
        quantityTextField.setText(String.valueOf(quantity));
    }

    public void handleIncreaseQuantity(ActionEvent actionEvent) {
        var quantity = Integer.parseInt(quantityTextField.getText());
        quantity += quantity < product.getProductQuantity() ? 1 : 0;
        quantityTextField.setText(String.valueOf(quantity));
    }

    public void handleAddToCart(ActionEvent actionEvent) {
        var quantity = Integer.parseInt(quantityTextField.getText());
        if(quantity > 0){
            parent.addToCart(product, quantity);
            quantityTextField.setText("0");
        }
    }

    public void handleQuantityInput(KeyEvent event) {
        if(quantityTextField.getText().isEmpty() || !quantityTextField.getText().matches("[0-9]+")){
            quantityTextField.setText("0");
            return;
        }
        var quantity = Integer.parseInt(quantityTextField.getText());

        if(quantity < 0){
            quantityTextField.setText("0");
        } else if(quantity > product.getProductQuantity()){
            quantityTextField.setText(String.valueOf(product.getProductQuantity()));
        }
    }
}
