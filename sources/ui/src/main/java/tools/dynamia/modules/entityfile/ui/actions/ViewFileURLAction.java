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

import org.zkoss.zul.Messagebox;
import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.actions.ReadableOnly;

@InstallAction
public class ViewFileURLAction extends AbstractEntityFileAction implements ReadableOnly {

    public ViewFileURLAction() {
        setName("URL");
        setGroup(ActionGroup.get("FILES"));
        setMenuSupported(true);
    }

    @Override
    public void actionPerformed(EntityFileActionEvent evt) {
        if (evt.getEntityFile() != null) {
            Messagebox.show(evt.getEntityFile().getStoredEntityFile().getUrl());
        }
    }
}
