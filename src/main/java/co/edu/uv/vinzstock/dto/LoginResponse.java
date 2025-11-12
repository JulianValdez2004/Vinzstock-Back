package co.edu.uv.vinzstock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private UsuarioDTO usuario;

    // Constructor sin token (para mantener compatibilidad)
    public LoginResponse(boolean success, String message, UsuarioDTO usuario) {
        this.success = success;
        this.message = message;
        this.usuario = usuario;
        this.token = null;
    }
}
