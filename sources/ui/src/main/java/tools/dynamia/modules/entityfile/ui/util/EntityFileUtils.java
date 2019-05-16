package tools.dynamia.modules.entityfile.ui.util;

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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.zkoss.util.media.Media;
import org.zkoss.zul.A;

import tools.dynamia.domain.AbstractEntity;
import tools.dynamia.modules.entityfile.StoredEntityFile;
import tools.dynamia.modules.entityfile.UploadedFileInfo;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.ui.EntityFileController;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.viewers.util.Viewers;
import tools.dynamia.zk.crud.CrudView;
import tools.dynamia.zk.util.ZKUtil;

public class EntityFileUtils {

	public static UploadedFileInfo build(Media media) {
		InputStream is = null;
		if (media.isBinary()) {
			is = media.getStreamData();
		} else {
			is = new ByteArrayInputStream(media.getStringData().getBytes());
		}

		UploadedFileInfo info = new UploadedFileInfo(media.getName(), media.getContentType(), is);

		return info;
	}

	public static void showFileExplorer(Object obj) {
		if (obj != null) {

			CrudView view = (CrudView) Viewers.getView(EntityFile.class, "crud", null);

			view.setHeight(null);
			EntityFileController controller = (EntityFileController) view.getController();
			controller.setTargetEntity(obj);
			controller.doQuery();
			ZKUtil.showDialog("Archivos Asociados", view, "80%", "80%");

		} else {
			UIMessages.showMessage("Debe seleccionar un elemento para ver los archivos asociados", MessageType.INFO);
		}
	}

	public static void showDownloadDialog(StoredEntityFile sef) {
		A downloadLink = new A("Descargar " + sef.getEntityFile().getName());
		downloadLink.setHref(sef.getUrl());
		downloadLink.setTarget("_blank");

		ZKUtil.showDialog("Descarga de Archivo", downloadLink, "300px", "200px");

		UIMessages.showMessage("Clic en el link para descargar  " + sef.getEntityFile().getName());

	}

}
