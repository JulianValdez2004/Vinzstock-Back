package co.edu.uv.vinzstock.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "TBL_TURNOS")


public class TurnosModel {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "ID_TURNOS")
    private long idTurno;

    @Column (name = "NOMBRE")
    private String nombre;
}
