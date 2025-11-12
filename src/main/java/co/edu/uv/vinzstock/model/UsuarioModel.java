package co.edu.uv.vinzstock.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "TBL_USUARIOS")

public class UsuarioModel {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name ="ID_USUARIO", nullable = false, unique = true)
    private long idUsuario;

    @Column (name ="NOMBRE", nullable = false, unique = false)
    private String nombre;

    @Column (name ="NUIP")
    private long nuip;

    @Column(name = "USUARIO_LOGIN")
    private String usuarioLogin;

    @Column (name ="CONTRASENA")
    private String contrasena;


    @CreationTimestamp
    @Column (name = "FECHA_CREACION")
    private LocalDate fechaCreacion;


    @ManyToOne
    @JoinColumn(name = "ID_ROL", nullable = false)
    private RolesModel rol;

    @Column(name = "ESTADO")
    private boolean estado;

    @Column (name ="EMAIL", nullable = false)
    private String email;
}
