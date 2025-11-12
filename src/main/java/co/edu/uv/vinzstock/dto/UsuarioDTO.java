package co.edu.uv.vinzstock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private long idUsuario;
    private String nombre;
    private String usuarioLogin;
    private String email;
    private String nombreRol;
}
