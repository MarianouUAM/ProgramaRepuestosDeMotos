package org.example.ProgramaRepuestosDeMotos.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;

@Entity
@Getter @Setter
public class Usuario extends BaseEntity {

    @Column(length=30)
    @Required
    private String username;

    @Column(length=30)
    @Stereotype("PASSWORD")
    @Required
    private String password;

}
