package org.example.ProgramaRepuestosDeMotos.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter @Setter
public class Producto extends BaseEntity {

    @SearchKey // Esto activa la búsqueda por código
    @Column(length=10)
    private String codigoSKU;

    @Column(length=60)
    private String nombre;

    @TextArea
    private String descripcion;

    @Money
    private BigDecimal precioVenta;

    @Money
    private BigDecimal costoCompra;

    private int stockActual;

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
}
