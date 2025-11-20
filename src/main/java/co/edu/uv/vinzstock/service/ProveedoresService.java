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

    public List<ProveedoresModel> findByNombreCompaniaContaining(String nombre) {
        return proveedoresRepository.findByNombreCompaniaContainingIgnoreCase(nombre);
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

        if (existsByNombreCompania(proveedor.getNombreCompania())) {
            throw new ProveedorDuplicadoException("nombre de la compañía", proveedor.getNombreCompania());
        }

        return proveedoresRepository.save(proveedor);
        }


    // ----------------------------------------------------------------
    // ACTUALIZAR PROVEEDOR
    // ----------------------------------------------------------------

    public ProveedoresModel updateProveedor(ProveedoresModel proveedor) {

        ProveedoresModel existente = proveedoresRepository.findById(proveedor.getIdProveedor())
                .orElseThrow(() ->
                        new RuntimeException("Proveedor no encontrado con ID: " + proveedor.getIdProveedor())
                );

        // Si cambia el número de identificación → validar duplicado
        if (!existente.getNitFiscal().equals(proveedor.getNitFiscal())) {
            if (proveedoresRepository.existsByNitFiscal(proveedor.getNitFiscal())) {
                throw new ProveedorDuplicadoException(
                        "número de identificación",
                        proveedor.getNitFiscal()
                );
            }
        }

        // Si cambia email → validar duplicado
        if (!existente.getEmail().equalsIgnoreCase(proveedor.getEmail())) {
            if (proveedoresRepository.existsByEmail(proveedor.getEmail())) {
                throw new ProveedorDuplicadoException("email", proveedor.getEmail());
            }
        }

        // Si cambia el nombre compañía → validar duplicado
        if (!existente.getNombreCompania().equalsIgnoreCase(proveedor.getNombreCompania())) {
            if (proveedoresRepository.existsByNombreCompaniaIgnoreCase(proveedor.getNombreCompania())) {
                throw new ProveedorDuplicadoException(
                        "nombre de la compañía",
                        proveedor.getNombreCompania()
                );
            }
        }

        // Actualizar campos
        existente.setNombreCompania(proveedor.getNombreCompania());
        existente.setNitFiscal(proveedor.getNitFiscal());
        existente.setEmail(proveedor.getEmail());
        existente.setNumeroContacto(proveedor.getNumeroContacto());

        return proveedoresRepository.save(existente);
    }
    public boolean existsByNombreCompania(String nombre) {
        return proveedoresRepository.existsByNombreCompaniaIgnoreCase(nombre);
    }

    public boolean existsByEmail(String email) {
        return proveedoresRepository.existsByEmail(email);
    }

    public boolean existsByNitFiscal(String nit) {
        return proveedoresRepository.existsByNitFiscal(nit);
    }

}
