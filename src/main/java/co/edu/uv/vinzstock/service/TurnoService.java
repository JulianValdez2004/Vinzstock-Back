package co.edu.uv.vinzstock.service;

import co.edu.uv.vinzstock.dto.TurnoDTO;
import co.edu.uv.vinzstock.model.TurnoModel;
import co.edu.uv.vinzstock.model.UsuarioModel;
import co.edu.uv.vinzstock.repository.TurnoRepository;
import co.edu.uv.vinzstock.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TurnoService(TurnoRepository turnoRepository, 
                        UsuarioRepository usuarioRepository,
                        JdbcTemplate jdbcTemplate) {
        this.turnoRepository = turnoRepository;
        this.usuarioRepository = usuarioRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Verificar si un usuario tiene un turno activo
     */
    public boolean tieneTurnoActivo(long idUsuario) {
        return turnoRepository.existeTurnoActivoByUsuario(idUsuario);
    }

    /**
     * Obtener turno activo de un usuario
     */
    public Optional<TurnoModel> obtenerTurnoActivo(long idUsuario) {
        return turnoRepository.findTurnoActivoByUsuario(idUsuario);
    }

    /**
     * Abrir un nuevo turno
     */
    @Transactional
    public TurnoModel abrirTurno(TurnoDTO.AbrirTurnoDTO dto) {
        // 1. Validar que el usuario existe
        UsuarioModel usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getIdUsuario()));

        // 2. Validar que NO tenga un turno activo
        if (tieneTurnoActivo(dto.getIdUsuario())) {
            throw new RuntimeException("El usuario ya tiene un turno activo. Debe cerrar el turno actual antes de abrir uno nuevo.");
        }

        // 3. Validar base inicial
        if (dto.getBaseInicial() < 0) {
            throw new IllegalArgumentException("La base inicial no puede ser negativa");
        }

        // 4. Crear el turno
        TurnoModel turno = new TurnoModel();
        turno.setUsuario(usuario);
        turno.setFechaInicio(LocalDate.now());
        turno.setHoraInicio(LocalTime.now());
        turno.setBaseInicial(dto.getBaseInicial());
        turno.setEstado(true);  // ABIERTO

        // 5. Guardar
        return turnoRepository.save(turno);
    }

    /**
     * Cerrar un turno existente
     */
    @Transactional
    public TurnoModel cerrarTurno(TurnoDTO.CerrarTurnoDTO dto) {
        // 1. Buscar el turno
        TurnoModel turno = turnoRepository.findById(dto.getIdTurno())
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con ID: " + dto.getIdTurno()));

        // 2. Validar que el turno esté abierto
        if (!turno.isEstado()) {
            throw new RuntimeException("El turno ya está cerrado");
        }

        // 3. Validar base final
        if (dto.getBaseFinal() < 0) {
            throw new IllegalArgumentException("La base final no puede ser negativa");
        }

        // 4. Calcular total de ventas del turno
        double totalVentas = calcularTotalVentas(turno);

        // 5. Calcular total en caja
        double totalCaja = dto.getBaseFinal() + totalVentas;

        // 6. Actualizar el turno
        turno.setFechaFin(LocalDate.now());
        turno.setHoraFin(LocalTime.now());
        turno.setBaseFinal(dto.getBaseFinal());
        turno.setTotalVentas(totalVentas);
        turno.setTotalCaja(totalCaja);
        turno.setEstado(false);  // CERRADO

        // 7. Guardar
        return turnoRepository.save(turno);
    }

    /**
     * Calcular total de ventas del turno desde la tabla ventas
     * Suma todos los valores_total de las ventas realizadas durante el turno
     */
    private double calcularTotalVentas(TurnoModel turno) {
        String sql = """
            SELECT COALESCE(SUM(v.valor_venta), 0) 
            FROM tbl_ventas v 
            WHERE v.id_usuario = ? 
              AND (v.fecha + v.hora) >= ? 
              AND (v.fecha + v.hora) <= ?
        """;

        LocalDate fechaInicio = turno.getFechaInicio();
        LocalDate fechaFin = LocalDate.now();  // Fecha actual al cerrar

        Double total = jdbcTemplate.queryForObject(
            sql, 
            Double.class, 
            turno.getUsuario().getIdUsuario(),
            fechaInicio,
            fechaFin
        );

        return total != null ? total : 0.0;
    }

    /**
     * Obtener historial de turnos de un usuario
     */
    public List<TurnoModel> obtenerHistorialTurnos(long idUsuario) {
        return turnoRepository.findAllByUsuario(idUsuario);
    }

    /**
     * Obtener turno por ID
     */
    public Optional<TurnoModel> obtenerTurnoPorId(long idTurno) {
        return turnoRepository.findById(idTurno);
    }

    /**
     * Obtener turnos cerrados de un usuario
     */
    public List<TurnoModel> obtenerTurnosCerrados(long idUsuario) {
        return turnoRepository.findTurnosCerradosByUsuario(idUsuario);
    }

    /**
     * Obtener todos los turnos activos (para admin - futuro)
     */
    public List<TurnoModel> obtenerTodosLosActivosTurnos() {
        return turnoRepository.findAllTurnosActivos();
    }
}
