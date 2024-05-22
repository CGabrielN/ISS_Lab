package iss.backend.Services;

import iss.Models.Order;
import iss.backend.Repositories.RepoOrder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceOrder {

    private final RepoOrder repoOrder;

    public void deleteOrder(Long id){
        repoOrder.deleteById(id);
    }

    public Order getOrder(Long id){
        return repoOrder.findById(id).orElse(null);
    }

    public Order createOrder(Order order){
        return repoOrder.save(order);
    }

    public Order updateOrder(Order order){
        return repoOrder.save(order);
    }

    public List<Order> getAll(){
        return repoOrder.findAll();
    }

}
