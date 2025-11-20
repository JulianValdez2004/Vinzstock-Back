package co.edu.uv.vinzstock.model;

import jakarta.persistence.*;
import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "TBL_PROVEEDORES")
public class ProveedoresModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "ID_PROVEEDOR", nullable = false, unique = true)
    private long idProveedor;

    @Column (name = "NOMBRE", nullable = false)
    private String nombre;

    @Column (name = "NIT_FISCAL", nullable = false, unique = true)
    private String nitFiscal;


    @Column (name = "TELEFONO", unique = true)
    private String telefono;

    @Column (name = "EMAIL", nullable = false, unique = true)
    private String email;

}
