package com.example.demo.Service;

import com.example.demo.model.Comprobante;
import com.example.demo.repository.ComprobanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ComprobanteService {
     @Autowired
    private ComprobanteRepository comprobanteRepository;

    
    public Comprobante crearComprobante(Comprobante comprobante) {
        return comprobanteRepository.save(comprobante);
    }

    
    public List<Comprobante> listarComprobante() {
        return comprobanteRepository.findAll();
    }

   
    public Optional<Comprobante> obtenerComprobantePorId(Long id) {
        return comprobanteRepository.findById(id);
    }

    
    public Comprobante actualizarComprobante(Long id, Comprobante comprobante) {
        comprobante.setId_comprobante(id);
        return comprobanteRepository.save(comprobante);
    }

    
    public void eliminarComprobante(Long id) {
        comprobanteRepository.deleteById(id);
    }
}

