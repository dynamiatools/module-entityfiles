package com.dynamia.modules.entityfile.actions;

import com.dynamia.modules.entityfile.enums.EntityFileType;
import com.dynamia.tools.web.actions.AbstractAction;
import com.dynamia.tools.web.actions.ActionEvent;

public abstract class AbstractEntityFileAction extends AbstractAction implements EntityFileAction {

    @Override
    public void actionPerformed(ActionEvent evt) {
        actionPerformed((EntityFileActionEvent) evt);
    }

    @Override
    public EntityFileType getApplicableType() {
        return null;
    }

    public abstract void actionPerformed(EntityFileActionEvent evt);
}
