package co.edu.uv.vinzstock.service;


import co.edu.uv.vinzstock.model.InventarioModel;
import co.edu.uv.vinzstock.model.ProductoModel;
import co.edu.uv.vinzstock.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {
    private final InventarioRepository inventarioRepository;


    @Autowired
    public InventarioService(InventarioRepository inventarioRepository){this.inventarioRepository = inventarioRepository;}

    public List<InventarioModel> findAllInventarios(){
        return this.inventarioRepository.findAll();
    }
}
