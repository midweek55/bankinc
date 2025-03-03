package com.bankinc.cards.service;

import com.bankinc.cards.dto.AnulacionRequest;
import com.bankinc.cards.dto.CompraRequest;
import com.bankinc.cards.exception.TarjetaException;
import com.bankinc.cards.exception.TransaccionException;
import com.bankinc.cards.model.Tarjeta;
import com.bankinc.cards.model.Transaccion;
import com.bankinc.cards.repository.TarjetaRepository;
import com.bankinc.cards.repository.TransaccionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private TarjetaRepository tarjetaRepository;

    @InjectMocks
    private TransaccionService transaccionService;

    @Test
    void realizarCompra_TarjetaActivaYSaldoSuficiente_CompraExitosa() {
        // Arrange
        String numeroTarjeta = "1234567890123456";
        double saldoInicial = 1000.0;
        double montoCompra = 500.0;

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNumeroTarjeta(numeroTarjeta);
        tarjeta.setActiva(true);
        tarjeta.setSaldo(saldoInicial);

        CompraRequest request = new CompraRequest();
        request.setNumeroTarjeta(numeroTarjeta);
        request.setMonto(montoCompra);

        when(tarjetaRepository.findById(numeroTarjeta)).thenReturn(Optional.of(tarjeta));
        when(tarjetaRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(transaccionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Transaccion resultado = transaccionService.realizarCompra(request);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(montoCompra, resultado.getMonto());
        assertEquals(saldoInicial - montoCompra, tarjeta.getSaldo());
        assertFalse(resultado.isAnulada());
        verify(tarjetaRepository).save(tarjeta);
        verify(transaccionRepository).save(any());
    }

    @Test
    void realizarCompra_SaldoInsuficiente_LanzaExcepcion() {
        // Arrange
        String numeroTarjeta = "1234567890123456";
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNumeroTarjeta(numeroTarjeta);
        tarjeta.setActiva(true);
        tarjeta.setSaldo(100.0);

        CompraRequest request = new CompraRequest();
        request.setNumeroTarjeta(numeroTarjeta);
        request.setMonto(200.0);

        when(tarjetaRepository.findById(numeroTarjeta)).thenReturn(Optional.of(tarjeta));

        // Act & Assert
        assertThrows(TarjetaException.class, () -> 
            transaccionService.realizarCompra(request)
        );
    }

    @Test
    void anularTransaccion_TransaccionRecienteYNoAnulada_AnulacionExitosa() {
        // Arrange
        String transaccionId = "123";
        double montoTransaccion = 100.0;
        double saldoInicial = 900.0;

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNumeroTarjeta("1234567890123456");
        tarjeta.setSaldo(saldoInicial);

        Transaccion transaccion = new Transaccion();
        transaccion.setId(transaccionId);
        transaccion.setTarjeta(tarjeta);
        transaccion.setMonto(montoTransaccion);
        transaccion.setFechaTransaccion(LocalDateTime.now().minusHours(1));
        transaccion.setAnulada(false);

        AnulacionRequest request = new AnulacionRequest();
        request.setTransaccionId(transaccionId);

        when(transaccionRepository.findById(transaccionId)).thenReturn(Optional.of(transaccion));
        when(transaccionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(tarjetaRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Transaccion resultado = transaccionService.anularTransaccion(request);

        // Assert
        assertTrue(resultado.isAnulada());
        assertNotNull(resultado.getFechaAnulacion());
        assertEquals(saldoInicial + montoTransaccion, tarjeta.getSaldo());
        verify(tarjetaRepository).save(tarjeta);
        verify(transaccionRepository).save(transaccion);
    }

    @Test
    void anularTransaccion_TransaccionAntigua_LanzaExcepcion() {
        // Arrange
        String transaccionId = "123";
        Transaccion transaccion = new Transaccion();
        transaccion.setId(transaccionId);
        transaccion.setFechaTransaccion(LocalDateTime.now().minusHours(25));
        transaccion.setAnulada(false);

        AnulacionRequest request = new AnulacionRequest();
        request.setTransaccionId(transaccionId);

        when(transaccionRepository.findById(transaccionId)).thenReturn(Optional.of(transaccion));

        // Act & Assert
        assertThrows(TransaccionException.class, () -> 
            transaccionService.anularTransaccion(request)
        );
    }

    @Test
    void consultarTransaccion_TransaccionExistente_RetornaTransaccion() {
        // Arrange
        String transaccionId = "123";
        Transaccion transaccion = new Transaccion();
        transaccion.setId(transaccionId);

        when(transaccionRepository.findById(transaccionId)).thenReturn(Optional.of(transaccion));

        // Act
        Transaccion resultado = transaccionService.consultarTransaccion(transaccionId);

        // Assert
        assertNotNull(resultado);
        assertEquals(transaccionId, resultado.getId());
    }

    @Test
    void consultarTransaccion_TransaccionNoExistente_LanzaExcepcion() {
        // Arrange
        String transaccionId = "123";
        when(transaccionRepository.findById(transaccionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TransaccionException.class, () -> 
            transaccionService.consultarTransaccion(transaccionId)
        );
    }
}
