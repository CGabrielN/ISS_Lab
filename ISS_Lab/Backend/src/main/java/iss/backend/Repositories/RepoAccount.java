package iss.backend.Repositories;

import iss.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoAccount extends JpaRepository<Account, Long>{
}
