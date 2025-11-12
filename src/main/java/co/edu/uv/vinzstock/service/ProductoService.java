package co.edu.uv.vinzstock.service;


import co.edu.uv.vinzstock.model.ProductoModel;
import co.edu.uv.vinzstock.model.RolesModel;
import co.edu.uv.vinzstock.model.UsuarioModel;
import co.edu.uv.vinzstock.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository){
        this.productoRepository = productoRepository;
    }


    public class ProductoDuplicadoException extends RuntimeException {
        public ProductoDuplicadoException(String campo, String valor) {
            super("Ya existe un producto con el " + campo + ": " + valor);
        }
    }

    public ProductoModel createProducto (ProductoModel productoModel){

        if (productoRepository.existsByNombre(productoModel.getNombre())){
            throw new ProductoService.ProductoDuplicadoException("nombre", productoModel.getNombre());
        }

        return this.productoRepository.save(productoModel);
    }

    /*public ProductoModel updateProducto(ProductoModel productoModel){

        if (productoModel.getRol() != null && usuarioModel.getRol().getIdRol() > 0) {
            Long idRol = usuarioModel.getRol().getIdRol();

            RolesModel rol = rolRepository.findById(idRol)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + idRol));

            usuarioModel.setRol(rol);
        }
        return this.usuarioRepository.save(usuarioModel);
    }*/


    public List<ProductoModel> findAllProductos(){
        return this.productoRepository.findAll();
    }

    public List<ProductoModel> findAllProductosByNombre (String nombre){
        return this.productoRepository.findAllByNombreContains(nombre);
    }

    public List<ProductoModel> findAllByIdProducto (long idProducto){
        return this.productoRepository.findAllByIdProducto(idProducto);
    }
}
