package co.edu.uv.vinzstock.service;

import co.edu.uv.vinzstock.model.ProveedoresModel;
import co.edu.uv.vinzstock.repository.ProveedoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedoresService {
    private final ProveedoresRepository proveedoresRepository;

    @Autowired
    public ProveedoresService(ProveedoresRepository proveedoresRepository){
        this.proveedoresRepository = proveedoresRepository;
    }

    // --- Excepción personalizada ---
    public class ProveedorDuplicadoException extends RuntimeException {
        public ProveedorDuplicadoException(String campo, String valor) {
            super("Ya existe un proveedor con el " + campo + ": " + valor);
        }
    }

    // ----------------------------------------------------------------
    // OBTENER LISTAS / CONSULTAS
    // ----------------------------------------------------------------

    public List<ProveedoresModel> findAllProveedores() {
        return proveedoresRepository.findAll();
    }

    public Optional<ProveedoresModel> findProveedorById(long idProveedor) {
        return proveedoresRepository.findById(idProveedor);
    }

    public List<ProveedoresModel> findByNombreContaining(String nombre) {
        return proveedoresRepository.findByNombreContainingIgnoreCase(nombre);
    }


    // ----------------------------------------------------------------
    // CREAR PROVEEDOR
    // ----------------------------------------------------------------

    public ProveedoresModel createProveedor(ProveedoresModel proveedor) {

        if (existsByNitFiscal(proveedor.getNitFiscal())) {
            throw new ProveedorDuplicadoException("número de identificación", proveedor.getNitFiscal());
        }

        if (existsByEmail(proveedor.getEmail())) {
            throw new ProveedorDuplicadoException("email", proveedor.getEmail());
        }

        if (existsByNombre(proveedor.getNombre())) {
            throw new ProveedorDuplicadoException("nombre de la compañía", proveedor.getNombre());
        }

        return proveedoresRepository.save(proveedor);
        }


    // ----------------------------------------------------------------
    // ACTUALIZAR PROVEEDOR
    // ----------------------------------------------------------------

    public ProveedoresModel updateProveedor(ProveedoresModel proveedor) {

        // ✅ 1. Verificar que se envió el ID
        if (proveedor.getIdProveedor() <= 0) {
            throw new IllegalArgumentException("El ID del proveedor es obligatorio para actualizar");
        }

        // Buscar proveedor existente
        ProveedoresModel existente = proveedoresRepository.findById(proveedor.getIdProveedor())
                .orElseThrow(() ->
                        new RuntimeException("Proveedor no encontrado con ID: " + proveedor.getIdProveedor())
                );

        // ✅ 3. Validar NIT solo si cambió
        if (!existente.getNitFiscal().equals(proveedor.getNitFiscal())) {
            if (proveedoresRepository.existsByNitFiscal(proveedor.getNitFiscal())) {
                throw new ProveedorDuplicadoException(
                        "número de identificación fiscal (NIT)",
                        proveedor.getNitFiscal()
                );
            }
        }

        // ✅ 4. Validar email solo si cambió
        if (!existente.getEmail().equalsIgnoreCase(proveedor.getEmail())) {
            if (proveedoresRepository.existsByEmail(proveedor.getEmail())) {
                throw new ProveedorDuplicadoException(
                        "email",
                        proveedor.getEmail()
                );
            }
        }

        // ✅ 5. Validar nombre solo si cambió
        if (!existente.getNombre().equalsIgnoreCase(proveedor.getNombre())) {
            if (proveedoresRepository.existsByNombreIgnoreCase(proveedor.getNombre())) {
                throw new ProveedorDuplicadoException(
                        "nombre de la compañía",
                        proveedor.getNombre()
                );
            }
        }

        // ✅ 6. Validar formato del teléfono si se proporciona
        if (proveedor.getTelefono() != null && !proveedor.getTelefono().isEmpty()) {
            if (!proveedor.getTelefono().matches("^\\d{10}$")) {
                throw new IllegalArgumentException(
                        "El teléfono debe tener exactamente 10 dígitos numéricos"
                );
            }
        }

        // ✅ 7. Actualizar campos
        existente.setNombre(proveedor.getNombre());
        existente.setNitFiscal(proveedor.getNitFiscal());
        existente.setEmail(proveedor.getEmail());
        existente.setTelefono(proveedor.getTelefono());

        // ✅ 8. Guardar y retornar
        return proveedoresRepository.save(existente);
    }
    public boolean existsByNombre(String nombre) {
        return proveedoresRepository.existsByNombreIgnoreCase(nombre);
    }

    public boolean existsByEmail(String email) {
        return proveedoresRepository.existsByEmail(email);
    }

    public boolean existsByNitFiscal(String nitFiscal) {
        return proveedoresRepository.existsByNitFiscal(nitFiscal);
    }
}
