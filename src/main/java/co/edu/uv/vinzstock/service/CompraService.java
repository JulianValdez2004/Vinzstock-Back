package co.edu.uv.vinzstock.service;

import co.edu.uv.vinzstock.dto.CompraDTO;
import co.edu.uv.vinzstock.dto.ProductoDTO;
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

    @Autowired
    private ProveedoresRepository proveedoresRepo;

    @Autowired
    private ProductosProveedoresRepository productosProveedoresRepo;

    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private CompraRepository comprasRepo;

    @Autowired
    private DetalleCompraRepository detalleRepo;

    /**
     * ✨ Obtener productos que vende un proveedor específico
     */
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerProductosPorProveedor(long idProveedor) {
        System.out.println("🔍 Buscando productos del proveedor: " + idProveedor);

        // 1. Verificar que el proveedor existe
        if (!proveedoresRepo.existsById(idProveedor)) {
            System.out.println("❌ Proveedor no encontrado: " + idProveedor);
            throw new RuntimeException("Proveedor no encontrado");
        }

        // 2. Obtener la relación proveedor-productos
        List<ProductosProveedoresModel> relaciones = productosProveedoresRepo
                .findByIdProveedor_IdProveedor(idProveedor);

        System.out.println("📦 Relaciones encontradas: " + relaciones.size());

        // 3. Verificar si hay productos
        if (relaciones.isEmpty()) {
            System.out.println("⚠️ No hay productos asociados al proveedor " + idProveedor);
            return new ArrayList<>(); // Retornar lista vacía en lugar de error
        }

        // 4. Extraer los productos y convertir a DTO
        List<ProductoDTO> productosDTO = new ArrayList<>();

        for (ProductosProveedoresModel relacion : relaciones) {
            try {
                ProductoModel producto = relacion.getIdProducto();

                if (producto == null) {
                    System.out.println("⚠️ Producto nulo en relación ID: " + relacion.getIdProductoProveedores());
                    continue;
                }

                ProductoDTO dto = new ProductoDTO();
                dto.setIdProducto(producto.getIdProducto());
                dto.setNombre(producto.getNombre());
                dto.setDescripcion(producto.getDescripcion());
                dto.setPrecioVenta(producto.getPrecioVenta());
                dto.setIva(producto.getIva());

                productosDTO.add(dto);
                System.out.println("✅ Producto agregado: " + producto.getNombre());

            } catch (Exception e) {
                System.out.println("❌ Error al procesar producto: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("🎯 Total productos procesados: " + productosDTO.size());
        return productosDTO;
    }

    /**
     * Registrar una nueva compra
     */
    @Transactional
    public void registrarCompra(CompraDTO dto) {
        ProveedoresModel proveedor = proveedoresRepo.findById(dto.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        ComprasModel compra = new ComprasModel();
        compra.setIdProveedor(proveedor);
        compra.setFecha(LocalDate.now());
        compra.setHora(LocalTime.now());
        compra.setValorCompra(0);

        long total = 0;
        List<DetalleCompraModel> detalles = new ArrayList<>();

        for (CompraDTO.DetalleDTO item : dto.getDetalles()) {
            // Verificar que el proveedor vende este producto
            boolean pertenece = productosProveedoresRepo
                    .existsByIdProveedor_IdProveedorAndIdProducto_IdProducto(
                            proveedor.getIdProveedor(),
                            item.getIdProducto()
                    );

            if (!pertenece) {
                throw new RuntimeException("El producto " + item.getIdProducto() +
                        " no pertenece al proveedor");
            }

            ProductoModel producto = productoRepo.findById(item.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            long costoUnitario = item.getPrecioUnitario();
            long subtotal = costoUnitario * item.getCantidad();
            total += subtotal;

            DetalleCompraModel detalle = new DetalleCompraModel();
            detalle.setIdCompra(compra);
            detalle.setId_Producto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setCostoUnitario(costoUnitario);
            detalle.setCostoTotal(subtotal);

            detalles.add(detalle);
        }

        compra.setValorCompra(total);
        comprasRepo.save(compra);

        for (DetalleCompraModel detalle : detalles) {
            detalleRepo.save(detalle);
        }
    }
}
