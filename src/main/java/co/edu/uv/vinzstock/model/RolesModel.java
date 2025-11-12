package co.edu.uv.vinzstock.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "TBL_ROLES")

public class RolesModel {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name ="ID_ROL")
    private long idRol;

    @Column (name ="NOMBRE")
    private String nombre;

    @Column (name ="DESCRIPCION")
    private String descripcion;

}
