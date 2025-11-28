package co.edu.uv.vinzstock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoConInventarioDTO {
    private long id;
    private String nombre;
    private String descripcion;
    private long precioVenta;
    private long iva;
    private long cantidad;
    
    // Método para calcular precio final
    public long getPrecioFinal() {
        return precioVenta + (precioVenta * iva / 100);
    }
}