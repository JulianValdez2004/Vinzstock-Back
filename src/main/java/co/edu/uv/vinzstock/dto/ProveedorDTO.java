package co.edu.uv.vinzstock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorDTO {

    private long idProveedor;

    private String nombreCompania;

    private String numeroContacto;

    private String tipoIdentificacion;

    private String nitFiscal;

    private String email;

    private String productoNombre;

    private String productoDescripcion;

    private String formaPago;

    private String fechaPedido;
}
