package co.edu.uv.vinzstock.service;

import co.edu.uv.vinzstock.model.RolesModel;
import co.edu.uv.vinzstock.model.UsuarioModel;
import co.edu.uv.vinzstock.repository.RolRespository;
import co.edu.uv.vinzstock.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRespository rolRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public UsuarioService (UsuarioRepository usuarioRepository, RolRespository rolRespository, JavaMailSender mailSender){
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRespository;
        this.mailSender = mailSender;
    }

    private static final int MAX_INTENTOS = 5;
    private static final int MAX_BLOQUEOS = 3;
    private static final int MINUTOS_BLOQUEO = 1; // Cambia este valor para pruebas

    public UsuarioModel authenticateUsuario(String usuarioLogin, String contrasena) {
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByUsuarioLogin(usuarioLogin);

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        UsuarioModel usuario = usuarioOpt.get();

        if (!usuario.isEstado()) {
            throw new RuntimeException("Usuario inactivo, por favor comuníquese con el administrador.");
        }

        if (usuario.isBloqueoPermanente()) {
            throw new RuntimeException("Sistema bloqueado permanentemente. Contacta con un desarrollador.");
        }

        if (usuario.getBloqueadoHasta() != null && usuario.getBloqueadoHasta().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Inicio de sesión bloqueado temporalmente. Inténtalo más tarde.");
        }

        if (!usuario.getContrasena().equals(contrasena)) {
            usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);

            if (usuario.getIntentosFallidos() >= MAX_INTENTOS) {
                usuario.setBloqueadoHasta(LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEO));
                usuario.setIntentosFallidos(0);
                usuario.setCantidadBloqueos(usuario.getCantidadBloqueos() + 1);

                if (usuario.getCantidadBloqueos() >= MAX_BLOQUEOS) {
                    usuario.setBloqueoPermanente(true);
                }
            }

            usuarioRepository.save(usuario);
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        // Login exitoso
        usuario.setIntentosFallidos(0);
        usuario.setCantidadBloqueos(0);
        usuario.setBloqueadoHasta(null);
        usuario.setBloqueoPermanente(false);
        usuarioRepository.save(usuario);

        return usuario;
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

    public Optional<UsuarioModel> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public void sendPasswordByEmail(String toEmail, String password) {
        // Fuerza el protocolo TLS correcto
        System.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
        System.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Recuperación de contraseña - Vinzstock");
        message.setText("Hola,\n\nTu contraseña actual es: " + password +
                "\n\nRecuerda mantenerla segura.\n\nVinzstock");

        mailSender.send(message);
    }
}
