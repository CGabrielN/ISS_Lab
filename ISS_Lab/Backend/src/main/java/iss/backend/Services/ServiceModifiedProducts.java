package iss.backend.Services;

import iss.Models.ModifiedProducts;
import iss.backend.Repositories.RepoModifiedProducts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceModifiedProducts {

    private final RepoModifiedProducts repoModifiedProducts;

    public void deleteModifiedProducts(Long id){
        repoModifiedProducts.deleteById(id);
    }

    public List<ModifiedProducts> getAll(){
        return repoModifiedProducts.findAll();
    }

    public ModifiedProducts getModifiedProducts(Long id) {
        return repoModifiedProducts.findById(id).orElse(null);
    }

    public ModifiedProducts createModifiedProducts(ModifiedProducts modifiedProducts){
        return repoModifiedProducts.save(modifiedProducts);
    }

    public ModifiedProducts updateModifiedProducts(ModifiedProducts modifiedProducts){
        return repoModifiedProducts.save(modifiedProducts);
    }

}
