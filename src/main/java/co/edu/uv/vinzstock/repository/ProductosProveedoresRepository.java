package co.edu.uv.vinzstock.repository;


import co.edu.uv.vinzstock.model.ProductoModel;
import co.edu.uv.vinzstock.model.ProductosProveedoresModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductosProveedoresRepository extends JpaRepository<ProductosProveedoresModel, Long> {

    List<ProductosProveedoresModel> findAllByIdProductoProveedores(long idProductoProveedores);
}
