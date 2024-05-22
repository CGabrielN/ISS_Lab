package iss.backend.Repositories;

import iss.Models.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoManager extends JpaRepository<Manager, Long>{

    @Query("SELECT m FROM Manager m WHERE m.account.username = ?1 AND m.account.passwordHash = ?2")
    Manager findByUsernameAndPassword(String username, String password);
}
