package tools.dynamia.modules.entityfile.ui.actions;

import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.ui.EntityFileController;

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
