package co.edu.uv.vinzstock.controller;

import co.edu.uv.vinzstock.dto.LoginRequest;
import co.edu.uv.vinzstock.dto.LoginResponse;
import co.edu.uv.vinzstock.dto.UsuarioDTO;
import co.edu.uv.vinzstock.model.InventarioModel;
import co.edu.uv.vinzstock.model.ProductoModel;
import co.edu.uv.vinzstock.model.RolesModel;
import co.edu.uv.vinzstock.model.UsuarioModel;
import co.edu.uv.vinzstock.security.JwtUtil;
import co.edu.uv.vinzstock.service.InventarioService;
import co.edu.uv.vinzstock.service.ProductoService;
import co.edu.uv.vinzstock.service.RolService;
import co.edu.uv.vinzstock.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
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

    @Autowired
    public VinzstockController(
            UsuarioService usuarioService,
            RolService rolService,
            ProductoService productoService,
            InventarioService inventarioService,
            JwtUtil jwtUtil
    ){
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.productoService = productoService;
        this.inventarioService = inventarioService;
        this.jwtUtil = jwtUtil;
    }

    // ENDPOINT DE LOGIN CON JWT
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            UsuarioModel usuario = usuarioService.authenticateUsuario(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );

            // Generar token JWT
            String token = jwtUtil.generateToken(
                    usuario.getUsuarioLogin(),
                    usuario.getIdUsuario(),
                    usuario.getRol().getNombre()
            );

            // Crear DTO con la información del usuario
            UsuarioDTO usuarioDTO = new UsuarioDTO(
                    usuario.getIdUsuario(),
                    usuario.getNombre(),
                    usuario.getUsuarioLogin(),
                    usuario.getEmail(),
                    usuario.getRol().getNombre()
            );

            LoginResponse response = new LoginResponse(
                    true,
                    "Login exitoso",
                    token,
                    usuarioDTO
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping(path = "/save")
    public ResponseEntity saveUsuario(@RequestBody UsuarioModel usuarioModel){
        try {
            UsuarioModel nuevoUsuario = this.usuarioService.createUsuario(usuarioModel);

            // Respuesta exitosa
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario creado exitosamente");
            response.put("data", nuevoUsuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch(RuntimeException e){
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
        } catch(RuntimeException e){
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping(path = "/all")
    public List<UsuarioModel> findAllUsuarios(){
        return this.usuarioService.findAllUsuarios();
    }

    @GetMapping(path = "/all/nombre")
    public List<UsuarioModel> findAllUsuariosByNombre(@RequestParam(name="nombre") String nombre){
        return this.usuarioService.findAllUsuariosByNombre(nombre);
    }

    @GetMapping(path = "/all/idusuario")
    public List<UsuarioModel> findAllByIdUsuario(@RequestParam(name="idUsuario") long idUsuario){
        return this.usuarioService.findAllByUsuarios(idUsuario);
    }

    // ========================================
    // ENDPOINTS DE ROLES
    // ========================================

    @GetMapping(path = "/roles/all")
    public List<RolesModel> findAllRoles(){
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
    @DeleteMapping(path = "/producto/delete/{id}")
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
    }

    /**
     * ✅ OBTENER TODOS LOS PRODUCTOS
     */
    @GetMapping(path = "/productos/all")
    public ResponseEntity<List<ProductoModel>> findAllProductos(){
        List<ProductoModel> productos = this.productoService.findAllProductos();
        return ResponseEntity.ok(productos);
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
        List<ProductoModel> productos = this.productoService.findByNombreContaining(nombre);
        return ResponseEntity.ok(productos);
    }

    // ========================================
    // ENDPOINTS DE INVENTARIO
    // ========================================

    @GetMapping(path = "/inventarios/all")
    public List<InventarioModel> findAllInventarios(){
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

}
