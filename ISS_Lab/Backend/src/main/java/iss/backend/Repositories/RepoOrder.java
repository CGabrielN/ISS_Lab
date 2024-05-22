package iss.backend.Repositories;

import iss.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoOrder extends JpaRepository<Order, Long>{
}
