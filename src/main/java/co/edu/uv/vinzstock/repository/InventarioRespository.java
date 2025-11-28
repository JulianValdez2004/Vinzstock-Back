package co.edu.uv.vinzstock.repository;

import co.edu.uv.vinzstock.model.InventarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRespository extends JpaRepository<InventarioModel, Long> {


    List<InventarioModel> findAllByIdInventario(long idInventario);

    /**
     * Buscar inventario por ID de producto
     * Esto es lo que necesitas en confirmarRecepcionCompra()
     */
    @Query("SELECT i FROM InventarioModel i WHERE i.producto.idProducto = :idProducto")
    Optional<InventarioModel> findByProductoId(@Param("idProducto") long idProducto);

    /**
     * Verificar si existe inventario para un producto
     */
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM InventarioModel i WHERE i.producto.idProducto = :idProducto")
    boolean existsByProductoId(@Param("idProducto") long idProducto);

    /**
     * Obtener todos los inventarios con información del producto
     */
    @Query("SELECT i FROM InventarioModel i JOIN FETCH i.producto")
    List<InventarioModel> findAllWithProducto();
}
