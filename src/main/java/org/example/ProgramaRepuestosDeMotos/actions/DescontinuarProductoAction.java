package org.example.ProgramaRepuestosDeMotos.actions;

import org.openxava.actions.*;
import org.openxava.jpa.XPersistence;
import org.openxava.model.*;
import java.util.*;
import javax.persistence.*;
import org.example.ProgramaRepuestosDeMotos.model.*;


public class DescontinuarProductoAction extends TabBaseAction {

    public void execute() throws Exception {

        Map<String, Object>[] selectedOnes = getSelectedKeys();

        if (selectedOnes != null) {
            for (Map<String, Object> key : selectedOnes) {

                Producto p = (Producto) MapFacade.findEntity("Producto", key);

                p.setEstado(EstadoProducto.AGOTADO);

                XPersistence.getManager().merge(p);
            }
        }

        addMessage("Se han descontinuado " + selectedOnes.length + " productos.");
        getTab().deselectAll();
        getTab().reset();
    }
}