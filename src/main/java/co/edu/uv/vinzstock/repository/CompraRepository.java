package co.edu.uv.vinzstock.repository;

import co.edu.uv.vinzstock.model.ComprasModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<ComprasModel, Long> {

    // ========================================
    // QUERIES EXISTENTES (mantenidas)
    // ========================================
    
    /**
     * Obtener TODAS las compras de un proveedor (sin filtrar por estado)
     * Esto es lo que necesitas para el historial según la HU
     */
    List<ComprasModel> findByIdProveedorIdProveedor(Long idProveedor);
    
    List<ComprasModel> findByFecha(LocalDate fecha);
    
    List<ComprasModel> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    List<ComprasModel> findAllByOrderByFechaDescHoraDesc();

    /**
     * Obtener TODAS las compras de un proveedor ordenadas por fecha
     * (Pendientes, Recibidas, Canceladas - TODAS)
     */
    @Query("SELECT c FROM ComprasModel c WHERE c.idProveedor.idProveedor = :idProveedor ORDER BY c.fecha DESC, c.hora DESC")
    List<ComprasModel> findAllByProveedorOrderByFechaDesc(@Param("idProveedor") Long idProveedor);

    /**
     * Obtener compras por estado específico (para filtros futuros si los necesitas)
     */
    List<ComprasModel> findByEstadoOrderByFechaDescHoraDesc(String estado);

    /**
     * Obtener compras por proveedor y estado específico (útil para acciones)
     */
    @Query("SELECT c FROM ComprasModel c WHERE c.idProveedor.idProveedor = :idProveedor AND c.estado = :estado ORDER BY c.fecha DESC, c.hora DESC")
    List<ComprasModel> findByProveedorAndEstado(
        @Param("idProveedor") Long idProveedor, 
        @Param("estado") String estado
    );

    /**
     * Verificar si una compra existe y está en estado Pendiente
     * (útil para validar antes de confirmar/cancelar)
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ComprasModel c WHERE c.idCompra = :idCompra AND c.estado = 'Pendiente'")
    boolean existsByIdAndEstadoPendiente(@Param("idCompra") Long idCompra);

    /**
     * Contar compras por estado
     */
    Long countByEstado(String estado);

    /**
     * Contar compras de un proveedor por estado
     */
    @Query("SELECT COUNT(c) FROM ComprasModel c WHERE c.idProveedor.idProveedor = :idProveedor AND c.estado = :estado")
    Long countByProveedorAndEstado(@Param("idProveedor") Long idProveedor, @Param("estado") String estado);

    /**
     * Contar TODAS las compras de un proveedor
     */
    @Query("SELECT COUNT(c) FROM ComprasModel c WHERE c.idProveedor.idProveedor = :idProveedor")
    Long countByProveedor(@Param("idProveedor") Long idProveedor);

    /**
     * 🗑️ Eliminar TODAS las compras de un proveedor (para "borrar historial")
     * CUIDADO: Esto borra TODO el historial del proveedor
     */
    void deleteByIdProveedorIdProveedor(Long idProveedor);
}