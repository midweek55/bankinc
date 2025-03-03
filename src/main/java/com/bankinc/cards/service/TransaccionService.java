package com.bankinc.cards.service;

import com.bankinc.cards.dto.AnulacionRequest;
import com.bankinc.cards.dto.CompraRequest;
import com.bankinc.cards.exception.TarjetaException;
import com.bankinc.cards.exception.TransaccionException;
import com.bankinc.cards.model.Tarjeta;
import com.bankinc.cards.model.Transaccion;
import com.bankinc.cards.repository.TarjetaRepository;
import com.bankinc.cards.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Transactional
    public Transaccion realizarCompra(CompraRequest request) {
        Tarjeta tarjeta = tarjetaRepository.findById(request.getNumeroTarjeta())
                .orElseThrow(() -> new TarjetaException("Tarjeta no encontrada"));

        if (!tarjeta.isActiva()) {
            throw new TarjetaException("No se puede realizar compras con una tarjeta inactiva");
        }

        if (!tarjeta.tieneSaldoSuficiente(request.getMonto())) {
            throw new TarjetaException("Saldo insuficiente");
        }

        Transaccion transaccion = new Transaccion();
        transaccion.setId(UUID.randomUUID().toString());
        transaccion.setTarjeta(tarjeta);
        transaccion.setMonto(request.getMonto());
        transaccion.setFechaTransaccion(LocalDateTime.now());
        transaccion.setAnulada(false);

        tarjeta.setSaldo(tarjeta.getSaldo() - request.getMonto());
        tarjetaRepository.save(tarjeta);

        return transaccionRepository.save(transaccion);
    }

    @Transactional(readOnly = true)
    public Transaccion consultarTransaccion(String transaccionId) {
        return transaccionRepository.findById(transaccionId)
                .orElseThrow(() -> new TransaccionException("Transacción no encontrada"));
    }

    @Transactional
    public Transaccion anularTransaccion(AnulacionRequest request) {
        Transaccion transaccion = transaccionRepository.findById(request.getTransaccionId())
                .orElseThrow(() -> new TransaccionException("Transacción no encontrada"));

        if (transaccion.isAnulada()) {
            throw new TransaccionException("La transacción ya fue anulada");
        }

        LocalDateTime ahora = LocalDateTime.now();
        if (ahora.isAfter(transaccion.getFechaTransaccion().plusHours(24))) {
            throw new TransaccionException("No se puede anular una transacción después de 24 horas");
        }

        Tarjeta tarjeta = transaccion.getTarjeta();
        tarjeta.setSaldo(tarjeta.getSaldo() + transaccion.getMonto());
        tarjetaRepository.save(tarjeta);

        transaccion.setAnulada(true);
        transaccion.setFechaAnulacion(ahora);
        return transaccionRepository.save(transaccion);
    }
}
