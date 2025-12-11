package org.example.ProgramaRepuestosDeMotos.calculators;

import org.openxava.calculators.ICalculator;

import java.math.BigDecimal;

public class PorcentajeIVACalculator implements ICalculator {
    @Override
    public Object calculate() throws Exception {
        return new BigDecimal("15");
    }
}
