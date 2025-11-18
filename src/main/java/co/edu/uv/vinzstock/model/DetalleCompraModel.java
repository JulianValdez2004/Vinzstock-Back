package co.edu.uv.vinzstock.model;

import jakarta.persistence.*;
import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "TBL_DETALLES_COMPRAS")
public class DetalleCompraModel {

    @Id
    @ManyToOne
    @JoinColumn (name = "ID_COMPRA")
    private ComprasModel idCompra;

    @ManyToOne
    @JoinColumn (name = "ID_PRODUCTO")
    private ProductoModel id_Producto;

    @Column (name = "CANTIDAD", nullable = false)
    private long cantidad;


    @Column (name = "COSTO_UNITARIO", nullable = false)
    private long costoUnitario;

    @Column (name = "COSTO_TOTAL", nullable = false)
    private long costoTotal;

}
