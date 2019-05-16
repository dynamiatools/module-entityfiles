package tools.dynamia.modules.entityfile.ui.actions;

/*-
 * #%L
 * Dynamia Modules - EntityFiles - UI
 * %%
 * Copyright (C) 2016 - 2019 Dynamia Soluciones IT SAS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

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
