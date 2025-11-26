package org.example.ProgramaRepuestosDeMotos.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date; // <--- CAMBIO IMPORTANTE: Usar java.util.Date

@Entity
@Getter @Setter
public class Factura extends BaseEntity {

    @DefaultValueCalculator(CurrentDateCalculator.class)
    @ReadOnly
    private Date fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @ReferenceView("Simple")
    private Cliente cliente;

    @ElementCollection
    @ListProperties("producto.codigoSKU, producto.nombre, cantidad, precioUnitario, subtotal")
    private Collection<DetalleFactura> detalles;

    @TextArea
    private String observaciones;

    @Money
    @ReadOnly
    public BigDecimal getTotal() {
        BigDecimal result = BigDecimal.ZERO;
        if (detalles != null) {
            for (DetalleFactura detalle : detalles) {
                result = result.add(detalle.getSubtotal());
            }
        }
        return result;
    }
}