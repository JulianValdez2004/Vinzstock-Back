package co.edu.uv.vinzstock.controller;

import co.edu.uv.vinzstock.dto.*;
import co.edu.uv.vinzstock.model.*;
import co.edu.uv.vinzstock.security.JwtUtil;
import co.edu.uv.vinzstock.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import co.edu.uv.vinzstock.dto.ProductoConInventarioDTO;
import co.edu.uv.vinzstock.service.ProductoService;
import co.edu.uv.vinzstock.service.PdfService;
import org.springframework.http.*;

import org.springframework.http.HttpHeaders;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/vinzstock")
@CrossOrigin(origins = "http://localhost:5173")
public class VinzstockController {

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final ProductoService productoService;
    private final InventarioService inventarioService;
    private final JwtUtil jwtUtil;
    private final ProveedoresService proveedoresService;
    private final CompraService compraService;
    private final ProductosProveedoresService productosProveedoresService;
    private final NotificacionService notificacionService;
    private final PdfService pdfService;
    
    private final ClienteService clienteService;

    @Autowired
    public VinzstockController(
            UsuarioService usuarioService,
            RolService rolService,
            ProductoService productoService,
            InventarioService inventarioService,
            JwtUtil jwtUtil,
            ProveedoresService proveedoresService,
            CompraService compraService, // ✅ AGREGAR
            ProductosProveedoresService productosProveedoresService,
            NotificacionService notificacionService,
            PdfService pdfService,
            ClienteService clienteService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.productoService = productoService;
        this.inventarioService = inventarioService;
        this.proveedoresService = proveedoresService;
        this.compraService = compraService; // ✅ AGREGAR
        this.productosProveedoresService = productosProveedoresService;
        this.clienteService = clienteService;
        this.jwtUtil = jwtUtil;
        this.notificacionService = notificacionService;
        this.pdfService = pdfService;
    }

    // ENDPOINT DE LOGIN CON JWT
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            UsuarioModel usuario = usuarioService.authenticateUsuario(
                    loginRequest.getUsername(),
                    loginRequest.getPassword());

            // Generar token JWT
            String token = jwtUtil.generateToken(
                    usuario.getUsuarioLogin(),
                    usuario.getIdUsuario(),
                    usuario.getRol().getNombre());

            // Crear DTO con la información del usuario
            UsuarioDTO usuarioDTO = new UsuarioDTO(
                    usuario.getIdUsuario(),
                    usuario.getNombre(),
                    usuario.getUsuarioLogin(),
                    usuario.getEmail(),
                    usuario.getRol().getNombre());

