package co.edu.uv.vinzstock.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonBackReference
    private ProductoModel producto;

    @Column (name = "CANTIDAD")
    private long cantidad;
}
