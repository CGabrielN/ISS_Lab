package iss.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountsDTO {

    private Long id;
    private String role;
    private String fullName;
    private Company company;
    private Manager manager;
    private Account account;

    public AccountsDTO(Manager manager) {
        this.id = manager.getId();
        this.role = "Manager";
        this.account = manager.getAccount();
        this.fullName = manager.getFullName();
        this.company = this.account.getCompany();
        this.manager = null;
    }

    public AccountsDTO(SalesAgent salesAgent) {
        this.id = salesAgent.getId();
        this.role = "SalesAgent";
        this.account = salesAgent.getAccount();
        this.fullName = salesAgent.getFullName();
        this.company = this.account.getCompany();
        this.manager = salesAgent.getManager();
    }

    public Manager toManager() {
        return new Manager(this.id, this.fullName, this.account);
    }

    public SalesAgent toSalesAgent() {
        return new SalesAgent(this.id, this.fullName, this.account, this.manager);
    }
}
