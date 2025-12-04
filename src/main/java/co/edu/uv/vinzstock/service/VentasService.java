package co.edu.uv.vinzstock.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uv.vinzstock.dto.DetalleVentaDTO;
import co.edu.uv.vinzstock.dto.VentaDTO;
import co.edu.uv.vinzstock.model.ClienteModel;
import co.edu.uv.vinzstock.model.DetallesVentasModel;
import co.edu.uv.vinzstock.model.InventarioModel;
import co.edu.uv.vinzstock.model.ProductoModel;
import co.edu.uv.vinzstock.model.UsuarioModel;
import co.edu.uv.vinzstock.model.VentasModel;
import co.edu.uv.vinzstock.repository.*;
import jakarta.transaction.Transactional;

@Service
public class VentasService {

    private final VentasRepository ventasRepository;
    private final DetallesVentasRepository detallesVentasRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;



    @Autowired
    public VentasService(VentasRepository ventasRepository, DetallesVentasRepository detallesVentasRepository, 
        UsuarioRepository usuarioRepository, ClienteRepository clienteRepository, ProductoRepository productoRepository, InventarioRepository inventarioRepository){
        
        this.ventasRepository = ventasRepository;
        this.detallesVentasRepository = detallesVentasRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
  

    }



    /**
     * Registrar una venta completa con sus detalles
     */
    @Transactional
    public VentasModel registrarVenta(VentaDTO ventaDTO) {
        // Validar usuario
        UsuarioModel usuario = usuarioRepository.findById(ventaDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar cliente
        ClienteModel cliente = clienteRepository.findById(ventaDTO.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Validar que hay detalles
        if (ventaDTO.getDetalles() == null || ventaDTO.getDetalles().isEmpty()) {
            throw new RuntimeException("La venta debe tener al menos un producto");
        }

        // Validar inventario antes de procesar
        for (DetalleVentaDTO detalle : ventaDTO.getDetalles()) {
            ProductoModel producto = productoRepository.findById(detalle.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalle.getIdProducto()));

            InventarioModel inventario = inventarioRepository.findByProducto(producto)
                    .orElseThrow(() -> new RuntimeException("No hay inventario para el producto: " + producto.getNombre()));

            if (inventario.getCantidad() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para " + producto.getNombre() + 
                    ". Disponible: " + inventario.getCantidad() + ", Solicitado: " + detalle.getCantidad());
            }
        }

        // Crear la venta
        VentasModel venta = VentasModel.builder()
                .usuario(usuario)
                .cliente(cliente)
                .valorVenta(ventaDTO.getValorVenta())
                .build();

        VentasModel ventaGuardada = ventasRepository.save(venta);

        // Crear los detalles y actualizar inventario
        List<DetallesVentasModel> detalles = new ArrayList<>();
        for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {
            ProductoModel producto = productoRepository.findById(detalleDTO.getIdProducto()).get();

            DetallesVentasModel detalle = DetallesVentasModel.builder()
                    .venta(ventaGuardada)
                    .producto(producto)
                    .cantidad(detalleDTO.getCantidad())
                    .costoUnitario(detalleDTO.getCostoUnitario())
                    .costoTotal(detalleDTO.getCostoTotal())
                    .build();

            detallesVentasRepository.save(detalle);
            detalles.add(detalle);

            // Actualizar inventario (restar cantidad)
            InventarioModel inventario = inventarioRepository.findByProducto(producto).get();
            inventario.setCantidad(inventario.getCantidad() - detalleDTO.getCantidad());
            inventarioRepository.save(inventario);
        }

        return ventaGuardada;
    }

    /**
     * Obtener todas las ventas
     */
    public List<VentasModel> findAllVentas() {
        return ventasRepository.findAll();
    }

    /**
     * Obtener venta por ID
     */
    public VentasModel findVentaById(Long id) {
        return ventasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }

    /**
     * Obtener detalles de una venta
     */
    public List<DetallesVentasModel> findDetallesByVenta(Long idVenta) {
        return detallesVentasRepository.findByVentaIdVenta(idVenta);
    }

    /**
     * Obtener ventas por usuario
     */
    public List<VentasModel> findVentasByUsuario(Long idUsuario) {
        return ventasRepository.findByUsuarioIdUsuario(idUsuario);
    }

    /**
     * Obtener ventas por cliente
     */
    public List<VentasModel> findVentasByCliente(Long idCliente) {
        return ventasRepository.findByClienteIdCliente(idCliente);
    }


}
