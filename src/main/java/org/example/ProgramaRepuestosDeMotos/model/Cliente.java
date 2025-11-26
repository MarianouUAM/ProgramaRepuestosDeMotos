package org.example.ProgramaRepuestosDeMotos.model;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;

@Entity
@Getter @Setter
@View(name="Simple", members="nombreCompleto; cedula; telefono")
public class Cliente extends Persona {
    @Column(length=30)
    private String nroCliente;
}
