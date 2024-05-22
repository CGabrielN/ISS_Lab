package iss.backend.Services;

import iss.Models.DeliveryAddress;
import iss.backend.Repositories.RepoDeliveryAddress;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceDeliveryAddress {

    private final RepoDeliveryAddress repoDeliveryAddress;

    public void deleteDeliveryAddress(Long id){
        repoDeliveryAddress.deleteById(id);
    }

    public List<DeliveryAddress> getAll(){
        return repoDeliveryAddress.findAll();
    }

    public DeliveryAddress getDeliveryAddress(Long id) {
        return repoDeliveryAddress.findById(id).orElse(null);
    }

    public DeliveryAddress createDeliveryAddress(DeliveryAddress deliveryAddress){
        return repoDeliveryAddress.save(deliveryAddress);
    }

    public DeliveryAddress updateDeliveryAddress(DeliveryAddress deliveryAddress){
        return repoDeliveryAddress.save(deliveryAddress);
    }

}
