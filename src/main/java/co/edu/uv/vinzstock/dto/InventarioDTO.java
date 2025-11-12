package co.edu.uv.vinzstock.dto;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioDTO {
    private long idinventario;
    private String nombreProducto;
    private long cantidad;

}