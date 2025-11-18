package co.edu.uv.vinzstock.service;

import co.edu.uv.vinzstock.model.ComprasModel;
import co.edu.uv.vinzstock.model.InventarioModel;
import co.edu.uv.vinzstock.repository.CompraRepository;
import co.edu.uv.vinzstock.repository.InventarioRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraSerivce {
    private final CompraRepository compraRepository;

    @Autowired
    public CompraSerivce(CompraRepository compraRepository ){this.compraRepository = compraRepository;}

    public List<ComprasModel> findAllComprasModels(){
        return this.compraRepository.findAll();
    }
}
