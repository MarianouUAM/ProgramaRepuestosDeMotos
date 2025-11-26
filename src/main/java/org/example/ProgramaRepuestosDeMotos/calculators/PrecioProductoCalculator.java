package org.example.ProgramaRepuestosDeMotos.calculators;

import org.example.ProgramaRepuestosDeMotos.model.Producto;
import org.openxava.calculators.ICalculator;
import lombok.Getter;
import lombok.Setter;

import org.openxava.jpa.XPersistence;

public class PrecioProductoCalculator implements ICalculator {

    @Getter @Setter
    private String productoId;

    @Override
    public Object calculate() throws Exception {
        if (productoId == null) return null;
        Producto producto = XPersistence.getManager().find(Producto.class, productoId);
        return producto != null ? producto.getPrecioVenta() : null;
    }
}