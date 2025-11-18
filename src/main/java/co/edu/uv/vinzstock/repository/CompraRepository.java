package co.edu.uv.vinzstock.repository;

import co.edu.uv.vinzstock.model.ComprasModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CompraRepository extends JpaRepository <ComprasModel, Long> {

    List<ComprasModel> findAllByIdCompra(long idCompra);
}
