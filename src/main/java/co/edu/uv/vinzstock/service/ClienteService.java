package co.edu.uv.vinzstock.service;

import co.edu.uv.vinzstock.model.ClienteModel;
import co.edu.uv.vinzstock.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Crear un nuevo cliente
     */
    public ClienteModel createCliente(ClienteModel cliente) {
        // Validar campos obligatorios
        validarCamposObligatorios(cliente);

        // Validar formato del nombre
        validarNombreRazonSocial(cliente.getNombreRazonSocial());

        // Validar formato del número de documento
        validarNumeroDocumento(cliente.getNumeroDocumento());

        // Verificar si el documento ya existe
        if (clienteRepository.existsByNumeroDocumento(cliente.getNumeroDocumento())) {
            throw new RuntimeException(
                "El cliente ya se encuentra registrado, verifique la información."
            );
        }

        // Verificar si el nombre ya existe (opcional, según tus reglas de negocio)
        if (clienteRepository.existsByNombreRazonSocial(cliente.getNombreRazonSocial())) {
            throw new RuntimeException(
                "Ya existe un cliente con ese nombre o razón social."
            );
        }

        // Normalizar datos
        cliente.setNombreRazonSocial(cliente.getNombreRazonSocial().trim());
        cliente.setNumeroDocumento(cliente.getNumeroDocumento().trim());
        
        // Asegurar que tenga estado activo por defecto
        cliente.setEstado(true);

        // Guardar
        return clienteRepository.save(cliente);
    }

    /**
     * Obtener todos los clientes
     */
    public List<ClienteModel> findAllClientes() {
        return clienteRepository.findAll();
    }

    /**
     * Obtener cliente por ID
     */
    public Optional<ClienteModel> findClienteById(long id) {
        return clienteRepository.findById(id);
    }

    /**
     * Obtener clientes por ID (lista)
     */
    public List<ClienteModel> findByIdCliente(long idCliente) {
        return clienteRepository.findByIdCliente(idCliente);
    }

    /**
     * Buscar clientes por nombre (contiene)
     */
    public List<ClienteModel> findByNombreContaining(String nombre) {
        return clienteRepository.findByNombreRazonSocialContainingIgnoreCase(nombre);
    }

    /**
     * Buscar clientes por nombre (contains sin ignore case)
     */
    public List<ClienteModel> findByNombreContains(String nombre) {
        return clienteRepository.findByNombreRazonSocialContains(nombre);
    }

    /**
     * Buscar cliente por número de documento
     */
    public Optional<ClienteModel> findByNumeroDocumento(String numeroDocumento) {
        return clienteRepository.findByNumeroDocumento(numeroDocumento);
    }

    /**
     * Buscar cliente por nombre exacto
     */
    public Optional<ClienteModel> findByNombreRazonSocial(String nombreRazonSocial) {
        return clienteRepository.findByNombreRazonSocial(nombreRazonSocial);
    }

    /**
     * Verificar si existe un cliente por documento
     */
    public boolean existsByNumeroDocumento(String numeroDocumento) {
        return clienteRepository.existsByNumeroDocumento(numeroDocumento);
    }

    /**
     * Verificar si existe un cliente por nombre
     */
    public boolean existsByNombreRazonSocial(String nombreRazonSocial) {
        return clienteRepository.existsByNombreRazonSocial(nombreRazonSocial);
    }

    // ============================================
    // MÉTODOS DE VALIDACIÓN PRIVADOS
    // ============================================

    private void validarCamposObligatorios(ClienteModel cliente) {
        if (cliente.getNombreRazonSocial() == null || cliente.getNombreRazonSocial().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre o razón social es obligatorio");
        }

        if (cliente.getNumeroDocumento() == null || cliente.getNumeroDocumento().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de documento es obligatorio");
        }
    }

    private void validarNombreRazonSocial(String nombre) {
        if (nombre.length() > 100) {
            throw new IllegalArgumentException(
                "El nombre o razón social no puede exceder 100 caracteres"
            );
        }

        // Validar que no contenga caracteres especiales
        // Permite: letras (con tildes), números, espacios, punto, coma, apóstrofe y guión
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s.,'-]+$")) {
            throw new IllegalArgumentException(
                "El nombre o razón social contiene caracteres no permitidos"
            );
        }
    }

    private void validarNumeroDocumento(String numeroDocumento) {
        String documento = numeroDocumento.trim();
        
        // Validar que no esté vacío
        if (documento.isEmpty()) {
            throw new IllegalArgumentException(
                "El número de documento es obligatorio"
            );
        }
        
        // Formato 1: CC (Cédula de Ciudadanía) - Solo números, entre 6 y 12 dígitos
        // Ejemplo: 1234567890
        boolean esCedula = documento.matches("^\\d{6,12}$");
        
        // Formato 2: NIT - 9 dígitos, guión, 1 dígito verificador
        // Ejemplo: 123456789-0
        boolean esNit = documento.matches("^\\d{9}-\\d{1}$");
        
        // Debe cumplir con alguno de los dos formatos
        if (!esCedula && !esNit) {
            throw new IllegalArgumentException(
                "Formato inválido. Use CC (6-12 dígitos) o NIT (123456789-0)"
            );
        }
        
        // Validación adicional para NIT: calcular dígito de verificación (opcional)
        if (esNit) {
            validarDigitoVerificadorNIT(documento);
        }
    }
    
    /**
     * Valida el dígito verificador del NIT colombiano
     * Algoritmo estándar DIAN
     */
    private void validarDigitoVerificadorNIT(String nit) {
        try {
            // Separar número base y dígito verificador
            String[] partes = nit.split("-");
            String numeroBase = partes[0];
            int digitoVerificador = Integer.parseInt(partes[1]);
            
            // Pesos para el cálculo (de derecha a izquierda: 3,7,13,17,19,23,29,37,41,43,47,53,59,67,71)
            int[] pesos = {71, 67, 59, 53, 47, 43, 41, 37, 29, 23, 19, 17, 13, 7, 3};
            
            int suma = 0;
            int longitudNumero = numeroBase.length();
            
            // Calcular suma ponderada
            for (int i = 0; i < longitudNumero; i++) {
                int digito = Character.getNumericValue(numeroBase.charAt(i));
                int peso = pesos[pesos.length - longitudNumero + i];
                suma += digito * peso;
            }
            
            // Calcular dígito verificador esperado
            int residuo = suma % 11;
            int digitoEsperado = (residuo > 1) ? (11 - residuo) : residuo;
            
            // Validar
            if (digitoVerificador != digitoEsperado) {
                throw new IllegalArgumentException(
                    "El dígito verificador del NIT es incorrecto. Debería ser: " + digitoEsperado
                );
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de NIT inválido");
        }
    }
}
