package com.bankinc.cards.service;

import com.bankinc.cards.dto.ActivarTarjetaRequest;
import com.bankinc.cards.dto.RecargaSaldoRequest;
import com.bankinc.cards.exception.TarjetaException;
import com.bankinc.cards.model.Tarjeta;
import com.bankinc.cards.repository.TarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class TarjetaService {

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Transactional
    public String generarNumeroTarjeta(String productId, String titular) {
        if (productId == null || productId.length() != 6) {
            throw new TarjetaException("El ID del producto debe tener 6 dígitos");
        }

        Random random = new Random();
        String numeroTarjeta;
        do {
            StringBuilder sb = new StringBuilder();
            sb.append(productId);
            for (int i = 0; i < 10; i++) {
                sb.append(random.nextInt(10));
            }
            numeroTarjeta = sb.toString();
        } while (tarjetaRepository.findById(numeroTarjeta).isPresent());

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNumeroTarjeta(numeroTarjeta);
        tarjeta.setTitular(titular);
        tarjeta.setFechaCreacion(LocalDateTime.now());
        tarjeta.setActiva(false);
        tarjeta.setSaldo(0.0);
        tarjetaRepository.save(tarjeta);

        return numeroTarjeta;
    }

    @Transactional
    public Tarjeta activarTarjeta(ActivarTarjetaRequest request) {
        Tarjeta tarjeta = tarjetaRepository.findById(request.getNumeroTarjeta())
                .orElseThrow(() -> new TarjetaException("Tarjeta no encontrada"));

        if (tarjeta.isActiva()) {
            throw new TarjetaException("La tarjeta ya está activa");
        }

        tarjeta.setActiva(true);
        return tarjetaRepository.save(tarjeta);
    }

    @Transactional
    public void bloquearTarjeta(String numeroTarjeta) {
        Tarjeta tarjeta = tarjetaRepository.findById(numeroTarjeta)
                .orElseThrow(() -> new TarjetaException("Tarjeta no encontrada"));

        if (!tarjeta.isActiva()) {
            throw new TarjetaException("La tarjeta ya está bloqueada");
        }

        tarjeta.setActiva(false);
        tarjetaRepository.save(tarjeta);
    }

    @Transactional
    public Tarjeta recargarSaldo(RecargaSaldoRequest request) {
        if (request.getMonto() <= 0) {
            throw new TarjetaException("El monto debe ser mayor a 0");
        }

        Tarjeta tarjeta = tarjetaRepository.findById(request.getNumeroTarjeta())
                .orElseThrow(() -> new TarjetaException("Tarjeta no encontrada"));

        if (!tarjeta.isActiva()) {
            throw new TarjetaException("No se puede recargar una tarjeta inactiva");
        }

        tarjeta.setSaldo(tarjeta.getSaldo() + request.getMonto());
        return tarjetaRepository.save(tarjeta);
    }

    @Transactional(readOnly = true)
    public double consultarSaldo(String numeroTarjeta) {
        Tarjeta tarjeta = tarjetaRepository.findById(numeroTarjeta)
                .orElseThrow(() -> new TarjetaException("Tarjeta no encontrada"));

        if (!tarjeta.isActiva()) {
            throw new TarjetaException("No se puede consultar el saldo de una tarjeta inactiva");
        }

        return tarjeta.getSaldo();
    }
}
