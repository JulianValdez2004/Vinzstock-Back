package co.edu.uv.vinzstock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class TurnoDTO {

    /**
     * DTO para abrir turno
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AbrirTurnoDTO {
        private long idUsuario;
        private double baseInicial;
    }

    /**
     * DTO para cerrar turno
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CerrarTurnoDTO {
        private long idTurno;
        private double baseFinal;
        // totalVentas se calcula automáticamente desde las ventas
    }
}