package co.edu.uv.vinzstock.repository;

import co.edu.uv.vinzstock.model.RolesModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface RolRespository extends JpaRepository<RolesModel, Long>{
    List<RolesModel> findAllByIdRol(long idRol);
}
