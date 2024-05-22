package iss.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "products", schema = "public")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @NotEmpty(message = "Try again! Product name cannot be empty")
    @Column(name = "product_name")
    private String productName;

    @NotEmpty(message = "Try again! Product description cannot be empty")
    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "product_price")
    private double productPrice;

    @Column(name = "product_quantity")
    private int productQuantity;

    @Column(name = "product_image")
    private String productImage;

    @ManyToOne
    @JoinColumn(name="company_id")
    private Company company;


}
