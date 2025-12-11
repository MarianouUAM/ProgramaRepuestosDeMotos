package org.example.ProgramaRepuestosDeMotos.calculators;

import org.openxava.calculators.*;
import org.openxava.jpa.*;
import javax.persistence.*;
import lombok.*;
import org.openxava.calculators.ICalculator;

public class NumeroClienteCalculator implements ICalculator {public Object calculate() throws Exception {
    Query query = XPersistence.getManager()
            .createQuery("select max(c.nroCliente) from Cliente c");

    String maxStr = (String) query.getSingleResult();

    int max = 0;
    if (maxStr != null) {
        try {
            max = Integer.parseInt(maxStr);
        } catch (NumberFormatException e) {
            max = 0;
        }
    }
    return String.valueOf(max + 1);
}
}