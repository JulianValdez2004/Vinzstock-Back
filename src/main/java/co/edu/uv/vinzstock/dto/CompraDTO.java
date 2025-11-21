package co.edu.uv.vinzstock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompraDTO {
    private long idProveedor;
    private List<DetalleDTO> detalles;

    @Data
    public static class DetalleDTO {
        private long idProducto;
        private long cantidad;
        private long precioUnitario;
    }
}