            LoginResponse response = new LoginResponse(
                    true,
                    "Login exitoso",
                    token,
                    usuarioDTO);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping(path = "/save")
    public ResponseEntity saveUsuario(@RequestBody UsuarioModel usuarioModel) {
        try {
            UsuarioModel nuevoUsuario = this.usuarioService.createUsuario(usuarioModel);

            // Respuesta exitosa
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario creado exitosamente");
            response.put("data", nuevoUsuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            // Capturar el error y devolverlo en formato JSON
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping(path = "/update")
    public ResponseEntity<Map<String, Object>> updateUsuario(@RequestBody UsuarioModel usuarioModel) {
        try {
            UsuarioModel usuarioActualizado = this.usuarioService.updateUsuario(usuarioModel);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario actualizado exitosamente");
            response.put("data", usuarioActualizado);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping(path = "/all")
    public List<UsuarioModel> findAllUsuarios() {
        return this.usuarioService.findAllUsuarios();
    }

    @GetMapping(path = "/all/nombre")
    public List<UsuarioModel> findAllUsuariosByNombre(@RequestParam(name = "nombre") String nombre) {
        return this.usuarioService.findAllUsuariosByNombre(nombre);
    }

    @GetMapping(path = "/all/idusuario")
    public List<UsuarioModel> findAllByIdUsuario(@RequestParam(name = "idUsuario") long idUsuario) {
        return this.usuarioService.findAllByUsuarios(idUsuario);
    }

    // ========================================
    // ENDPOINTS DE ROLES
    // ========================================

    @GetMapping(path = "/roles/all")
    public List<RolesModel> findAllRoles() {
        return this.rolService.findAllRoles();
    }

    // ========================================
    // ENDPOINTS DE PRODUCTOS
    // ========================================

    /**
     * ✅ CREAR PRODUCTO
     */
    @PostMapping(path = "/producto/save")
    public ResponseEntity<Map<String, Object>> saveProducto(@RequestBody ProductoModel productoModel) {
        try {
            ProductoModel nuevoProducto = this.productoService.createProducto(productoModel);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto creado exitosamente");
            response.put("data", nuevoProducto);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * ✅ ACTUALIZAR PRODUCTO
     */
    @PutMapping(path = "/producto/update")
    public ResponseEntity<Map<String, Object>> updateProducto(@RequestBody ProductoModel productoModel) {
        try {
            ProductoModel productoActualizado = this.productoService.updateProducto(productoModel);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto actualizado exitosamente");
            response.put("data", productoActualizado);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * ✅ ELIMINAR PRODUCTO
     */
    /*@DeleteMapping(path = "/producto/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteProducto(@PathVariable long id) {
        try {
            this.productoService.deleteProducto(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto eliminado exitosamente");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }*/

    /**
     * ✅ OBTENER TODOS LOS PRODUCTOS
     */
    @GetMapping(path = "/productos/all")
    public ResponseEntity<List<ProductoConInventarioDTO>> getAllProductos()  {
    return ResponseEntity.ok(productoService.getAllProductosConInventario());
    }

    /**
     * ✅ OBTENER PRODUCTO POR ID
     */
    @GetMapping(path = "/producto/{id}")
    public ResponseEntity<?> findProductoById(@PathVariable long id) {
        try {
            ProductoModel producto = this.productoService.findProductoById(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * ✅ BUSCAR PRODUCTOS POR NOMBRE
     */
    @GetMapping(path = "/producto/search")
    public ResponseEntity<List<ProductoModel>> searchProductos(@RequestParam String nombre) {
        List<ProductoModel> productos = this.productoService.findAllProductosByNombre(nombre);
        return ResponseEntity.ok(productos);
    }

    // ========================================
    // ENDPOINTS DE INVENTARIO
    // ========================================

    @GetMapping(path = "/inventarios/all")
    public List<InventarioModel> findAllInventarios() {
        return this.inventarioService.findAllInventarios();
    }

    // Endpoint para recuperación de contraseña
    @PostMapping("/recover-password")
    public ResponseEntity<?> recoverPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        // Validar si el correo existe
        Optional<UsuarioModel> userOpt = usuarioService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "El correo no está registrado."));
        }

        UsuarioModel user = userOpt.get();

        // Validar que el rol sea Administrador o Cajero
        String rolNombre = user.getRol().getNombre(); // asumimos que el Rol tiene un nombre
        if (!rolNombre.equalsIgnoreCase("Administrador") && !rolNombre.equalsIgnoreCase("Cajero")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "No tienes permisos para recuperar contraseña."));
        }

        String password = user.getContrasena();

        // Enviar el correo con la contraseña
        usuarioService.sendPasswordByEmail(email, password);

        return ResponseEntity.ok(Map.of("message", "Se ha enviado un correo a " + email));
    }

    /*
     * ===PROVEEDORES===
     */

    // Crear Proveedor
    @PostMapping("/proveedor/save")
    public ResponseEntity<?> saveProveedor(@RequestBody ProveedoresModel proveedor) {
        try {
            ProveedoresModel nuevo = proveedoresService.createProveedor(proveedor);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Proveedor registrado exitosamente",
                    "data", nuevo));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // Actualizar Proveedor
    @PutMapping("/proveedor/update")
    public ResponseEntity<?> updateProveedor(@RequestBody ProveedoresModel proveedor) {
        try {
            ProveedoresModel actualizado = proveedoresService.updateProveedor(proveedor);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Proveedor actualizado exitosamente",
                    "data", actualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // Listar todos los proveedores:
    @GetMapping("/proveedores/all")
    public ResponseEntity<List<ProveedoresModel>> findAllProveedores() {
        return ResponseEntity.ok(proveedoresService.findAllProveedores());
    }

    // Obtener proveedor por id:
    @GetMapping("/proveedor/{id}")
    public ResponseEntity<?> findProveedorById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(
                    proveedoresService.findProveedorById(id)
                            .orElseThrow(() -> new RuntimeException("Proveedor no encontrado")));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // Buscar proovedores por nombre:
    @GetMapping("/proveedor/search")
    public ResponseEntity<List<ProveedoresModel>> searchByNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(proveedoresService.findByNombreContaining(nombre));
    }

    /*
     * Validar nombre
     * 
     * @GetMapping("/proveedor/validar/nombre/{nombre}")
     * public ResponseEntity<Boolean> validarNombre(@PathVariable String nombre) {
     * boolean disponible = !proveedoresService.existsByNombreCompania(nombre);
     * return ResponseEntity.ok(disponible);
     * }
     * 
     * // Validar email
     * 
     * @GetMapping("/proveedor/validar/email/{email}")
     * public ResponseEntity<Boolean> validarEmail(@PathVariable String email) {
     * boolean disponible = !proveedoresService.existsByEmail(email);
     * return ResponseEntity.ok(disponible);
     * }
     * 
     * // Validar NIT
     * 
     * @GetMapping("/proveedor/validar/nit/{nit}")
     * public ResponseEntity<Boolean> validarNit(@PathVariable String nit) {
     * boolean disponible = !proveedoresService.existsByNitFiscal(nit);
     * return ResponseEntity.ok(disponible);
     * }
     */
    // ========================================
    // ENDPOINTS DE COMPRAS
    // ========================================

    @PostMapping(path = "/compras/registrar")
    public ResponseEntity<?> registrarCompra(@RequestBody CompraDTO compraDTO) {
        try {
            System.out.println("=== REGISTRANDO COMPRA ===");
            System.out.println("Proveedor ID: " + compraDTO.getIdProveedor());
            System.out.println("Detalles: " + compraDTO.getDetalles());

            ComprasModel compra = compraService.registrarCompra(compraDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Compra registrada exitosamente");
            response.put("data", compra);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            System.err.println("Error al registrar compra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    @GetMapping(path = "/compras/all")
    public ResponseEntity<?> findAllCompras() {
        try {
            List<ComprasModel> compras = compraService.findAllCompras();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", compras,
                    "total", compras.size()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    @GetMapping(path = "/compras/{id}")
    public ResponseEntity<?> findCompraById(@PathVariable Long id) {
        try {
            ComprasModel compra = compraService.findCompraById(id);
            List<DetalleCompraModel> detalles = compraService.findDetallesByCompra(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("compra", compra);
            response.put("detalles", detalles);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    @GetMapping(path = "/compras/proveedor/{idProveedor}")
    public ResponseEntity<?> findComprasByProveedor(@PathVariable Long idProveedor) {
        try {
            List<ComprasModel> compras = compraService.findComprasByProveedor(idProveedor);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", compras,
                    "total", compras.size()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    @GetMapping(path = "/productos/proveedor/{idProveedor}")
    public ResponseEntity<?> findProductosByProveedor(@PathVariable Long idProveedor) {
        try {
            List<ProductoModel> productos = productosProveedoresService.findProductosByProveedor(idProveedor);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Productos encontrados: " + productos.size(),
                    "data", productos,
                    "total", productos.size()));
                    

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/inventario-bajo")
    public ResponseEntity<List<String>> getNotificacionesInventarioBajo() {
        return ResponseEntity.ok(notificacionService.obtenerNotificacionesInventarioBajo());
    }
    
    @GetMapping("/inventario-bajo/count")
    public ResponseEntity<Integer> getContadorNotificaciones() {
        return ResponseEntity.ok(notificacionService.contarProductosBajos());
    }

    @GetMapping("/productos/exportar-pdf")
    public ResponseEntity<byte[]> exportarInventarioPdf() {
        try {
            List<ProductoConInventarioDTO> productos = productoService.getAllProductosConInventario();
            byte[] pdfBytes = pdfService.generarReporteInventario(productos);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                ContentDisposition.attachment()
                    .filename("reporte_inventario_" + 
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
                        ".pdf")
                    .build()
            );
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    // ========================================
    // ENDPOINTS DE CLIENTES
    // ========================================

    @PostMapping("/cliente/save")
    public ResponseEntity<?> saveCliente(@RequestBody ClienteModel cliente) {
        try {
            ClienteModel nuevo = clienteService.createCliente(cliente);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "El cliente " + nuevo.getNombreRazonSocial() + " fue guardado exitosamente.",
                    "data", nuevo
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /*
    * ✅ LISTAR TODOS LOS CLIENTES
    */
    @GetMapping("/clientes/all")
    public ResponseEntity<?> findAllClientes() {
        try {
            List<ClienteModel> clientes = clienteService.findAllClientes();
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", clientes,
                    "total", clientes.size()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /*
    * ✅ OBTENER CLIENTE POR ID
    */
    @GetMapping("/cliente/{id}")
    public ResponseEntity<?> findClienteById(@PathVariable long id) {
        try {
            ClienteModel cliente = clienteService.findClienteById(id)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", cliente
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /*
     * ✅ BUSCAR CLIENTES POR NOMBRE
     */
    @GetMapping("/cliente/search")
    public ResponseEntity<?> searchClienteByNombre(@RequestParam String nombre) {
        try {
            List<ClienteModel> clientes = clienteService.findByNombreContaining(nombre);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", clientes,
                    "total", clientes.size()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /*
     * ✅ BUSCAR CLIENTE POR NÚMERO DE DOCUMENTO
     */
    @GetMapping("/cliente/documento/{numeroDocumento}")
    public ResponseEntity<?> findClienteByDocumento(@PathVariable String numeroDocumento) {
        try {
            ClienteModel cliente = clienteService.findByNumeroDocumento(numeroDocumento)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con documento: " + numeroDocumento));

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", cliente
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "Cliente no encontrado"
            ));
        }
    }

    /*
     * ✅ OBTENER CLIENTES ACTIVOS
     */
    @GetMapping("/clientes/activos")
    public ResponseEntity<?> findClientesActivos() {
        try {
            List<ClienteModel> clientes = clienteService.findAllClientes()
                    .stream()
                    .filter(ClienteModel::isEstado)  // Filtra solo los activos (estado = true)
                    .toList();
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", clientes,
                    "total", clientes.size()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /*
     * ✅ VERIFICAR SI EXISTE CLIENTE POR DOCUMENTO
     */
    @GetMapping("/cliente/existe/{numeroDocumento}")
    public ResponseEntity<?> existeClientePorDocumento(@PathVariable String numeroDocumento) {
        try {
            boolean existe = clienteService.existsByNumeroDocumento(numeroDocumento);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "existe", existe
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    
        
    

    /**
     * Obtener compras PENDIENTES de un proveedor
     * GET /compras/proveedor/{idProveedor}/pendientes
     * 
     * Este es el endpoint principal para tu pantalla de historial
     */
    @GetMapping(path = "/compras/proveedor/{idProveedor}/pendientes")
    public ResponseEntity<?> findComprasPendientesByProveedor(@PathVariable Long idProveedor) {
        try {
            System.out.println("📋 Consultando compras pendientes del proveedor: " + idProveedor);

            List<CompraResponseDTO> compras = compraService.obtenerHistorialComprasPorProveedor(idProveedor);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Compras pendientes encontradas: " + compras.size(),
                    "data", compras,
                    "total", compras.size()));
        } catch (RuntimeException e) {
            System.err.println("❌ Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    /**
     * ✅ Confirmar recepción de una compra (actualiza inventario)
     * PUT /compras/{idCompra}/confirmar
     */
    @PutMapping(path = "/compras/{idCompra}/confirmar")
    public ResponseEntity<?> confirmarRecepcionCompra(@PathVariable Long idCompra) {
        try {
            System.out.println("✅ Confirmando recepción de compra: " + idCompra);

            compraService.confirmarRecepcionCompra(idCompra);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Compra confirmada exitosamente. Inventario actualizado."));
        } catch (RuntimeException e) {
            System.err.println("❌ Error al confirmar: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    /**
     * ❌ Cancelar una compra
     * PUT /compras/{idCompra}/cancelar
     */
    @PutMapping(path = "/compras/{idCompra}/cancelar")
    public ResponseEntity<?> cancelarCompra(@PathVariable Long idCompra) {
        try {
            System.out.println("❌ Cancelando compra: " + idCompra);

            compraService.cancelarCompra(idCompra);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Compra cancelada exitosamente"));
        } catch (RuntimeException e) {
            System.err.println("❌ Error al cancelar: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    /**
     * 🗑️ Eliminar una compra completamente
     * DELETE /compras/{idCompra}
     */
    @DeleteMapping(path = "/compras/{idCompra}")
    public ResponseEntity<?> eliminarCompra(@PathVariable Long idCompra) {
        try {
            System.out.println("🗑️ Eliminando compra: " + idCompra);

            compraService.eliminarCompra(idCompra);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Compra eliminada exitosamente"));
        } catch (RuntimeException e) {
            System.err.println("❌ Error al eliminar: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    /**
     * 🗑️ Borrar historial de compras pendientes de un proveedor
     * DELETE /compras/proveedor/{idProveedor}/historial-pendiente
     */
    @DeleteMapping(path = "/compras/proveedor/{idProveedor}/historial-pendiente")
    public ResponseEntity<?> borrarHistorialPendiente(@PathVariable Long idProveedor) {
        try {
            System.out.println("🗑️ Borrando historial pendiente del proveedor: " + idProveedor);

            compraService.borrarHistorialCompleto(idProveedor);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Historial de compras pendientes eliminado exitosamente"));
        } catch (RuntimeException e) {
            System.err.println("❌ Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    /**
     * 📊 Obtener estadísticas de compras
     * GET /compras/estadisticas
     */
    /*@GetMapping(path = "/compras/estadisticas")
    public ResponseEntity<?> obtenerEstadisticasCompras() {
        try {
            CompraEstadisticasDTO stats = compraService.obtenerEstadisticas();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", stats));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }*/

}
