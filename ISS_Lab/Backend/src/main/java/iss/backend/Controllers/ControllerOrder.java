package iss.backend.Controllers;

import iss.Models.Order;
import iss.Models.OrderItem;
import iss.Models.Product;
import iss.backend.Services.ServiceOrder;
import iss.backend.Services.ServiceOrderItem;
import iss.backend.Services.ServiceProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class ControllerOrder {

    @Autowired
    private ServiceOrder serviceOrders;

    @Autowired
    private ServiceOrderItem serviceOrderItems;

    @Autowired
    private ServiceProduct serviceProducts;

    @PostMapping("/add")
    public Order addOrder(@RequestBody Order order) {
        return serviceOrders.createOrder(order);
    }

    @PostMapping("/addItem")
    public OrderItem addOrderItem(@RequestBody OrderItem orderItem) {
        // Save the order item
        OrderItem savedOrderItem = serviceOrderItems.createOrderItem(orderItem);

        // Update the quantity of the product
        Product product = savedOrderItem.getProduct();
        product.setProductQuantity(product.getProductQuantity() - savedOrderItem.getQuantity());
        serviceProducts.updateProduct(product);

        return savedOrderItem;
    }
}
