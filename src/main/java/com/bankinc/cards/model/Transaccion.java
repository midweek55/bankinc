package com.bankinc.cards.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Transaccion {
    @Id
    private String id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Tarjeta tarjeta;
    
    private double monto;
    private LocalDateTime fechaTransaccion;
    private boolean anulada;
    private LocalDateTime fechaAnulacion;

    public Transaccion() {
    }
}
