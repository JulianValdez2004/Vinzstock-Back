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
    private String nombreCompania;

    // Tipo identificación fiscal – NIT o RUC
    @Column(name = "TIPO_IDENTIFICACION", nullable = false, length = 10)
    private String tipoIdentificacion;

    @Column (name = "NIT_FISCAL", nullable = false, unique = true)
    private String nitFiscal;

    @Column (name = "TELEFONO", unique = false)
    private String numeroContacto;

    @Column (name = "EMAIL", nullable = false, unique = true)
    private String email;

    // Lista de productos → nombre máx 20, descripción máx 120
    @Column(name = "PRODUCTO_NOMBRE", length = 20)
    private String productoNombre;

    @Column(name = "PRODUCTO_DESCRIPCION", length = 120)
    private String productoDescripcion;

    // Forma de pago – obligatorio (contra entrega, tarjeta, consignación)
    @Column(name = "FORMA_PAGO", nullable = false, length = 20)
    private String formaPago;

    // Horarios de pedido / fecha de envío – obligatorio
    @Column(name = "FECHA_PEDIDO", nullable = false)
    private String fechaPedido;

}
