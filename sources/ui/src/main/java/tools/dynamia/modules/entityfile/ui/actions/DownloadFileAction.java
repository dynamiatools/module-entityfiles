/*
 * Copyright (C) 2023 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia / South America
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
