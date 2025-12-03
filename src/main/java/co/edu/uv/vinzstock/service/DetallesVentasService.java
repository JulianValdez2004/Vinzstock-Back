package co.edu.uv.vinzstock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uv.vinzstock.model.DetallesVentasModel;
import co.edu.uv.vinzstock.repository.DetallesVentasRepository;



@Service
public class DetallesVentasService {

    private final DetallesVentasRepository detallesVentasRepository;

    @Autowired
    public DetallesVentasService (DetallesVentasRepository detallesVentasRepository){
        this.detallesVentasRepository = detallesVentasRepository;
    }

    public List<DetallesVentasModel> findAllDetallesVentas(){
        return this.detallesVentasRepository.findAll();
    }

}
