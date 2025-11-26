package org.example.ProgramaRepuestosDeMotos.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@MappedSuperclass
@Getter @Setter
public abstract class Persona extends BaseEntity {

    @Column(length=60)
    private String nombreCompleto;

    @Column(length=20)
    private String cedula;

    @Column(length=20)
    private String telefono;

    @Column(length=100)
    private String direccion;

    @Column(length=50)
    private String email;
}
