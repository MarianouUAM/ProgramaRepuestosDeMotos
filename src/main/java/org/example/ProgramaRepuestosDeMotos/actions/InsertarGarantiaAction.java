package org.example.ProgramaRepuestosDeMotos.actions;
import org.openxava.actions.ViewBaseAction;

public class InsertarGarantiaAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {

        String observacionesActuales = getView().getValueString("observaciones");

        if (observacionesActuales == null) {
            observacionesActuales = "";
        }
        String textoGarantia = "\n\n--- TÉRMINOS Y GARANTÍA ---\n" +
                "1. Garantía requiere instalación profesional comprobable; el montaje casero la anula.\n" +
                "2. Cambios solo dentro de las 24 horas.\n" +
                "3. Es obligatorio presentar esta factura para reclamos.";

        getView().setValue("observaciones", observacionesActuales + textoGarantia);

        addMessage("Términos de garantía agregados a la factura.");
    }
}