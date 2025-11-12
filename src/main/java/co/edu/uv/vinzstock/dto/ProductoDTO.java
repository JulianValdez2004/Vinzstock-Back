package co.edu.uv.vinzstock.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private long idProducto;
    private String nombre;
    private String descripcion;
    private long precioVenta;
    private long iva;
}
