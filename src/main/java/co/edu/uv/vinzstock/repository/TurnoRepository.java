package co.edu.uv.vinzstock.repository;

import co.edu.uv.vinzstock.model.TurnoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoRepository extends JpaRepository<TurnoModel, Long> {

    /**
     * Buscar turno activo (abierto) por usuario
     * Solo debe haber uno por usuario
     */
    @Query("SELECT t FROM TurnoModel t WHERE t.usuario.idUsuario = :idUsuario AND t.estado = true")
    Optional<TurnoModel> findTurnoActivoByUsuario(@Param("idUsuario") long idUsuario);

    /**
     * Verificar si existe un turno activo para un usuario
     */
    @Query("SELECT COUNT(t) > 0 FROM TurnoModel t WHERE t.usuario.idUsuario = :idUsuario AND t.estado = true")
    boolean existeTurnoActivoByUsuario(@Param("idUsuario") long idUsuario);

    /**
     * Obtener todos los turnos de un usuario (historial)
     */
    @Query("SELECT t FROM TurnoModel t WHERE t.usuario.idUsuario = :idUsuario ORDER BY t.fechaInicio DESC, t.horaInicio DESC")
    List<TurnoModel> findAllByUsuario(@Param("idUsuario") long idUsuario);

    /**
     * Obtener turnos cerrados de un usuario
     */
    @Query("SELECT t FROM TurnoModel t WHERE t.usuario.idUsuario = :idUsuario AND t.estado = false ORDER BY t.fechaInicio DESC")
    List<TurnoModel> findTurnosCerradosByUsuario(@Param("idUsuario") long idUsuario);

    /**
     * Obtener turnos por rango de fechas
     */
    @Query("SELECT t FROM TurnoModel t WHERE t.fechaInicio BETWEEN :fechaInicio AND :fechaFin ORDER BY t.fechaInicio DESC")
    List<TurnoModel> findTurnosByFechaRange(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    /**
     * Obtener todos los turnos activos (para admin - futuro)
     */
    @Query("SELECT t FROM TurnoModel t WHERE t.estado = true ORDER BY t.fechaInicio DESC, t.horaInicio DESC")
    List<TurnoModel> findAllTurnosActivos();
}
