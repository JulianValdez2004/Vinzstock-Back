package co.edu.uv.vinzstock.repository;

import co.edu.uv.vinzstock.model.VentasModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VentasRepository extends JpaRepository<VentasModel, Long> {

    List<VentasModel> findByIdVenta(long idVenta);

    @Query("SELECT v FROM VentasModel v WHERE v.usuario.idUsuario = :idUsuario")
    List<VentasModel> findByUsuarioIdUsuario(@Param("idUsuario") Long idUsuario);

    @Query("SELECT v FROM VentasModel v WHERE v.cliente.idCliente = :idCliente")
    List<VentasModel> findByClienteIdCliente(@Param("idCliente") Long idCliente);

    @Query("SELECT v FROM VentasModel v WHERE v.fecha = :fecha")
    List<VentasModel> findByFecha(@Param("fecha") LocalDate fecha);

    /* Ventas del día ordenadas por hora descendente */
    @Query("SELECT v FROM VentasModel v WHERE v.fecha = :fecha ORDER BY v.hora DESC")
    List<VentasModel> findVentasDelDiaOrdenadas(@Param("fecha") LocalDate fecha);

    /* Ventas entre un rango de fechas */
    @Query("SELECT v FROM VentasModel v WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY v.fecha DESC, v.hora DESC")
    List<VentasModel> findByFechaBetween(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    /* Ventas por nombre/razón social del cliente con rango de fechas */
    @Query("SELECT v FROM VentasModel v WHERE " +
            "LOWER(v.cliente.nombreRazonSocial) LIKE LOWER(CONCAT('%', :busqueda, '%')) AND " +
            "v.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "ORDER BY v.fecha DESC, v.hora DESC")
    List<VentasModel> findByClienteNombreOEmpresaYRangoFechas(
            @Param("busqueda") String busqueda,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT v FROM VentasModel v WHERE " +
            "LOWER(v.cliente.nombreRazonSocial) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
            "ORDER BY v.fecha DESC, v.hora DESC")
    List<VentasModel> findByClienteNombreOEmpresa(@Param("busqueda") String busqueda);
}
