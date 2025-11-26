package org.example.ProgramaRepuestosDeMotos.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter @Setter
public class Proveedor extends BaseEntity {
    @Column(length=60)
    private String nombreEmpresa;

    @Column(length=60)
    private String contactoVentas;

    @Column(length=20)
    private String telefono;

    @Column(length=100)
    private String direccion;
}
