package iss.backend.Repositories;

import iss.Models.ModifiedProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoModifiedProducts extends JpaRepository<ModifiedProducts, Long> {
}
