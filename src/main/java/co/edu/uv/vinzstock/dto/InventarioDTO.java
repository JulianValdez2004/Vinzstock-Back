package co.edu.uv.vinzstock.dto;


import co.edu.uv.vinzstock.model.ProductoModel;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioDTO {
    private long idinventario;
    private ProductoModel Producto;
    private long cantidad;

}