package iss.backend.Repositories;

import iss.Models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoOrderItem extends JpaRepository<OrderItem, Long> {
}
