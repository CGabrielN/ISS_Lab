package iss.backend.Services;

import iss.Models.SalesAgent;
import iss.backend.Repositories.RepoSalesAgent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceSalesAgent {

    private RepoSalesAgent repoSalesAgent;

    public SalesAgent login(String username, String password){
        return repoSalesAgent.findByUsernameAndPassword(username, password);
    }

    public List<SalesAgent> getAgentsForManager(Long id){
        return repoSalesAgent.findByManagerId(id);
    }

    public List<SalesAgent> getAgentsFiltered(String username, Long id){
        return repoSalesAgent.findByUsernameContainingAndManagerId(username, id);
    }

    public SalesAgent createAgent(SalesAgent salesAgent){
        return repoSalesAgent.save(salesAgent);
    }

    public SalesAgent updateAgent(SalesAgent salesAgent){
        return repoSalesAgent.save(salesAgent);
    }

    public void deleteAgent(Long id){
        repoSalesAgent.deleteById(id);
    }

    public List<SalesAgent> getAllAgents(){
        return repoSalesAgent.findAll();
    }

    public SalesAgent findOneAgent(Long id){
        return repoSalesAgent.findById(id).orElse(null);
    }

    public SalesAgent getAgentById(Long id){
        return repoSalesAgent.findById(id).orElse(null);
    }
}
