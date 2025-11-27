package co.edu.uv.vinzstock.model;

import lombok.*;
import jakarta.persistence.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TBL_CLIENTES")
public class ClienteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "ID_CLIENTE", nullable = false, unique = true)
    private long idCliente;

    @Column (name = "NOMBRE_RAZON_SOCIAL", nullable = false, length = 100)
    private String nombreRazonSocial;

    @Column (name = "NUMERO_DOCUMENTO", nullable = false, unique = true)
    private String numeroDocumento;

    @Column (name = "ESTADO")
    private boolean estado;
}