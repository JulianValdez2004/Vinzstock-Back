package co.edu.uv.vinzstock.service;

import co.edu.uv.vinzstock.model.ProveedoresModel;
import co.edu.uv.vinzstock.repository.ProveedoresRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedoresService {
    private final ProveedoresRepository proveedoresRepository;

    public ProveedoresService(ProveedoresRepository proveedoresRepository){
        this.proveedoresRepository = proveedoresRepository;
    }

    public List <ProveedoresModel> findAllProveedores(){
        return this.proveedoresRepository.findAll();
    }
}
