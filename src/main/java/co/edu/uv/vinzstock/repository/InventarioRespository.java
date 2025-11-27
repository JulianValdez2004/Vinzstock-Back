package co.edu.uv.vinzstock.repository;

import co.edu.uv.vinzstock.model.InventarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRespository extends JpaRepository<InventarioModel, Long> {

    List<InventarioModel> findAllByIdInventario(long idInventario);
    List<InventarioModel> findByCantidadLessThanEqual(long cantidad);
    
    int countByCantidadLessThanEqual(long cantidad);
}

