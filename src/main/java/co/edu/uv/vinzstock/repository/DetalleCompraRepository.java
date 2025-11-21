package co.edu.uv.vinzstock.repository;

import co.edu.uv.vinzstock.model.DetalleCompraModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompraModel, Long> {

    // Buscar detalles por compra
    List<DetalleCompraModel> findByIdCompraIdCompra(Long idCompra);

    // Buscar detalles por producto
    List<DetalleCompraModel> findByIdProductoIdProducto(Long idProducto);
}