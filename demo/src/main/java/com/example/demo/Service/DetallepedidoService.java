package com.example.demo.Service;

import com.example.demo.model.Detallepedido;
import com.example.demo.model.Pedido;
import com.example.demo.repository.DetallepedidoRepository;
import com.example.demo.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetallepedidoService {

    @Autowired
    private DetallepedidoRepository detallepedidoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public Detallepedido crearDetallepedido(Detallepedido detallepedido) {
        Detallepedido detalleGuardado = detallepedidoRepository.save(detallepedido);

        // Recalcular total del pedido
        recalcularTotalPedido(detalleGuardado.getPedido().getId_pedido());

        return detalleGuardado;
    }

    public List<Detallepedido> listarDetallepedido() {
        return detallepedidoRepository.findAll();
    }

    public Optional<Detallepedido> obtenerDetallepedidoPorId(Long id) {
        return detallepedidoRepository.findById(id);
    }

    public Detallepedido actualizarDetallepedido(Long id, Detallepedido detallepedido) {
        detallepedido.setId_detalle(id);
        Detallepedido detalleActualizado = detallepedidoRepository.save(detallepedido);

        // Recalcular total del pedido
        recalcularTotalPedido(detalleActualizado.getPedido().getId_pedido());

        return detalleActualizado;
    }

    public void eliminarDetallepedido(Long id) {
        // Obtener el pedido antes de eliminar para recalcular después
        Optional<Detallepedido> detalleOpt = detallepedidoRepository.findById(id);

        if (detalleOpt.isPresent()) {
            Long idPedido = detalleOpt.get().getPedido().getId_pedido();
            detallepedidoRepository.deleteById(id);

            // Recalcular total del pedido
            recalcularTotalPedido(idPedido);
        }
    }

    // Método privado para recalcular el total del pedido
    private void recalcularTotalPedido(Long idPedido) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(idPedido);

        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();

            // Sumar todos los detalles del pedido
            List<Detallepedido> detalles = detallepedidoRepository.findAll()
                    .stream()
                    .filter(d -> d.getPedido().getId_pedido().equals(idPedido))
                    .toList();

            double total = detalles.stream()
                    .mapToDouble(d -> d.getCantidad() * d.getPrecio())
                    .sum();

            // Actualizar el total del pedido
            pedido.setTotal(total);
            pedidoRepository.save(pedido);

            System.out.println("✅ Total del pedido #" + idPedido + " actualizado a: S/. " + total);
        }
    }

}
