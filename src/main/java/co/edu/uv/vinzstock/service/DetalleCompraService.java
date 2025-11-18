package co.edu.uv.vinzstock.service;

import co.edu.uv.vinzstock.model.DetalleCompraModel;
import co.edu.uv.vinzstock.repository.DetalleCompraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleCompraService {
    private final DetalleCompraRepository detalleCompraRepository;

    public DetalleCompraService (DetalleCompraRepository detalleCompraRepository){this.detalleCompraRepository = detalleCompraRepository;}

    public List <DetalleCompraModel> findAllDetalleCompra (){
        return detalleCompraRepository.findAll();
    }


}
