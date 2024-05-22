package iss.backend.Repositories;

import iss.Models.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoDeliveryAddress extends JpaRepository<DeliveryAddress, Long>{
}
