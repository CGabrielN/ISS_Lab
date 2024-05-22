package iss.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "orders", schema = "public")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @NotEmpty(message = "Try again! Order date cannot be empty")
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "delivery_price")
    private double deliveryPrice;

    @Column(name = "status")
    private String status;


    @ManyToOne
    @JoinColumn(name="sales_agent_id")
    private SalesAgent salesAgent;

    @ManyToOne
    @JoinColumn(name="delivery_address_id")
    private DeliveryAddress deliveryAddress;

}
