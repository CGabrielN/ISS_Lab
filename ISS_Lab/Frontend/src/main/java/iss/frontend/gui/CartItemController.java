package iss.frontend.gui;

import iss.Models.Product;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

public class CartItemController {
    public Label nameLabel;
    public Label priceLabel;
    public Label totalLabel;
    public ImageView imageView;
    public TextField quantityInput;

    private Product product;
    private CartController parent;
    private int quantity;

    public void setData(Product product, CartController cartController, int quantity){
        this.product = product;
        this.parent = cartController;
        this.quantity = quantity;
        nameLabel.setText(product.getProductName());
        priceLabel.setText("$" + product.getProductPrice());
        imageView.setImage(new Image(product.getProductImage()));
        totalLabel.setText("$" + product.getProductPrice() * quantity);
        quantityInput.setText(String.valueOf(quantity));
    }


    public void handleIncreaseQuantity(ActionEvent actionEvent) {
        var quantity = Integer.parseInt(quantityInput.getText());
        quantity += quantity < product.getProductQuantity() ? 1 : 0;
        quantityInput.setText(String.valueOf(quantity));
        updateCart(quantity);
    }

    public void handleDecreaseQuantity(ActionEvent actionEvent) {
        var quantity = Integer.parseInt(quantityInput.getText());
        quantity -= quantity >= 1 ? 1 : 0;
        quantityInput.setText(String.valueOf(quantity));
        updateCart(quantity);
    }

    public void handleQuantityInput(KeyEvent event) {
        if(quantityInput.getText().isEmpty() || !quantityInput.getText().matches("[0-9]+")){
            quantityInput.setText("0");
            return;
        }
        var quantity = Integer.parseInt(quantityInput.getText());

        if(quantity < 0){
            quantityInput.setText("0");
        } else if(quantity > product.getProductQuantity()){
            quantityInput.setText(String.valueOf(product.getProductQuantity()));
        }
        updateCart(quantity);
    }

    private void updateCart(int quantity){
        this.totalLabel.setText("$" + product.getProductPrice() * quantity);
        parent.updateCart(product, quantity);
    }
}
