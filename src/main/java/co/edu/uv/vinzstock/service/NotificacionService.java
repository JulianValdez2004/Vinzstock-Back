package co.edu.uv.vinzstock.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uv.vinzstock.model.InventarioModel;
import co.edu.uv.vinzstock.repository.InventarioRepository;

@Service
public class NotificacionService {
    
    @Autowired
    private InventarioRepository inventarioRepository;
    
    private static final int UMBRAL_MINIMO = 5;
    
    public List<String> obtenerNotificacionesInventarioBajo() {
        List<InventarioModel> inventariosBajos = 
            inventarioRepository.findByCantidadLessThanEqual(UMBRAL_MINIMO);
        
        return inventariosBajos.stream()
            .map(inv -> String.format(
                "El producto %s está próximo a agotarse", 
                inv.getProducto().getNombre()
            ))
            .collect(Collectors.toList());
    }
    
    public int contarProductosBajos() {
        return inventarioRepository.countByCantidadLessThanEqual(UMBRAL_MINIMO);
    }
}