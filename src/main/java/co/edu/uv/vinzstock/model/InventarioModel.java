package co.edu.uv.vinzstock.model;

import jakarta.persistence.*;
import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "TBL_INVENTARIOS")

public class InventarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "ID_INVENTARIO")
    private long idInventario;

    @ManyToOne
    @JoinColumn (name = "ID_PRODUCTO")
    private ProductoModel producto;

    @Column (name = "CANTIDAD")
    private long cantidad;
}
