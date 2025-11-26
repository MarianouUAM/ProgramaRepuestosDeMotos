package org.example.ProgramaRepuestosDeMotos.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter @Setter
public class Marca extends BaseEntity {
    @Column(length=50)
    private String nombre;

    @Column(length=50)
    private String paisOrigen;
}
