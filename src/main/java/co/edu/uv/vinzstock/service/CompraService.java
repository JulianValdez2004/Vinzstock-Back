package co.edu.uv.vinzstock.service;

import co.edu.uv.vinzstock.dto.CompraDTO;
import co.edu.uv.vinzstock.dto.CompraResponseDTO;
import co.edu.uv.vinzstock.model.*;
import co.edu.uv.vinzstock.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompraService {

    private final CompraRepository comprasRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final ProveedoresRepository proveedoresRepository;
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;

    @Autowired
    public CompraService(
            CompraRepository comprasRepository,
            DetalleCompraRepository detalleCompraRepository,
            ProveedoresRepository proveedoresRepository,
            ProductoRepository productoRepository,
            InventarioRepository inventarioRepository) {
        this.comprasRepository = comprasRepository;
        this.detalleCompraRepository = detalleCompraRepository;
        this.proveedoresRepository = proveedoresRepository;
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
    }

    // ========================================
    // MÉTODOS EXISTENTES (mantenidos)
    // ========================================

    @Transactional
    public ComprasModel registrarCompra(CompraDTO compraDTO) {
        // 1. Validar que el proveedor exista
        ProveedoresModel proveedor = proveedoresRepository.findById(compraDTO.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + compraDTO.getIdProveedor()));

        // 2. Validar que haya detalles
        if (compraDTO.getDetalles() == null || compraDTO.getDetalles().isEmpty()) {
            throw new RuntimeException("La compra debe tener al menos un producto");
        }

        // 3. Calcular el valor total de la compra
        long valorTotal = 0;
        for (CompraDTO.DetalleDTO detalle : compraDTO.getDetalles()) {
            valorTotal += detalle.getCantidad() * detalle.getPrecioUnitario();
        }

        // 4. Crear la compra
        ComprasModel compra = ComprasModel.builder()
                .idProveedor(proveedor)
                .fecha(LocalDate.now())
                .valorCompra(valorTotal)
                .build();

        // 5. Guardar la compra
        ComprasModel compraGuardada = comprasRepository.save(compra);

        // 6. Guardar los detalles y actualizar inventario
        for (CompraDTO.DetalleDTO detalleDTO : compraDTO.getDetalles()) {
            // Obtener el producto
            ProductoModel producto = productoRepository.findById(detalleDTO.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalleDTO.getIdProducto()));

            // Calcular costo total del detalle
            long costoTotal = detalleDTO.getCantidad() * detalleDTO.getPrecioUnitario();

            // Crear el detalle de compra
            DetalleCompraModel detalle = DetalleCompraModel.builder()
                    .idCompra(compraGuardada)
                    .idProducto(producto)  // Asegúrate que en tu modelo se llame idProducto
                    .cantidad(detalleDTO.getCantidad())
                    .costoUnitario(detalleDTO.getPrecioUnitario())
                    .costoTotal(costoTotal)
                    .build();

            detalleCompraRepository.save(detalle);

            // ✅ Actualizar el stock del producto (sumar la cantidad comprada)
            /*actualizarInventario(producto, detalleDTO.getCantidad());*/
        }

        return compraGuardada;
    }

    public List<ComprasModel> findAllCompras() {
        return comprasRepository.findAllByOrderByFechaDescHoraDesc();
    }

    public ComprasModel findCompraById(Long id) {
        return comprasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
    }

    public List<DetalleCompraModel> findDetallesByCompra(Long idCompra) {
        return detalleCompraRepository.findByIdCompraIdCompra(idCompra);
    }

    public List<ComprasModel> findComprasByProveedor(Long idProveedor) {
        return comprasRepository.findByIdProveedorIdProveedor(idProveedor);
    }


    /**
     * Obtener TODAS las compras de un proveedor (historial completo)
     */
    @Transactional(readOnly = true)
    public List<CompraResponseDTO> obtenerHistorialComprasPorProveedor(Long idProveedor) {
        System.out.println("📋 Obteniendo historial completo del proveedor: " + idProveedor);

        if (!proveedoresRepository.existsById(idProveedor)) {
            throw new RuntimeException("Proveedor no encontrado");
        }

        List<ComprasModel> compras = comprasRepository.findAllByProveedorOrderByFechaDesc(idProveedor);
        System.out.println("📦 Total de compras encontradas: " + compras.size());

        long pendientes = compras.stream().filter(c -> "Pendiente".equals(c.getEstado())).count();
        long recibidas = compras.stream().filter(c -> "Recibido".equals(c.getEstado())).count();
        long canceladas = compras.stream().filter(c -> "Cancelado".equals(c.getEstado())).count();
        System.out.println("  ⏳ Pendientes: " + pendientes);
        System.out.println("  ✅ Recibidas: " + recibidas);
        System.out.println("  ❌ Canceladas: " + canceladas);

        return compras.stream()
                .map(this::convertirACompraResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     *  Obtener compras por estado específico de un proveedor
     */
    @Transactional(readOnly = true)
    public List<CompraResponseDTO> obtenerComprasPorProveedorYEstado(Long idProveedor, String estado) {
        System.out.println("📋 Buscando compras del proveedor " + idProveedor + " con estado: " + estado);

        if (!proveedoresRepository.existsById(idProveedor)) {
            throw new RuntimeException("Proveedor no encontrado");
        }

        List<ComprasModel> compras = comprasRepository.findByProveedorAndEstado(idProveedor, estado);
        System.out.println("📦 Compras encontradas: " + compras.size());

        return compras.stream()
                .map(this::convertirACompraResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Confirmar recepción de una compra (actualiza inventario)
     */
    @Transactional
    public void confirmarRecepcionCompra(Long idCompra) {
        System.out.println("✅ Confirmando recepción de compra: " + idCompra);

        // 1. Obtener la compra
        ComprasModel compra = comprasRepository.findById(idCompra)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        // 2. Verificar que esté en estado Pendiente
        if (!"Pendiente".equals(compra.getEstado())) {
            throw new RuntimeException("Solo se pueden confirmar compras en estado Pendiente. Estado actual: " + compra.getEstado());
        }

        // 3. Obtener los detalles de la compra
        List<DetalleCompraModel> detalles = detalleCompraRepository.findByIdCompraIdCompra(idCompra);
        System.out.println("📦 Actualizando inventario para " + detalles.size() + " productos");

        // 4. Actualizar el inventario (sumar cantidades)
        for (DetalleCompraModel detalle : detalles) {
            long idProducto = detalle.getIdProducto().getIdProducto();
            long cantidadRecibida = detalle.getCantidad();

            // ⭐ CORREGIDO: Buscar por producto usando el nuevo método
            InventarioModel inventario = inventarioRepository.findByProductoId(idProducto)
                    .orElse(null);

            if (inventario != null) {
                // Ya existe, sumar cantidad
                long cantidadAnterior = inventario.getCantidad();
                long nuevaCantidad = cantidadAnterior + cantidadRecibida;
                inventario.setCantidad(nuevaCantidad); // ⭐ CORREGIDO: long, no int
                inventarioRepository.save(inventario);
                System.out.println("  ✅ Producto " + idProducto + ": " + cantidadAnterior + " → " + nuevaCantidad);
            } else {
                // No existe, crear nuevo registro
                InventarioModel nuevoInventario = new InventarioModel();
                nuevoInventario.setProducto(detalle.getIdProducto()); // ⭐ CORREGIDO: setProducto, no setIdProducto
                nuevoInventario.setCantidad(cantidadRecibida); // ⭐ CORREGIDO: long
                inventarioRepository.save(nuevoInventario);
                System.out.println("  ✅ Producto " + idProducto + ": Nuevo en inventario con " + cantidadRecibida + " unidades");
            }
        }

        // 5. Cambiar el estado de la compra a "Recibido"
        compra.setEstado("Recibido");
        comprasRepository.save(compra);

        System.out.println("✅ Compra confirmada exitosamente. Estado: Recibido");
    }

    /**
     * ❌ Cancelar una compra (solo si está Pendiente)
     */
    @Transactional
    public void cancelarCompra(Long idCompra) {
        System.out.println("❌ Cancelando compra: " + idCompra);

        ComprasModel compra = comprasRepository.findById(idCompra)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        if (!"Pendiente".equals(compra.getEstado())) {
            throw new RuntimeException("Solo se pueden cancelar compras en estado Pendiente. Estado actual: " + compra.getEstado());
        }

        compra.setEstado("Cancelado");
        comprasRepository.save(compra);

        System.out.println("❌ Compra cancelada exitosamente");
    }

    /**
     * 🗑️ Eliminar una compra completamente (borrar de BD)
     */
    @Transactional
    public void eliminarCompra(Long idCompra) {
        System.out.println("🗑️ Eliminando compra: " + idCompra);

        ComprasModel compra = comprasRepository.findById(idCompra)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        if ("Recibido".equals(compra.getEstado())) {
            throw new RuntimeException("No se pueden eliminar compras ya recibidas (el inventario fue actualizado)");
        }

        comprasRepository.delete(compra);
        System.out.println("🗑️ Compra eliminada de la base de datos");
    }

    /**
     * 🗑️ Borrar TODO el historial de un proveedor
     */
    @Transactional
    public void borrarHistorialCompleto(Long idProveedor) {
        System.out.println("🗑️ ADVERTENCIA: Borrando TODO el historial del proveedor: " + idProveedor);

        if (!proveedoresRepository.existsById(idProveedor)) {
            throw new RuntimeException("Proveedor no encontrado");
        }

        Long totalCompras = comprasRepository.countByProveedor(idProveedor);
        System.out.println("⚠️ Se eliminarán " + totalCompras + " compras");

        comprasRepository.deleteByIdProveedorIdProveedor(idProveedor);

        System.out.println("🗑️ Historial completo eliminado");
    }

    /**
     * 📊 Obtener estadísticas generales de compras
     */
    public CompraEstadisticasDTO obtenerEstadisticas() {
        long pendientes = comprasRepository.countByEstado("Pendiente");
        long recibidas = comprasRepository.countByEstado("Recibido");
        long canceladas = comprasRepository.countByEstado("Cancelado");

        CompraEstadisticasDTO stats = new CompraEstadisticasDTO();
        stats.setTotalPendientes(pendientes);
        stats.setTotalRecibidas(recibidas);
        stats.setTotalCanceladas(canceladas);

        return stats;
    }

    /**
     * 📊 Obtener estadísticas de un proveedor específico
     */
    public CompraEstadisticasProveedorDTO obtenerEstadisticasProveedor(Long idProveedor) {
        if (!proveedoresRepository.existsById(idProveedor)) {
            throw new RuntimeException("Proveedor no encontrado");
        }

        long pendientes = comprasRepository.countByProveedorAndEstado(idProveedor, "Pendiente");
        long recibidas = comprasRepository.countByProveedorAndEstado(idProveedor, "Recibido");
        long canceladas = comprasRepository.countByProveedorAndEstado(idProveedor, "Cancelado");

        CompraEstadisticasProveedorDTO stats = new CompraEstadisticasProveedorDTO();
        stats.setIdProveedor(idProveedor);
        stats.setTotalPendientes(pendientes);
        stats.setTotalRecibidas(recibidas);
        stats.setTotalCanceladas(canceladas);

        return stats;
    }

    // ========================================
    // MÉTODOS PRIVADOS DE CONVERSIÓN
    // ========================================

    private CompraResponseDTO convertirACompraResponseDTO(ComprasModel compra) {
        List<DetalleCompraModel> detalles = detalleCompraRepository.findByIdCompraIdCompra(compra.getIdCompra());

        return CompraResponseDTO.builder()
                .idCompra(compra.getIdCompra())
                .idProveedor(compra.getIdProveedor().getIdProveedor())
                .nombreProveedor(compra.getIdProveedor().getNombre())
                .nitProveedor(compra.getIdProveedor().getNitFiscal())
                .fecha(compra.getFecha())
                .hora(compra.getHora())
                .valorCompra(compra.getValorCompra())
                .estado(compra.getEstado())
                .detalles(detalles.stream()
                        .map(this::convertirADetalleResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private CompraResponseDTO.DetalleCompraResponseDTO convertirADetalleResponseDTO(DetalleCompraModel detalle) {
        return CompraResponseDTO.DetalleCompraResponseDTO.builder()
                .idDetalleCompra(detalle.getIdDetalleCompra())
                .idProducto(detalle.getIdProducto().getIdProducto())
                .nombreProducto(detalle.getIdProducto().getNombre())
                .descripcionProducto(detalle.getIdProducto().getDescripcion())
                .cantidad(detalle.getCantidad())
                .costoUnitario(detalle.getCostoUnitario())
                .costoTotal(detalle.getCostoTotal())
                .build();
    }
}

// ========================================
// DTOs AUXILIARES PARA ESTADÍSTICAS
// ========================================

@lombok.Data
class CompraEstadisticasDTO {
    private long totalPendientes;
    private long totalRecibidas;
    private long totalCanceladas;

    public long getTotal() {
        return totalPendientes + totalRecibidas + totalCanceladas;
    }
}

@lombok.Data
class CompraEstadisticasProveedorDTO {
    private long idProveedor;
    private long totalPendientes;
    private long totalRecibidas;
    private long totalCanceladas;

    public long getTotal() {
        return totalPendientes + totalRecibidas + totalCanceladas;
    }
}