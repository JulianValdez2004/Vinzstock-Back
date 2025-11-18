package co.edu.uv.vinzstock.service;


import co.edu.uv.vinzstock.model.ProductosProveedoresModel;
import co.edu.uv.vinzstock.repository.ProductosProveedoresRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductosProveedoresService {

    private final ProductosProveedoresRepository productosProveedoresRepository;

    public ProductosProveedoresService(ProductosProveedoresRepository productosProveedoresRepository){this.productosProveedoresRepository = productosProveedoresRepository;}

    public List<ProductosProveedoresModel> findAllProductosProveedores(){
        return this.productosProveedoresRepository.findAll();
    }
}
