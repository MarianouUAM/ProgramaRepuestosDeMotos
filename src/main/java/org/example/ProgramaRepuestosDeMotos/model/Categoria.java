package org.example.ProgramaRepuestosDeMotos.model;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter @Setter
public class Categoria extends BaseEntity {
    @Column(length=50)
    private String nombre;

    @Column(length=100)
    private String descripcion;
}