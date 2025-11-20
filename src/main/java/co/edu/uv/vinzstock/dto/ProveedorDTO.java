package co.edu.uv.vinzstock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorDTO {

    private long idProveedor;

    private String nombre;

    private String telefono;


    private String nitFiscal;

    private String email;

}
