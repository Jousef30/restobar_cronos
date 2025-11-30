package com.example.demo.Service;

import com.example.demo.model.Mesa;
import com.example.demo.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    
    public Mesa crearMesa(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    
    public List<Mesa> listarMesas() {
        return mesaRepository.findAll();
    }

    
    public Optional<Mesa> obtenerMesaPorId(Long id) {
        return mesaRepository.findById(id);
    }

    
    public Mesa actualizarMesa(Long id, Mesa mesa) {
        mesa.setId_mesa(id);
        return mesaRepository.save(mesa);
    }

    
    public void eliminarMesa(Long id) {
        mesaRepository.deleteById(id);
    }
}
