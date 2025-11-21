package co.edu.uv.vinzstock.repository;



import co.edu.uv.vinzstock.model.ProveedoresModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedoresRepository extends JpaRepository <ProveedoresModel, Long> {

    List <ProveedoresModel> findAllByIdProveedor(long idProveedor);

    // --- BÚSQUEDAS BÁSICAS ---
    List<ProveedoresModel> findAllByNombre(String nombre);

    List<ProveedoresModel> findByNombreContainingIgnoreCase(String nombre);

    Optional<ProveedoresModel> findByNitFiscal(String nitFiscal);

    Optional<ProveedoresModel> findByEmail(String email);

    boolean existsByNitFiscal(String nitFiscal);

    boolean existsByEmail(String email);

    boolean existsByNombreIgnoreCase(String nombre);
}
