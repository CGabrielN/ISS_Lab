package iss.backend.Repositories;

import iss.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoProduct extends JpaRepository<Product, Long> {
}
