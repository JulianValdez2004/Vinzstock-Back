package co.edu.uv.vinzstock.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TBL_VENTAS")
public class VentasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VENTA", nullable = false, unique = true)
    private long idVenta;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private UsuarioModel usuario;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE", nullable = false)
    private ClienteModel cliente;

    @CreationTimestamp
    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @CreationTimestamp
    @Column(name = "HORA", nullable = false)
    private LocalTime hora;

    @Column(name = "VALOR_VENTA", nullable = false)
    private long valorVenta;

}
