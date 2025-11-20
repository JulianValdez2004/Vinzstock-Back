package co.edu.uv.vinzstock.model;

import jakarta.persistence.*;
import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "TBL_PRODUCTOS_PROVEEDORES")
public class ProductosProveedoresModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "ID_PRODUCTOS_PROVEEDORES")
    private long idProductoProveedores;


    @ManyToOne
    @JoinColumn  (name = "ID_PROVEEDOR")
    private ProveedoresModel idProveedor;

    @ManyToOne
    @JoinColumn (name = "ID_PRODUCTO")
    private ProductoModel idProducto;

}
