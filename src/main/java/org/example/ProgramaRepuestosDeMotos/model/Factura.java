package org.example.ProgramaRepuestosDeMotos.model;


import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;
import org.example.ProgramaRepuestosDeMotos.calculators.PorcentajeIVACalculator;
import org.openxava.jpa.XPersistence;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

@Entity
@Getter @Setter
@View(members =
        "numeroFactura, fecha;" +
                "cliente;" +
                "detalles;" +
                "observaciones;" +
                "subtotalBase, porcentajeIVA, iva;" +
                "total;" +
                "cancela, cambio"
)
public class Factura extends BaseEntity {

    @Column(length = 20)
    @ReadOnly
    private String numeroFactura;

    @DefaultValueCalculator(CurrentDateCalculator.class)
    @ReadOnly
    private Date fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @ReferenceView("Simple")
    private Cliente cliente;

    @ElementCollection
    @ListProperties(
            "producto.codigoSKU, producto.nombre, producto.categoria.nombre, cantidad, precioUnitario, subtotal+[factura.iva, factura.subtotalBase, factura.porcentajeIVA, factura.total, factura.cancela, factura.cambio]"
    )
    private Collection<DetalleFactura> detalles;

    @TextArea
    private String observaciones;

    @ReadOnly
    @Money
    @Calculation("sum(detalles.subtotal)")
     BigDecimal subtotalBase;

    @ReadOnly
    @Digits(integer=2, fraction=0)
    @DefaultValueCalculator(PorcentajeIVACalculator.class)
     BigDecimal porcentajeIVA;

    @ReadOnly
    @Money
    @Calculation("subtotalBase * porcentajeIVA / 100")
     BigDecimal iva;

    @ReadOnly
    @Money
    @Calculation("subtotalBase + iva")
     BigDecimal total;

    @Money
    private BigDecimal cancela;

    @ReadOnly
    @Money
    @Calculation("cancela - total")
     BigDecimal cambio;

    @PrePersist
    private void ejecutarAutomatizacion() {
        generarCodigoUnico();
        generarSalidasDeInventario();
    }

    private void generarCodigoUnico() {
        Query query = XPersistence.getManager()
                .createQuery("select count(f) from Factura f");
        Long cantidad = (Long) query.getSingleResult();
        this.numeroFactura = "FAC-" + String.format("%05d", cantidad + 1);
    }

    private void generarSalidasDeInventario() {
        if (detalles == null) return;

        for (DetalleFactura d : detalles) {
            MovimientoInventario movimiento = new MovimientoInventario();
            movimiento.setProducto(d.getProducto());
            movimiento.setCantidad(d.getCantidad());
            movimiento.setTipo(TipoMovimiento.SALIDA);
            movimiento.setFecha(new Date());
            movimiento.setReferencia("Venta Automática: " + this.numeroFactura);
            XPersistence.getManager().persist(movimiento);
        }
    }
}