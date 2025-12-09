package org.example.ProgramaRepuestosDeMotos.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import org.openxava.jpa.XPersistence;
import javax.persistence.*;
import javax.validation.ValidationException;
import java.math.BigDecimal;

@Entity
@Getter @Setter
@View(members =
        "codigoSKU, nombre;" + "categoria, marca, modelo;" + "descripcion;" + "precioVenta, costoCompra;" + "stockActual, estado;" +
                "fotos"
)
public class Producto extends BaseEntity {
    @SearchKey
    @Column(length=15)
    @ReadOnly
    private String codigoSKU;

    @Column(length=60)
    @Required
    private String nombre;

    @TextArea
    private String descripcion;

    @Money
    private BigDecimal precioVenta;

    @Money
    private BigDecimal costoCompra;
    private int stockActual = 0;

    @Enumerated(EnumType.STRING)
    private EstadoProducto estado;

    @Files
    @Column(length=32)
    private String fotos;

    @ManyToOne(fetch=FetchType.LAZY)
    @DescriptionsList
    private Categoria categoria;

    @ManyToOne(fetch=FetchType.LAZY)
    @DescriptionsList
    private Marca marca;

    @ManyToOne(fetch=FetchType.LAZY)
    @DescriptionsList
    private ModeloMoto modelo;

    public void aumentarStock(int cantidad) {
        this.stockActual += cantidad;
        if (this.stockActual > 0 && this.estado == EstadoProducto.AGOTADO) {
            this.estado = EstadoProducto.DISPONIBLE;
        }
    }

    public void disminuirStock(int cantidad) {
        if (this.stockActual < cantidad) {
            throw new ValidationException("Stock insuficiente: Intentas vender " + cantidad +
                    " unidades de '" + this.nombre +
                    "', pero solo tienes " + this.stockActual + " en inventario.");
        }
        this.stockActual -= cantidad;
        if (this.stockActual == 0) {
            this.estado = EstadoProducto.AGOTADO;
        }
    }

    @PrePersist
    private void generarCodigoAutomatico() {
        if (this.codigoSKU == null || this.codigoSKU.isEmpty()) {
            Query query = XPersistence.getManager().createQuery("select count(p) from Producto p");
            Long cantidad = (Long) query.getSingleResult();
            this.codigoSKU = "PROD-" + String.format("%04d", cantidad + 1);
        }
    }
}