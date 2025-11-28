package co.edu.uv.vinzstock.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TBL_COMPRAS")
public class ComprasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COMPRA", nullable = false, unique = true)
    private long idCompra;

    @ManyToOne
    @JoinColumn(name = "ID_PROVEEDOR", nullable = false)
    private ProveedoresModel idProveedor;

    @CreationTimestamp
    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @CreationTimestamp
    @Column(name = "HORA", nullable = false)
    private LocalTime hora;

    @Column(name = "VALOR_COMPRA", nullable = false)
    private long valorCompra;

    @Column(name = "ESTADO", nullable = false, length = 20)
    @Builder.Default
    private String estado = "Pendiente"; // "Pendiente", "Recibido", "Cancelado"
    
    @OneToMany(mappedBy = "idCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCompraModel> detalles;
}
