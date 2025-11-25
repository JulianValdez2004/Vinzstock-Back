package co.edu.uv.vinzstock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompraResponseDTO {

    private long idCompra;
    private long idProveedor;
    private String nombreProveedor;
    private String nitProveedor;
    private LocalDate fecha;
    private LocalTime hora;
    private long valorCompra;
    private String estado;
    private List<DetalleCompraResponseDTO> detalles;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetalleCompraResponseDTO {
        private long idDetalleCompra;
        private long idProducto;
        private String nombreProducto;
        private String descripcionProducto;
        private long cantidad;
        private long costoUnitario;
        private long costoTotal;
    }
}