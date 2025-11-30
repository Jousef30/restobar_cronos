package com.example.demo.Service;

import com.example.demo.model.Pedido;

import com.example.demo.repository.PedidoRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
   
    
    public Pedido crearPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

   
    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    
    public Pedido actualizarPedido(Long id, Pedido pedido) {
        pedido.setId_pedido(id);
        return pedidoRepository.save(pedido);
    }

    
    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }
}
