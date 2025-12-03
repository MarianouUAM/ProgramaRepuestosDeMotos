package org.example.ProgramaRepuestosDeMotos.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import org.openxava.annotations.*;

@Entity
@Getter @Setter
public class ModeloMoto extends BaseEntity {

    @Column(length = 50)
    @Required
    private String nombre;

}