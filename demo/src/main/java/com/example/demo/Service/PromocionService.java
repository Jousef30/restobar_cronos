package com.example.demo.Service;

import com.example.demo.model.Promocion;
import com.example.demo.repository.PromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromocionService {

    @Autowired
    private PromocionRepository promocionRepository;

   
    public Promocion crearPromocion(Promocion promocion) {
        return promocionRepository.save(promocion);
    }

    
    public List<Promocion> listarPromociones() {
        return promocionRepository.findAll();
    }

    
    public Optional<Promocion> obtenerPromocionPorId(Long id) {
        return promocionRepository.findById(id);
    }

   
    public Promocion actualizarPromocion(Long id, Promocion promocion) {
        promocion.setId_carrusel(id);
        return promocionRepository.save(promocion);
    }

    
    public void eliminarPromocion(Long id) {
        promocionRepository.deleteById(id);
    }
}
