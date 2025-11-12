package co.edu.uv.vinzstock.service;

import co.edu.uv.vinzstock.model.RolesModel;
import co.edu.uv.vinzstock.repository.RolRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {
    private final RolRespository rolRespository;


    @Autowired
    public RolService (RolRespository rolRespository){
        this.rolRespository = rolRespository;
    }


    public List<RolesModel> findAllRoles() {
        return rolRespository.findAll();
    }

    public List<RolesModel> findAllByIdRol(long idRol){
        return this.rolRespository.findAllByIdRol(idRol);
    }

}
