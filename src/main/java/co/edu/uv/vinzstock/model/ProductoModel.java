package co.edu.uv.vinzstock.model;


import jakarta.persistence.*;
import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "TBL_PRODUCTOS")
public class ProductoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "ID_PRODUCTO", nullable = false, unique = true)
    private long idProducto;

    @Column (name = "NOMBRE")
    private String nombre;

    @Column (name = "DESCRIPCION")
    private String descripcion;

    @Column (name = "PRECIO_VENTA")
    private Long precioVenta;

    @Column (name = "IVA")
    private long iva;

    @OneToOne(mappedBy = "producto")
    private InventarioModel inventario;

    @Transient
    public Long getCantidadInventario() {
        return inventario != null ? inventario.getCantidad() : 0L;
    }

}
