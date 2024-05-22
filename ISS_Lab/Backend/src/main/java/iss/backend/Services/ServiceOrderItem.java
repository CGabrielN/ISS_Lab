package iss.backend.Services;

import iss.Models.OrderItem;
import iss.backend.Repositories.RepoOrderItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceOrderItem {

    private final RepoOrderItem repoOrderItem;

    public void deleteOrderItem(Long id){
        repoOrderItem.deleteById(id);
    }

    public List<OrderItem> getAll(){
        return repoOrderItem.findAll();
    }

    public OrderItem createOrderItem(OrderItem orderItem){
        return repoOrderItem.save(orderItem);
    }

    public OrderItem updateOrderItem(OrderItem orderItem){
        return repoOrderItem.save(orderItem);
    }

    public OrderItem getOrderItem(Long id) {
        return repoOrderItem.findById(id).orElse(null);
    }

}
