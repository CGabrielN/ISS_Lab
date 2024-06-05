package iss.backend.Repositories;

import iss.Models.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoDeliveryAddress extends JpaRepository<DeliveryAddress, Long>{

    @Query("SELECT d FROM DeliveryAddress d WHERE d.salesAgent.id = ?1")
    List<DeliveryAddress> findByAgentId(Long id);
}
