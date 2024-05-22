package iss.backend.Services;

import iss.Models.Company;
import iss.backend.Repositories.RepoCompany;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceCompany {

    private final RepoCompany repositoryCompany;

    public void deleteCompany(Long id){
        repositoryCompany.deleteById(id);
    }

    public List<Company> getAll(){
        return repositoryCompany.findAll();
    }

    public Company getCompany(Long id) {
        return repositoryCompany.findById(id).orElse(null);
    }

    public Company createCompany(Company company){
        return repositoryCompany.save(company);
    }

    public Company updateCompany(Company company){
        return repositoryCompany.save(company);
    }

}
