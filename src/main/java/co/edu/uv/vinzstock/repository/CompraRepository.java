package co.edu.uv.vinzstock.repository;

import co.edu.uv.vinzstock.model.ComprasModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<ComprasModel, Long> {

    // Buscar compras por proveedor
    List<ComprasModel> findByIdProveedorIdProveedor(Long idProveedor);

    // Buscar compras por fecha
    List<ComprasModel> findByFecha(LocalDate fecha);

    // Buscar compras entre fechas
    List<ComprasModel> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Ordenar por fecha descendente
    List<ComprasModel> findAllByOrderByFechaDescHoraDesc();
}