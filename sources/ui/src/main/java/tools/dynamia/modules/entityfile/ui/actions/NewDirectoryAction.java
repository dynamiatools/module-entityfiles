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

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.modules.entityfile.enums.EntityFileType;
import tools.dynamia.modules.entityfile.service.EntityFileService;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.ui.InputPanel;

@InstallAction
public class NewDirectoryAction extends AbstractEntityFileAction {

    @Autowired
    private EntityFileService service;

    public NewDirectoryAction() {
        setName("Nuevo Directorio");
        setImage("folder2");
        setGroup(ActionGroup.get("FILES"));
        setMenuSupported(true);
    }

    @Override
    public void actionPerformed(final EntityFileActionEvent evt) {
        InputPanel inputPanel = new InputPanel("Nombre de Directorio", "", String.class);
        inputPanel.showDialog();
        inputPanel.addEventListener(InputPanel.ON_INPUT, event -> {
            String dirName = (String) event.getData();
            if (dirName != null && !dirName.isEmpty()) {
                if (evt.getEntityFile() != null && evt.getEntityFile().getType() == EntityFileType.DIRECTORY) {
                    service.createDirectory(evt.getEntityFile(), dirName, "");
                } else {
                    service.createDirectory(evt.getTargetEntity(), dirName, "");
                }
                evt.getCrudView().getController().doQuery();
            } else {
                UIMessages.showMessage("Ingrese nombre del nuevo directorio", MessageType.ERROR);
            }
        });

    }

}
