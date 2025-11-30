package com.example.demo.Service;

import com.example.demo.model.Mesa;
import com.example.demo.model.Reserva;
import com.example.demo.repository.MesaRepository;
import com.example.demo.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private MesaRepository mesaRepository;

    public Reserva crearReserva(Reserva reserva) {
        System.out.println("==========================================");
        System.out.println("📝 RECIBIENDO RESERVA:");
        System.out.println(
                "Usuario ID: " + (reserva.getUsuario() != null ? reserva.getUsuario().getIdUsuario() : "null"));
        System.out.println("Mesa ID: " + (reserva.getMesa() != null ? reserva.getMesa().getId_mesa() : "null"));
        System.out.println("Fecha: " + reserva.getFecha());
        System.out.println("Hora: " + reserva.getHora());
        System.out.println("Estado: " + reserva.getEstado());

        // Guardar la reserva
        Reserva reservaGuardada = reservaRepository.save(reserva);
        System.out.println("✅ RESERVA GUARDADA CON ID: " + reservaGuardada.getId_reserva());

        // Actualizar el estado de la mesa a RESERVADA
        if (reserva.getMesa() != null && reserva.getMesa().getId_mesa() != null) {
            // Buscar la mesa completa por ID
            Optional<Mesa> mesaOpt = mesaRepository.findById(reserva.getMesa().getId_mesa());

            if (mesaOpt.isPresent()) {
                Mesa mesa = mesaOpt.get();
                mesa.setEstado(Mesa.Estado.RESERVADA);
                mesaRepository.save(mesa);
                System.out.println("✅ Mesa #" + mesa.getNumero_mesa() + " marcada como RESERVADA");
            }
        }

        System.out.println("==========================================");
        return reservaGuardada;
    }

    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> obtenerReserva(Long id) {
        return reservaRepository.findById(id);
    }

    public Reserva actualizarReserva(Long id, Reserva reserva) {
        reserva.setId_reserva(id);
        return reservaRepository.save(reserva);
    }

    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);
    }
}
