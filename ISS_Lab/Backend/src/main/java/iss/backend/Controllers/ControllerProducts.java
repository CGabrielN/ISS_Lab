package iss.backend.Controllers;

import iss.Models.Product;
import iss.Models.SalesAgent;
import iss.backend.Services.ServiceProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ControllerProducts {


    @Autowired
    private ServiceProduct serviceProduct;

    @GetMapping("/{id}")
    public ResponseEntity<List<Product>> getProductsByCompany(@PathVariable Long id) {
        return ResponseEntity.ok(serviceProduct.getProductsForCompany(id));
    }

    @GetMapping("/filtered/{id}/{name}")
    public ResponseEntity<List<Product>> getFilteredProductsForCompany(@PathVariable Long id, @PathVariable String name) {
        return ResponseEntity.ok(serviceProduct.getFilteredProductsForCompany(id, name.toLowerCase()));
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(serviceProduct.createProduct(product));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        serviceProduct.deleteProduct(id);

        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/quantity/{id}")
    public ResponseEntity<Integer> getStockQuantity(@PathVariable Long id) {
        Product product = serviceProduct.getProduct(id);
        if (product != null) {
            return ResponseEntity.ok(product.getProductQuantity());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
