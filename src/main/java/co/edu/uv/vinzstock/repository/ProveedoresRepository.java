package co.edu.uv.vinzstock.repository;



import co.edu.uv.vinzstock.model.ProveedoresModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProveedoresRepository extends JpaRepository <ProveedoresModel, Long> {

    List <ProveedoresModel> findAllByIdProveedor(long idProveedor);

}
