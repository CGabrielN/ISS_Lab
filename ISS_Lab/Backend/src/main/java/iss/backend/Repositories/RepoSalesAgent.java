package iss.backend.Repositories;

import iss.Models.SalesAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoSalesAgent extends JpaRepository<SalesAgent, Long> {

    @Query("SELECT s FROM SalesAgent s WHERE s.account.username = ?1 AND s.account.passwordHash = ?2")
    SalesAgent findByUsernameAndPassword(String username, String password);

    @Query("SELECT s FROM SalesAgent s WHERE s.manager.id = ?1")
    List<SalesAgent> findByManagerId(Long id);

    @Query("SELECT s FROM SalesAgent s WHERE s.account.username LIKE %?1% AND s.manager.id = ?2")
    List<SalesAgent> findByUsernameAndManagerId(String username, Long id);
}
