package co.edu.uv.vinzstock.service;

import co.edu.uv.vinzstock.model.ProductosProveedoresModel;
import co.edu.uv.vinzstock.model.ProductoModel;
import co.edu.uv.vinzstock.repository.ProductosProveedoresRepository;
import co.edu.uv.vinzstock.repository.ProveedoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductosProveedoresService {

    private final ProductosProveedoresRepository productosProveedoresRepository;
    private final ProveedoresRepository proveedoresRepository;

    @Autowired
    public ProductosProveedoresService(
            ProductosProveedoresRepository productosProveedoresRepository,
            ProveedoresRepository proveedoresRepository) {
        this.productosProveedoresRepository = productosProveedoresRepository;
        this.proveedoresRepository = proveedoresRepository;
    }

    /**
     * ✅ OBTENER PRODUCTOS DE UN PROVEEDOR
     */
    public List<ProductoModel> findProductosByProveedor(Long idProveedor) {
        // Verificar que el proveedor existe
        if (!proveedoresRepository.existsById(idProveedor)) {
            throw new RuntimeException("Proveedor no encontrado con ID: " + idProveedor);
        }

        // Obtener las relaciones y extraer los productos
        List<ProductosProveedoresModel> relaciones = productosProveedoresRepository
                .findByIdProveedorIdProveedor(idProveedor);

        // Extraer solo los productos de las relaciones
        return relaciones.stream()
                .map(ProductosProveedoresModel::getIdProducto)
                .collect(Collectors.toList());
    }

    /**
     * Obtener todas las relaciones de un proveedor (con datos completos)
     */
    public List<ProductosProveedoresModel> findRelacionesByProveedor(Long idProveedor) {
        return productosProveedoresRepository.findByIdProveedorIdProveedor(idProveedor);
    }

    /**
     * Obtener todas las relaciones
     */
    public List<ProductosProveedoresModel> findAll() {
        return productosProveedoresRepository.findAll();
    }
}