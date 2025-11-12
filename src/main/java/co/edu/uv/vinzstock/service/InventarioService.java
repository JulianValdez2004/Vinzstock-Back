package co.edu.uv.vinzstock.service;


import co.edu.uv.vinzstock.model.InventarioModel;
import co.edu.uv.vinzstock.model.ProductoModel;
import co.edu.uv.vinzstock.repository.InventarioRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {
    private final InventarioRespository inventarioRespository;


    @Autowired
    public InventarioService(InventarioRespository inventarioRespository){this.inventarioRespository = inventarioRespository;}

    public List<InventarioModel> findAllInventarios(){
        return this.inventarioRespository.findAll();
    }
}
