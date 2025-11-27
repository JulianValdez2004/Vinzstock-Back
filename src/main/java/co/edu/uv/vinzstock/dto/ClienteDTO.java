package co.edu.uv.vinzstock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    
    private long idCliente;
    private String nombreRazonSocial;
    private String numeroDocumento;
    private Boolean estado;
}
