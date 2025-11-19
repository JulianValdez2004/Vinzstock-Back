package co.edu.uv.vinzstock.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(name = "INTENTOS_FALLIDOS")
    private int intentosFallidos;

    @Column(name = "BLOQUEADO_HASTA")
    private LocalDateTime bloqueadoHasta;

    @Column(name = "CANTIDAD_BLOQUEOS")
    private int cantidadBloqueos;

    @Column(name = "BLOQUEO_PERMANENTE")
    private boolean bloqueoPermanente;



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
