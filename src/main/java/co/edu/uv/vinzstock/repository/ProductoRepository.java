package co.edu.uv.vinzstock.repository;


import co.edu.uv.vinzstock.model.ProductoModel;
import co.edu.uv.vinzstock.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository <ProductoModel, Long> {


    List<ProductoModel> findAllByIdProducto(long idProducto);

    List<ProductoModel> findAllByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<ProductoModel> findAllByNombreContains(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    Optional<ProductoModel> findByNombreIgnoreCase(String nombre);

    List<ProductoModel> findByNombreContainingIgnoreCase(String nombre);
}
