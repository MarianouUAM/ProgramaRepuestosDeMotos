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
        "codigoSKU, nombre;" +
                "categoria, marca, modelo;" +
                "descripcion;" +
                "precioVenta, costoCompra;" +
                "stockActual, estado;" +
                "fotos"
)
@View(name="AnalisisFinanciero", members=
        "Datos Generales {" +
                "codigoSKU, nombre;" +
                "fotos;" +
                "} " +
                "Métricas de Inventario {" +
                "costoCompra, precioVenta;" +
                "stockActual, totalUnidadesVendidas;" +
                "} " +
                "Balance Financiero {" +
                "totalInvertido, dineroRecuperado;" +
                "estadoRentabilidad;" +
                "}"
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
    @Transient
    @Hidden
    public int getTotalUnidadesVendidas() {
        try {
            String jpql = "select sum(d.cantidad) from Factura f join f.detalles d " +
                    "where d.producto.id = :id";
            Long cantidad = (Long) XPersistence.getManager()
                    .createQuery(jpql)
                    .setParameter("id", this.getId())
                    .getSingleResult();

            return cantidad == null ? 0 : cantidad.intValue();
        } catch (Exception e) {
            return 0;
        }
    }
    @Transient
    @Stereotype("MONEY")
    public BigDecimal getDineroRecuperado() {
        try {
            String jpql = "select sum(d.cantidad * d.precioUnitario) from Factura f join f.detalles d " +
                    "where d.producto.id = :id";
            BigDecimal total = (BigDecimal) XPersistence.getManager()
                    .createQuery(jpql)
                    .setParameter("id", this.getId())
                    .getSingleResult();

            return total == null ? BigDecimal.ZERO : total;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    @Transient
    @Stereotype("MONEY")
    public BigDecimal getTotalInvertido() {
        if (costoCompra == null) return BigDecimal.ZERO;

        int totalHistorico = this.stockActual + getTotalUnidadesVendidas();
        return costoCompra.multiply(new BigDecimal(totalHistorico));
    }

    @Transient
    public String getEstadoRentabilidad() {
        BigDecimal invertido = getTotalInvertido();
        BigDecimal recuperado = getDineroRecuperado();

        if (invertido.compareTo(BigDecimal.ZERO) == 0) return "Costo no registrado";
        if (recuperado.compareTo(invertido) >= 0) {
            return "RENTABILIDAD POSITIVA ";
        } else {
            return "RETORNO DE INVERSIÓN PENDIENTE";
        }
    }
}