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

}