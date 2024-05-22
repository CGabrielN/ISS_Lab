package iss.backend.Repositories;

import iss.Models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoCompany extends JpaRepository<Company, Long> {
}
