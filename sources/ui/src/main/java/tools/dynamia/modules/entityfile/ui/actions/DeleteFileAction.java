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
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.enums.EntityFileType;
import tools.dynamia.modules.entityfile.service.EntityFileService;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class DeleteFileAction extends AbstractEntityFileAction {

    @Autowired
    private EntityFileService service;

    public DeleteFileAction() {
        setName("Borrar");
        setImage("icons:delete");
        setGroup(ActionGroup.get("FILES"));
        setMenuSupported(true);
    }

    @Override
    public void actionPerformed(final EntityFileActionEvent evt) {
        try {
            final EntityFile entityFile = evt.getEntityFile();
            if (entityFile != null) {
                UIMessages.showQuestion("Esta seguro que desea borrar el archivo/directorio " + entityFile.getName() + "?", () -> {
				    service.delete(entityFile);
				    if (entityFile.getType() == EntityFileType.DIRECTORY) {
				        UIMessages.showMessage("Carpeta borrada correctamente");
				    } else {
				        UIMessages.showMessage("Archivo borrado correctamente");
				    }
				    evt.getController().doQuery();
				});

            } else {
                UIMessages.showMessage("Seleccion archivo para borrar", MessageType.WARNING);
            }
        } catch (Exception e) {
            e.printStackTrace();
            UIMessages.showMessage("Error al borrar archivo", MessageType.ERROR);
        }
    }

}
