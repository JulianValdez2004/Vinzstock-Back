package co.edu.uv.vinzstock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaDTO {
    private Long idUsuario;
    private Long idCliente;
    private Long valorVenta;
    private List<DetalleVentaDTO> detalles;
}