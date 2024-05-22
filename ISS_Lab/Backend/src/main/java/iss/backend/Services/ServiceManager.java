package iss.backend.Services;

import iss.Models.Manager;
import iss.backend.Repositories.RepoManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceManager {

    private final RepoManager repoManager;

    public Manager login(String username, String password) {
        return repoManager.findByUsernameAndPassword(username, password);
    }

    public void deleteManager(Long id){
        repoManager.deleteById(id);
    }

    public Manager getManager(Long id) {
        return repoManager.findById(id).orElse(null);
    }

    public Manager createManager(Manager manager){
        return repoManager.save(manager);
    }

    public Manager updateManager(Manager manager){
        return repoManager.save(manager);
    }

    public List<Manager> getAll(){
        return repoManager.findAll();
    }

}
