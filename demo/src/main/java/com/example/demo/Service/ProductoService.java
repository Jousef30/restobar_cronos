package com.example.demo.Service;

import com.example.demo.model.Producto;
import com.example.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    
    public Producto actualizarProducto(Long id, Producto producto) {
        producto.setId_producto(id);
        return productoRepository.save(producto);
    }

    
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}
