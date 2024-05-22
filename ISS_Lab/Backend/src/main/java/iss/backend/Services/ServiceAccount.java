package iss.backend.Services;

import iss.Models.Account;
import iss.backend.Repositories.RepoAccount;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceAccount {

    private RepoAccount repoAccount;

    public Account createAccount(Account account){
        return repoAccount.save(account);
    }

    public Account updateAccount(Account account){
        return repoAccount.save(account);
    }

    public void deleteAccount(Long id){
        repoAccount.deleteById(id);
    }

    public Account getAccount(Long id) {
        return repoAccount.findById(id).orElse(null);
    }

    public List<Account> getAll(){
        return repoAccount.findAll();
    }

}
