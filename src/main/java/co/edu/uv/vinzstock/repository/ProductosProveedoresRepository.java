package co.edu.uv.vinzstock.repository;

import co.edu.uv.vinzstock.model.ProductosProveedoresModel;
import co.edu.uv.vinzstock.model.ProductoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductosProveedoresRepository extends JpaRepository<ProductosProveedoresModel, Long> {

    // Obtener todas las relaciones de un proveedor
    List<ProductosProveedoresModel> findByIdProveedorIdProveedor(Long idProveedor);

    // Obtener todas las relaciones de un producto
    List<ProductosProveedoresModel> findByIdProductoIdProducto(Long idProducto);

    // Verificar si existe la relación producto-proveedor
    boolean existsByIdProductoIdProductoAndIdProveedorIdProveedor(Long idProducto, Long idProveedor);

    // ✅ Query para obtener solo los productos de un proveedor
    @Query("SELECT pp.idProducto FROM ProductosProveedoresModel pp WHERE pp.idProveedor.idProveedor = :idProveedor")
    List<ProductoModel> findProductosByProveedorId(@Param("idProveedor") Long idProveedor);
}