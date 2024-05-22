package iss.backend.Services;

import iss.Models.Product;
import iss.backend.Repositories.RepoProduct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceProduct {

    private final RepoProduct repoProduct;

    public void deleteProduct(Long id) {
        repoProduct.deleteById(id);
    }

    public void updateProduct(Product product) {
        repoProduct.save(product);
    }

    public List<Product> getAll() {
        return repoProduct.findAll();
    }

    public Product createProduct(Product product) {
        return repoProduct.save(product);
    }

    public Product getProduct(Long id) {
        return repoProduct.findById(id).orElse(null);
    }
}
