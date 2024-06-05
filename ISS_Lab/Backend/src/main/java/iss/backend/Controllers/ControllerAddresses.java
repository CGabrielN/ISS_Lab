package iss.backend.Controllers;

import iss.Models.DeliveryAddress;
import iss.backend.Services.ServiceDeliveryAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/addresses")
public class ControllerAddresses {

    @Autowired
    private ServiceDeliveryAddress serviceAddresses;

    @GetMapping("/{id}")
    public List<DeliveryAddress> getAddressesForAgent(@PathVariable Long id){
        return serviceAddresses.getAddressesForAgent(id);
    }

    @PostMapping("/add")
    public DeliveryAddress addAddress(@RequestBody DeliveryAddress address){
        return serviceAddresses.createDeliveryAddress(address);
    }

}
