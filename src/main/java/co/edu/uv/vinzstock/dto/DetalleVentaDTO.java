package co.edu.uv.vinzstock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaDTO {
    private Long idProducto;
    private Long cantidad;
    private Long costoUnitario;
    private Long costoTotal;
}