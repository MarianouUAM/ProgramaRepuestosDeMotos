package org.example.ProgramaRepuestosDeMotos.actions;

import org.openxava.actions.ViewBaseAction;

public class BloquearEdicionAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        getView().setEditable(false);

        getView().setKeyEditable(false);
    }
}
