package co.edu.uv.vinzstock.repository;

import co.edu.uv.vinzstock.model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {
    List<ClienteModel> findByNombreRazonSocialContains(String nombreRazonSocial);
    List<ClienteModel> findByIdCliente(long idCliente);
    
    List<ClienteModel> findByNombreRazonSocialContainingIgnoreCase(String nombreRazonSocial);

    boolean existsByNumeroDocumento(String numeroDocumento);
    boolean existsByNombreRazonSocial(String nombreRazonSocial);

    Optional<ClienteModel> findByNumeroDocumento(String numeroDocumento);
    Optional<ClienteModel> findByNombreRazonSocial(String nombreRazonSocial);




}
