package co.edu.uv.vinzstock.service;

import co.edu.uv.vinzstock.model.RolesModel;
import co.edu.uv.vinzstock.model.UsuarioModel;
import co.edu.uv.vinzstock.repository.RolRespository;
import co.edu.uv.vinzstock.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRespository rolRepository;

    @Autowired
    public UsuarioService (UsuarioRepository usuarioRepository, RolRespository rolRespository){
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRespository;
    }

    public UsuarioModel authenticateUsuario(String usuarioLogin, String contrasena) {
        Optional<UsuarioModel> usuario = usuarioRepository.findByUsuarioLoginAndContrasena(usuarioLogin, contrasena);

        if (usuario.isPresent()) {
            UsuarioModel user = usuario.get();

            // Verificar si el usuario está activo
            if (!user.isEstado()) {
                throw new RuntimeException("Usuario inactivo, por favor comuniquese con el administrador.");
            }

            return user;
        } else {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
    }


    public class UsuarioDuplicadoException extends RuntimeException {
        public UsuarioDuplicadoException(String campo, String valor) {
            super("Ya existe un usuario con el " + campo + ": " + valor);
        }
    }


    public UsuarioModel createUsuario (UsuarioModel usuarioModel){
        if (usuarioModel.getRol() != null && usuarioModel.getRol().getIdRol() > 0) {
            Long idRol = usuarioModel.getRol().getIdRol();

            RolesModel rol = rolRepository.findById(idRol)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + idRol));

            usuarioModel.setRol(rol);
        } else {
            throw new RuntimeException("Debe especificar un rol válido");
        }

        if (usuarioRepository.existsByNuip(usuarioModel.getNuip())){
            throw new UsuarioDuplicadoException("NUIP", usuarioModel.getUsuarioLogin());

        }

        if (usuarioRepository.existsByEmail(usuarioModel.getEmail())){
            throw new UsuarioDuplicadoException("EMAIL", usuarioModel.getUsuarioLogin());

        }

        if (usuarioRepository.existsByUsuarioLogin(usuarioModel.getUsuarioLogin())){
            throw new UsuarioDuplicadoException("USERNAME", usuarioModel.getUsuarioLogin());

        }
        return this.usuarioRepository.save(usuarioModel);
    }

    public UsuarioModel updateUsuario(UsuarioModel usuarioModel){

        if (usuarioModel.getRol() != null && usuarioModel.getRol().getIdRol() > 0) {
            Long idRol = usuarioModel.getRol().getIdRol();

            RolesModel rol = rolRepository.findById(idRol)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + idRol));

            usuarioModel.setRol(rol);
        }
        return this.usuarioRepository.save(usuarioModel);
    }

    /*public void deleteUsuario(Long id){
        this.usuarioRepository.deleteById(id);
    }*/

    public List<UsuarioModel> findAllUsuarios(){
        return this.usuarioRepository.findAll();
    }

    public List<UsuarioModel> findAllUsuariosByNombre (String nombre){
        return this.usuarioRepository.findAllByNombreContains(nombre);
    }

    public List<UsuarioModel> findAllByUsuarios (long idUsuario){
        return this.usuarioRepository.findAllByIdUsuario(idUsuario);
    }

}
