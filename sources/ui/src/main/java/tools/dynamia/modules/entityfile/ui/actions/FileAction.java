
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

/**
 *
 * @author programador
 */
import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.Messages;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.modules.entityfile.ui.util.EntityFileUtils;

/**
 *
 * @author Mario Serrano Leones
 */
@InstallAction
public class FileAction extends AbstractCrudAction {

    public FileAction() {
        setName(Messages.get(getClass(), "Archivos"));
        setImage("attachment");
        setGroup(ActionGroup.get("CRUD_OTHER"));
        setMenuSupported(true);

    }

    @Override
    public CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ);
    }

    @Override
    public void actionPerformed(CrudActionEvent evt) {
        Object obj = evt.getData();
        EntityFileUtils.showFileExplorer(obj);

    }
}
