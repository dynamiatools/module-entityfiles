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

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.actions.ReadableOnly;
import tools.dynamia.modules.entityfile.StoredEntityFile;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class DownloadFileAction extends AbstractEntityFileAction implements ReadableOnly {

    public DownloadFileAction() {
        setName("Descargar Archivo");
        setImage("icons:download");
        setGroup(ActionGroup.get("FILES"));
        setMenuSupported(true);
    }

    @Override
    public void actionPerformed(EntityFileActionEvent evt) {

        EntityFile file = evt.getEntityFile();
        if (file != null) {

            StoredEntityFile sef = file.getStoredEntityFile();
            if (sef != null && sef.getUrl() != null) {
                download(sef);
            } else {
                UIMessages.showMessage("No se pudo encontrar archivo " + file.getName()
                        + " en el servidor, por favor contacte con el administrador del sistema", MessageType.ERROR);
            }

        } else {
            UIMessages.showMessage("Seleccion archivo para descargar", MessageType.WARNING);
        }

    }

    private void download(StoredEntityFile sef) {

        Execution exec = Executions.getCurrent();
        exec.sendRedirect(sef.getUrl(), "_blank");

    }
}
