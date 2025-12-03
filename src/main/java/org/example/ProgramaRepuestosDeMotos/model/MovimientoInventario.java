package org.example.ProgramaRepuestosDeMotos.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;
import org.openxava.jpa.XPersistence;
import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
public class MovimientoInventario extends BaseEntity {

    @DefaultValueCalculator(CurrentDateCalculator.class)
    @ReadOnly
    private Date fecha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Producto producto;

    @Required
    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo;

    @Required
    private int cantidad;

    @Column(length = 50)
    private String referencia;

    @PrePersist
    private void actualizarStockProducto() {
        if (producto == null) return;

        Producto prod = XPersistence.getManager().merge(producto);

        if (tipo == TipoMovimiento.ENTRADA) {
            prod.aumentarStock(cantidad);
        } else {
            prod.disminuirStock(cantidad);
        }
    }
}