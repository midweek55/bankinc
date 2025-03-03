package com.bankinc.cards.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Tarjeta {
    @Id
    private String numeroTarjeta;
    private String titular;
    private LocalDateTime fechaCreacion;
    private boolean activa;
    private double saldo;

    public Tarjeta() {
    }

    public boolean tieneSaldoSuficiente(double monto) {
        return saldo >= monto;
    }
}
