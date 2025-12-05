package org.example.ProgramaRepuestosDeMotos.actions;

import org.openxava.actions.OnChangePropertyBaseAction;

public class ActualizarTotalesFactura extends OnChangePropertyBaseAction {
    @Override
    public void execute() throws Exception {
        resetDescriptionsCache();
    }
}
