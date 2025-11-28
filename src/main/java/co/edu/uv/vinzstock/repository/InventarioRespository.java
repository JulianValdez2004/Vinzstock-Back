package co.edu.uv.vinzstock.repository;

import co.edu.uv.vinzstock.model.InventarioModel;
import co.edu.uv.vinzstock.model.ProductoModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRespository extends JpaRepository<InventarioModel, Long> {

    List<InventarioModel> findAllByIdInventario(long idInventario);
    List<InventarioModel> findByCantidadLessThanEqual(long cantidad);
    
    int countByCantidadLessThanEqual(long cantidad);

    Optional<InventarioModel> findByProducto(ProductoModel producto);
}

