package co.edu.uv.vinzstock.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TBL_DETALLES_VENTAS")
public class DetallesVentasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE_VENTA", unique = true)
    private long idDetalleVenta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_VENTA")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private VentasModel venta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PRODUCTO")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProductoModel producto;

    @Column(name = "CANTIDAD", nullable = false)
    private long cantidad;

    @Column(name = "COSTO_UNITARIO", nullable = false)
    private long costoUnitario;

    @Column(name = "COSTO_TOTAL", nullable = false)
    private long costoTotal;
}