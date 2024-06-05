package iss.backend.Controllers;

import iss.Models.AccountsDTO;
import iss.backend.Services.ServiceManager;
import iss.backend.Services.ServiceSalesAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class ControllerAccount {

    @Autowired
    private ServiceSalesAgent serviceSalesAgent;

    @Autowired
    private ServiceManager serviceManager;

    @GetMapping("/login/{username}/{password}")
    public ResponseEntity<AccountsDTO> login(@PathVariable String username, @PathVariable String password) {
        var manager = serviceManager.login(username, password);
        if (manager != null) {
            return ResponseEntity.ok(new AccountsDTO(manager));
        } else {
            var salesAgent = serviceSalesAgent.login(username, password);
            if (salesAgent != null) {
                return ResponseEntity.ok(new AccountsDTO(salesAgent));
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }
}
