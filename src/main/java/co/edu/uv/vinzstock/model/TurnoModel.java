
package co.edu.uv.vinzstock.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "TBL_TURNOS")


public class TurnoModel {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "ID_TURNOS")
    private long idTurno;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private UsuarioModel usuario;
    
    // ========================================
    // DATOS DE APERTURA
    // ========================================
    @Column(name = "FECHA_INICIO", nullable = false)
    private LocalDate fechaInicio;
    
    @Column(name = "HORA_INICIO", nullable = false)
    private LocalTime horaInicio;
    
    @Column(name = "BASE_INICIAL", nullable = false)
    private double baseInicial;
    
    // ========================================
    // DATOS DE CIERRE (NULL mientras está abierto)
    // ========================================
    @Column(name = "FECHA_FIN")
    private LocalDate fechaFin;
    
    @Column(name = "HORA_FIN")
    private LocalTime horaFin;
    
    @Column(name = "BASE_FINAL")
    private Double baseFinal;
    
    @Column(name = "TOTAL_VENTAS")
    private Double totalVentas;
    
    @Column(name = "TOTAL_CAJA")
    private Double totalCaja;
    
    // ========================================
    // ESTADO Y AUDITORÍA
    // ========================================
    @Column(name = "ESTADO", nullable = false)
    private boolean estado = true;  // true=ABIERTO, false=CERRADO
    
    @Column(name = "FECHA_REGISTRO", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;
    
    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }

}
