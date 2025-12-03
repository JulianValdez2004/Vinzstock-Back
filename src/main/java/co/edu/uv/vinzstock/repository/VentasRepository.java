package co.edu.uv.vinzstock.repository;

import co.edu.uv.vinzstock.model.VentasModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VentasRepository extends JpaRepository<VentasModel, Long> {

    List<VentasModel> findByIdVenta(long idVenta);
    List<VentasModel> findByClienteIdCliente(Long idCliente);
    List<VentasModel> findByUsuarioIdUsuario(Long idUsuario);

}