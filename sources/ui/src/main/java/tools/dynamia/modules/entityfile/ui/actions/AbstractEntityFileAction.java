/*
 * Copyright (C)  2020. Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 *  All Rights Reserved.
 *
 * This file is free software: you can redistribute it and/or modify it  under the terms of the
 *  GNU Lesser General Public License (LGPL v3) as published by the Free Software Foundation,
 *   either version 3 of the License, or (at your option) any later version.
 *
 *  This file is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *   without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *   See the GNU Lesser General Public License for more details. You should have received a copy of the
 *   GNU Lesser General Public License along with this file.
 *   If not, see <https://www.gnu.org/licenses/>.
 *
 */

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
        actionPerformed(new EntityFileActionEvent(controller.getTargetEntity(), evt.getData(), evt.getSource(), evt.getCrudView(), controller));
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
