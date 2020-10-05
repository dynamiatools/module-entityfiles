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
import org.zkoss.util.media.Media;
import org.zkoss.zul.Fileupload;

import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.modules.entityfile.UploadedFileInfo;
import tools.dynamia.modules.entityfile.service.EntityFileService;
import tools.dynamia.modules.entityfile.ui.util.EntityFileUtils;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class NewFileAction extends AbstractEntityFileAction {

	@Autowired
	private EntityFileService service;

	public NewFileAction() {
		setName("Nuevo Archivo");
		setImage("icons:add");
		setGroup(ActionGroup.get("FILES"));
	}

	@Override
	public void actionPerformed(final EntityFileActionEvent evt) {

		Fileupload.get(10, event -> {

			Media[] medias = event.getMedias();
			if (medias != null) {
				for (Media media : medias) {
					UploadedFileInfo info = EntityFileUtils.build(media);
					info.setParent(evt.getEntityFile());
					service.createEntityFile(info, evt.getTargetEntity());
				}
				evt.getCrudView().getController().doQuery();
				UIMessages.showMessage("Archivo(s) Cargado(s) Correctamente");
			} else {
				UIMessages.showMessage("Debe seleccionar al menos un archivo", MessageType.ERROR);
			}
		});

	}
}
