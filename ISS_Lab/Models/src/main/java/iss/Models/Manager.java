package iss.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "managers", schema = "public")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Length(max = 255, message = "Full name is too long")
    @Column(name = "full_name")
    private String fullName;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;


}
