package org.example.ProgramaRepuestosDeMotos.actions;

import com.openxava.naviox.actions.ForwardToOriginalURIBaseAction;
import com.openxava.naviox.impl.SignInHelper;
import org.openxava.jpa.XPersistence;
import org.openxava.util.Is;
import javax.persistence.Query;

public class SignInAction extends ForwardToOriginalURIBaseAction {

    public void execute() throws Exception {
        SignInHelper.initRequest(getRequest(), getView());

        if (getErrors().contains()) return;

        String userName = getView().getValueString("user");
        String password = getView().getValueString("password");

        if (Is.emptyString(userName, password)) {
            addError("unauthorized_user");
            return;
        }

        Query query = XPersistence.getManager().createQuery(
                "select count(e) from Usuario e where e.username = :username and e.password = :password"
        );

        query.setParameter("username", userName);
        query.setParameter("password", password);

        Long count = (Long) query.getSingleResult();

        if (count == 0L) {
            addError("unauthorized_user");
            return;
        }

        SignInHelper.signIn(getRequest(), userName);
        getView().reset();
        getContext().resetAllModulesExceptCurrent(getRequest());
        forwardToOriginalURI();
    }
}