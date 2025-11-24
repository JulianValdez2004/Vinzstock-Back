package co.edu.uv.vinzstock.service;


import co.edu.uv.vinzstock.model.InventarioModel;
import co.edu.uv.vinzstock.model.ProductoModel;
import co.edu.uv.vinzstock.model.RolesModel;
import co.edu.uv.vinzstock.model.UsuarioModel;
import co.edu.uv.vinzstock.repository.InventarioRespository;
import co.edu.uv.vinzstock.repository.ProductoRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final InventarioRespository inventarioRespository;
    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository, InventarioRespository inventarioRespository){
        this.productoRepository = productoRepository;
        this.inventarioRespository = inventarioRespository;
    }


    public class ProductoDuplicadoException extends RuntimeException {
        public ProductoDuplicadoException(String campo, String valor) {
            super("Ya existe un producto con el " + campo + ": " + valor);
        }
    }




    public List<ProductoModel> findAllProductosByNombre (String nombre){
        return this.productoRepository.findAllByNombreContains(nombre);
    }

    public List<ProductoModel> findAllByIdProducto (long idProducto){
        return this.productoRepository.findAllByIdProducto(idProducto);
    }


    /**
     * Crear producto
     */
    @Transactional
    public ProductoModel createProducto(ProductoModel productoModel) {
        // Validar nombre duplicado
        if (productoRepository.existsByNombreIgnoreCase(productoModel.getNombre())) {
            throw new RuntimeException("Ya existe un producto con el nombre: " + productoModel.getNombre());
        }

        // Validar precio
        if (productoModel.getPrecioVenta() <= 0) {
            throw new RuntimeException("El precio de venta debe ser mayor a 0");
        }

        // Validar IVA
        if (productoModel.getIva() < 0 || productoModel.getIva() > 100) {
            throw new RuntimeException("El IVA debe estar entre 0 y 100");
        }
        // Guardar el producto
        ProductoModel productoGuardado = productoRepository.save(productoModel);
    
        // Crear el inventario inicial con cantidad 0
        InventarioModel inventario = InventarioModel.builder()
                .producto(productoGuardado)
                .cantidad(0L)
                .build();
    
        inventarioRespository.save(inventario);
    
        return productoGuardado;

        
    }

    /**
     * Actualizar producto
     */
    @Transactional
    public ProductoModel updateProducto(ProductoModel productoModel) {
        ProductoModel productoExistente = productoRepository.findById(productoModel.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoModel.getIdProducto()));

        // Validar nombre duplicado (solo si cambió)
        if (!productoExistente.getNombre().equalsIgnoreCase(productoModel.getNombre())) {
            if (productoRepository.existsByNombreIgnoreCase(productoModel.getNombre())) {
                throw new RuntimeException("Ya existe un producto con el nombre: " + productoModel.getNombre());
            }
        }

        // Validaciones
        if (productoModel.getPrecioVenta() <= 0) {
            throw new RuntimeException("El precio de venta debe ser mayor a 0");
        }

        if (productoModel.getIva() < 0 || productoModel.getIva() > 100) {
            throw new RuntimeException("El IVA debe estar entre 0 y 100");
        }

        // Actualizar campos
        productoExistente.setNombre(productoModel.getNombre());
        productoExistente.setDescripcion(productoModel.getDescripcion());
        productoExistente.setPrecioVenta(productoModel.getPrecioVenta());
        productoExistente.setIva(productoModel.getIva());

        return productoRepository.save(productoExistente);
    }

    /**
     * Eliminar producto
     */
    public void deleteProducto(long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    /**
     * Obtener todos los productos
     */
    public List<ProductoModel> findAllProductos() {
        return productoRepository.findAll();
    }

    /**
     * Obtener producto por ID
     */
    public Optional<ProductoModel> findProductoById(long id) {
        return productoRepository.findById(id);
    }

    /**
     * Buscar productos por nombre
     */
    public List<ProductoModel> findByNombreContaining(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
