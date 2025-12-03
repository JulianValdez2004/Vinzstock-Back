package co.edu.uv.vinzstock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uv.vinzstock.model.VentasModel;
import co.edu.uv.vinzstock.repository.VentasRepository;

@Service
public class VentasService {

    private final VentasRepository ventasRepository;


    @Autowired
    public VentasService(VentasRepository ventasRepository){
        this.ventasRepository = ventasRepository;
    }

    public List<VentasModel> findAllVentas(){
        return ventasRepository.findAll();
    }


}
