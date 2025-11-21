package co.edu.uv.vinzstock.service;

import co.edu.uv.vinzstock.dto.CompraDTO;
import co.edu.uv.vinzstock.model.*;
import co.edu.uv.vinzstock.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ComprasService {

    private final CompraRepository comprasRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final ProveedoresRepository proveedoresRepository;
    private final ProductoRepository productoRepository;
    private final InventarioRespository inventarioRepository;

    @Autowired
    public ComprasService(
            CompraRepository comprasRepository,
            DetalleCompraRepository detalleCompraRepository,
            ProveedoresRepository proveedoresRepository,
            ProductoRepository productoRepository,
            InventarioRespository inventarioRepository) {
        this.comprasRepository = comprasRepository;
        this.detalleCompraRepository = detalleCompraRepository;
        this.proveedoresRepository = proveedoresRepository;
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
    }

    /**
     * ✅ REGISTRAR UNA NUEVA COMPRA
     */
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

    /**
     * Actualizar el inventario del producto
     */
    /*private void actualizarInventario(ProductoModel producto, long cantidadComprada) {
        // Opción 1: Si el stock está en ProductoModel
        if (producto.getStock() != null) {
            producto.setStock(producto.getStock() + (int) cantidadComprada);
            productoRepository.save(producto);
        }

        // Opción 2: Si tienes una tabla de inventario separada
        // InventarioModel inventario = inventarioRepository.findByProductoIdProducto(producto.getIdProducto())
        //         .orElseGet(() -> {
        //             InventarioModel nuevoInventario = new InventarioModel();
        //             nuevoInventario.setProducto(producto);
        //             nuevoInventario.setCantidad(0);
        //             return nuevoInventario;
        //         });
        // inventario.setCantidad(inventario.getCantidad() + (int) cantidadComprada);
        // inventarioRepository.save(inventario);
    }

    /**
     * Obtener todas las compras
     */
    public List<ComprasModel> findAllCompras() {
        return comprasRepository.findAllByOrderByFechaDescHoraDesc();
    }

    /**
     * Obtener compra por ID
     */
    public ComprasModel findCompraById(Long id) {
        return comprasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con ID: " + id));
    }

    /**
     * Obtener compras por proveedor
     */
    public List<ComprasModel> findComprasByProveedor(Long idProveedor) {
        return comprasRepository.findByIdProveedorIdProveedor(idProveedor);
    }

    /**
     * Obtener compras por rango de fechas
     */
    public List<ComprasModel> findComprasByFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return comprasRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    /**
     * Obtener detalles de una compra
     */
    public List<DetalleCompraModel> findDetallesByCompra(Long idCompra) {
        return detalleCompraRepository.findByIdCompraIdCompra(idCompra);
    }
}