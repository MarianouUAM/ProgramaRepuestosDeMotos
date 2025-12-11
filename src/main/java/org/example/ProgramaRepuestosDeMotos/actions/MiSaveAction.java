package org.example.ProgramaRepuestosDeMotos.actions;

import org.openxava.actions.SaveAction;
import org.openxava.validators.ValidationException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.ObjectNotFoundException;
import java.util.Map;

public class MiSaveAction extends SaveAction {

    @Override
    public void execute() throws Exception {
        try {
            if (getView().isKeyEditable()) {
                Map values = create();
                updateView(values, isResetAfterOnCreate());
            } else {
                Map values = modify();
                updateView(values, isResetAfterOnModify());
            }

            resetDescriptionsCache();
        } catch (ValidationException ex) {
            addErrors(ex.getErrors());
        } catch (ObjectNotFoundException ex) {
            addError("no_modify_no_exists");
        } catch (DuplicateKeyException ex) {
            addError("no_create_exists");
        }
    }
}