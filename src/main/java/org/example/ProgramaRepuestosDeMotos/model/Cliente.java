package org.example.ProgramaRepuestosDeMotos.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;
import org.example.ProgramaRepuestosDeMotos.calculators.NumeroClienteCalculator;

@Entity
@Getter @Setter
@View(name="Simple", members="nombreCompleto; cedula; telefono")
public class Cliente extends Persona {

    @Column(length=30)
    @ReadOnly
    @DefaultValueCalculator(NumeroClienteCalculator.class)
    private String nroCliente;

}