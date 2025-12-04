package co.edu.uv.vinzstock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.edu.uv.vinzstock.model.DetallesVentasModel;
import java.util.List;


@Repository
public interface DetallesVentasRepository extends JpaRepository <DetallesVentasModel, Long>{


    List<DetallesVentasModel> findByProductoIdProducto(Long idProducto);
    
    @Query("SELECT d FROM DetallesVentasModel d WHERE d.venta.idVenta = :idVenta")
    List<DetallesVentasModel> findByVentaIdVenta(@Param("idVenta") Long idVenta);
}
