package iss.backend.Controllers;

import iss.Models.SalesAgent;
import iss.backend.Services.ServiceAccount;
import iss.backend.Services.ServiceSalesAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/manager")
public class ControllerManager {

    @Autowired
    private ServiceSalesAgent serviceSalesAgent;

    @Autowired
    private ServiceAccount serviceAccount;


    @GetMapping("/agents/{id}")
    public ResponseEntity<List<SalesAgent>> getAgents(@PathVariable Long id) {
        return ResponseEntity.ok(serviceSalesAgent.getAgentsForManager(id));
    }

    @GetMapping("/agentsFiltered/{username}/{id}")
    public ResponseEntity<List<SalesAgent>> getAgentsFiltered(@PathVariable String username, @PathVariable Long id) {
        return ResponseEntity.ok(serviceSalesAgent.getAgentsFiltered(username, id));
    }

    @PostMapping("/agents/create")
    public ResponseEntity<SalesAgent> createAgent(@RequestBody SalesAgent salesAgent) {
        var account = salesAgent.getAccount();
        var createdAcc = serviceAccount.createAccount(account);
        salesAgent.setAccount(createdAcc);
        return ResponseEntity.ok(serviceSalesAgent.createAgent(salesAgent));
    }

    @PutMapping("/agents/update")
    public ResponseEntity<SalesAgent> updateAgent(@RequestBody SalesAgent salesAgent) {
        var account = salesAgent.getAccount();
        serviceAccount.updateAccount(account);
        var agent = serviceSalesAgent.updateAgent(salesAgent);
        return ResponseEntity.ok(agent);
    }

    @DeleteMapping("/agents/delete/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        var accountID = serviceSalesAgent.getAgentById(id).getAccount().getId();
        serviceSalesAgent.deleteAgent(id);
        serviceAccount.deleteAccount(accountID);
        return ResponseEntity.ok().build();
    }

}
