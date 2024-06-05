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
@Table(name = "delivery_addresses", schema = "public")
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @NotEmpty(message = "Try again! Street cannot be empty")
    @Column(name = "street")
    private String street;

    @Column(name = "street_nr")
    private int streetNr;

    @NotEmpty(message = "Try again! City cannot be empty")
    @Column(name = "city")
    private String city;

    @NotEmpty(message = "Try again! County cannot be empty")
    @Column(name = "county")
    private String county;

    @NotEmpty(message = "Try again! Country cannot be empty")
    @Column(name = "country")
    private String country;

    @Column(name = "building")
    private int building;

    @Column(name = "suite")
    private int suite;

    @ManyToOne
    @JoinColumn(name="sales_agent_id")
    private SalesAgent salesAgent;

}
