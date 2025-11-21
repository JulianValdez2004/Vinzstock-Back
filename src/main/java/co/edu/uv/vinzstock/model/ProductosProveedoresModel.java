package co.edu.uv.vinzstock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "TBL_PRODUCTOS_PROVEEDORES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductosProveedoresModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODUCTOS_PROVEEDORES")
    private long idProductoProveedores;

    // ⭐ ESTO ES LO IMPORTANTE: fetch = FetchType.EAGER
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PROVEEDOR")
    private ProveedoresModel idProveedor;

    // ⭐ ESTO ES LO IMPORTANTE: fetch = FetchType.EAGER
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PRODUCTO")
    private ProductoModel idProducto;
}
