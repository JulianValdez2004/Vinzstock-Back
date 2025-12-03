package co.edu.uv.vinzstock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uv.vinzstock.model.DetallesVentasModel;
import java.util.List;


@Repository
public interface DetallesVentasRepository extends JpaRepository <DetallesVentasModel, Long>{


    List<DetallesVentasModel> findByProductoIdProducto(Long idProducto);
    List<DetallesVentasModel> findByVentaIdVenta(Long idVenta);

}
