package iss.backend.Repositories;

import iss.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoProduct extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.company.id = ?1")
    List<Product> getProductsForCompany(long company_id);

    @Query("SELECT p FROM Product p WHERE p.company.id = ?1 AND LOWER(p.productName) LIKE LOWER(CONCAT('%', ?2, '%'))")
    List<Product> getFilteredProductsForCompany(long company_id, String name);

}
