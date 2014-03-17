package com.dynamia.modules.entityfile.actions;

import com.dynamia.modules.entityfile.EntityFileController;
import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.tools.commons.ApplicableClass;
import com.dynamia.tools.web.crud.AbstractCrudAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;

public abstract class AbstractEntityFileAction extends AbstractCrudAction {

    @Override
    public void actionPerformed(CrudActionEvent evt) {
        EntityFileController controller = (EntityFileController) evt.getController();
        actionPerformed(new EntityFileActionEvent(controller.getTargetEntity(), evt.getData(), evt.getSource(), evt.getView(), controller));
    }

    public abstract void actionPerformed(EntityFileActionEvent evt);

    @Override
    public ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(EntityFile.class);
    }

    @Override
    public CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ);
    }

}
