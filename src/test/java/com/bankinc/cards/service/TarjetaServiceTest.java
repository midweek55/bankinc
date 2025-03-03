package com.bankinc.cards.service;

import com.bankinc.cards.dto.ActivarTarjetaRequest;
import com.bankinc.cards.dto.RecargaSaldoRequest;
import com.bankinc.cards.exception.TarjetaException;
import com.bankinc.cards.model.Tarjeta;
import com.bankinc.cards.repository.TarjetaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarjetaServiceTest {

    @Mock
    private TarjetaRepository tarjetaRepository;

    @InjectMocks
    private TarjetaService tarjetaService;

    @Test
    void generarNumeroTarjeta_ConProductIdValido_RetornaTarjetaCreada() {
        // Arrange
        String productId = "123456";
        String titular = "John Doe";
        when(tarjetaRepository.findById(any())).thenReturn(Optional.empty());
        when(tarjetaRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        String numeroTarjeta = tarjetaService.generarNumeroTarjeta(productId, titular);

        // Assert
        assertNotNull(numeroTarjeta);
        assertTrue(numeroTarjeta.startsWith(productId));
        assertEquals(16, numeroTarjeta.length());
        verify(tarjetaRepository).save(any());
    }

    @Test
    void generarNumeroTarjeta_ConProductIdInvalido_LanzaExcepcion() {
        // Arrange
        String productId = "12345"; // menos de 6 dígitos
        String titular = "John Doe";

        // Act & Assert
        assertThrows(TarjetaException.class, () -> 
            tarjetaService.generarNumeroTarjeta(productId, titular)
        );
    }

    @Test
    void activarTarjeta_TarjetaExistente_ActivaCorrectamente() {
        // Arrange
        String numeroTarjeta = "1234567890123456";
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNumeroTarjeta(numeroTarjeta);
        tarjeta.setActiva(false);

        ActivarTarjetaRequest request = new ActivarTarjetaRequest();
        request.setNumeroTarjeta(numeroTarjeta);

        when(tarjetaRepository.findById(numeroTarjeta)).thenReturn(Optional.of(tarjeta));
        when(tarjetaRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Tarjeta resultado = tarjetaService.activarTarjeta(request);

        // Assert
        assertTrue(resultado.isActiva());
        verify(tarjetaRepository).save(tarjeta);
    }

    @Test
    void activarTarjeta_TarjetaNoExistente_LanzaExcepcion() {
        // Arrange
        ActivarTarjetaRequest request = new ActivarTarjetaRequest();
        request.setNumeroTarjeta("1234567890123456");

        when(tarjetaRepository.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TarjetaException.class, () -> 
            tarjetaService.activarTarjeta(request)
        );
    }

    @Test
    void recargarSaldo_TarjetaActivaYMontoValido_RecargaExitosa() {
        // Arrange
        String numeroTarjeta = "1234567890123456";
        double saldoInicial = 100.0;
        double montoRecarga = 50.0;

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNumeroTarjeta(numeroTarjeta);
        tarjeta.setActiva(true);
        tarjeta.setSaldo(saldoInicial);

        RecargaSaldoRequest request = new RecargaSaldoRequest();
        request.setNumeroTarjeta(numeroTarjeta);
        request.setMonto(montoRecarga);

        when(tarjetaRepository.findById(numeroTarjeta)).thenReturn(Optional.of(tarjeta));
        when(tarjetaRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Tarjeta resultado = tarjetaService.recargarSaldo(request);

        // Assert
        assertEquals(saldoInicial + montoRecarga, resultado.getSaldo());
        verify(tarjetaRepository).save(tarjeta);
    }

    @Test
    void recargarSaldo_TarjetaInactiva_LanzaExcepcion() {
        // Arrange
        String numeroTarjeta = "1234567890123456";
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNumeroTarjeta(numeroTarjeta);
        tarjeta.setActiva(false);

        RecargaSaldoRequest request = new RecargaSaldoRequest();
        request.setNumeroTarjeta(numeroTarjeta);
        request.setMonto(50.0);

        when(tarjetaRepository.findById(numeroTarjeta)).thenReturn(Optional.of(tarjeta));

        // Act & Assert
        assertThrows(TarjetaException.class, () -> 
            tarjetaService.recargarSaldo(request)
        );
    }

    @Test
    void consultarSaldo_TarjetaActivaYExistente_RetornaSaldo() {
        // Arrange
        String numeroTarjeta = "1234567890123456";
        double saldoEsperado = 100.0;

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNumeroTarjeta(numeroTarjeta);
        tarjeta.setActiva(true);
        tarjeta.setSaldo(saldoEsperado);

        when(tarjetaRepository.findById(numeroTarjeta)).thenReturn(Optional.of(tarjeta));

        // Act
        double saldoActual = tarjetaService.consultarSaldo(numeroTarjeta);

        // Assert
        assertEquals(saldoEsperado, saldoActual);
    }
}
