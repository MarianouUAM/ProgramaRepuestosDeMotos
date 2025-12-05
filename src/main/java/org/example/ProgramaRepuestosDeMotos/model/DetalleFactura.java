package org.example.ProgramaRepuestosDeMotos.model;

import lombok.Getter;
import lombok.Setter;
import org.example.ProgramaRepuestosDeMotos.actions.ActualizarTotalesFactura;
import org.example.ProgramaRepuestosDeMotos.calculators.PrecioProductoCalculator;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Embeddable
@Getter @Setter
public class DetalleFactura {

    @Required
    @OnChange(ActualizarTotalesFactura.class)
    private int cantidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnChange(ActualizarTotalesFactura.class)
    private Producto producto;

    @ReadOnly
    @Money
    @DefaultValueCalculator(
            value = PrecioProductoCalculator.class,
            properties = @PropertyValue(name = "productoId", from = "producto.id")
    )
    private BigDecimal precioUnitario;

    @Money
    @ReadOnly
    @Depends("precioUnitario, cantidad")
    public BigDecimal getSubtotal() {
        if (precioUnitario == null) return BigDecimal.ZERO;
        return precioUnitario.multiply(new BigDecimal(cantidad));
    }
}
