package co.edu.uv.vinzstock.repository;

import co.edu.uv.vinzstock.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    List<UsuarioModel> findAllByIdUsuario (long idUsuario);
    List<UsuarioModel> findAllByNombreContains (String nombre);

    Optional<UsuarioModel> findByUsuarioLoginAndContrasena(String usuarioLogin, String contrasena);


    boolean existsByNuip(long nuip);
    boolean existsByUsuarioLogin(String usuarioLogin);
    boolean existsByEmail(String email);

    Optional<UsuarioModel> findByEmail(String email);

    Optional<UsuarioModel> findByNuip(long nuip);

    Optional<UsuarioModel> findByUsuarioLogin(String usuarioLogin);

}
