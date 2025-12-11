package org.example.ProgramaRepuestosDeMotos.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;
import org.example.ProgramaRepuestosDeMotos.calculators.PorcentajeIVACalculator;
import org.openxava.jpa.XPersistence;

import javax.persistence.*;
import javax.validation.ValidationException;
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
                "metodoPago, cancela, cambio"
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
            "producto.codigoSKU, producto.nombre, producto.categoria.nombre, cantidad, precioUnitario, subtotal+[factura.iva, factura.subtotalBase, factura.porcentajeIVA, factura.total, factura.metodoPago, factura.cancela, factura.cambio]"
    )
    private Collection<DetalleFactura> detalles;

    @TextArea
    private String observaciones;

    @Required
    @Enumerated(EnumType.STRING)
    MetodoPago metodoPago;

    @ReadOnly
    @Money
    @Calculation("sum(detalles.subtotal)")
    private BigDecimal subtotalBase;

    @ReadOnly
    @Digits(integer=2, fraction=0)
    @DefaultValueCalculator(PorcentajeIVACalculator.class)
    private BigDecimal porcentajeIVA;

    @ReadOnly
    @Money
    @Calculation("subtotalBase * porcentajeIVA / 100")
    private BigDecimal iva;

    @ReadOnly
    @Money
    @Calculation("subtotalBase + iva")
    private BigDecimal total;

    @Money
    private BigDecimal cancela;

    @ReadOnly
    @Money
    @Calculation("cancela - total")
    private BigDecimal cambio;

    @PrePersist
    private void ejecutarAutomatizacion() {

        for (DetalleFactura d : detalles) {
            Producto p = d.getProducto();

            if (p.getStockActual() < d.getCantidad()) {
                throw new ValidationException(
                        "Inventario insuficiente para '" + p.getNombre() +
                                "'. Stock disponible: " + p.getStockActual() +
                                ", solicitado: " + d.getCantidad()
                );
            }
        }

        generarCodigoUnico();
        generarSalidasDeInventario();
    }

    private void generarCodigoUnico() {
        Query query = XPersistence.getManager()
                .createQuery("select count(f) from Factura f");
        Long cantidad = (Long) query.getSingleResult();
        if (cantidad == null) cantidad = 0L;
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
