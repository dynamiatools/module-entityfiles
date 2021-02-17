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

package tools.dynamia.modules.entityfile.ui.util;

import org.zkoss.util.media.Media;
import org.zkoss.zul.A;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;
import tools.dynamia.modules.entityfile.StoredEntityFile;
import tools.dynamia.modules.entityfile.UploadedFileInfo;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.ui.EntityFileController;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.viewers.util.Viewers;
import tools.dynamia.zk.crud.CrudView;
import tools.dynamia.zk.util.ZKUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
            ZKUtil.showDialog("Archivos Asociados - " + obj.toString(), view, "85%", "80%");

        } else {
            UIMessages.showMessage("Debe seleccionar un elemento para ver los archivos asociados", MessageType.INFO);
        }
    }

    public static void showDownloadDialog(StoredEntityFile sef) {
        Vlayout vlayout = new Vlayout();
        vlayout.setStyle("text-align: center; background: white !important; margin: 10px");
        vlayout.appendChild(new Label(sef.getEntityFile().getName()));
        vlayout.appendChild(new Label(sef.getEntityFile().getDescription()));

        A downloadLink = new A("Descargar ");
        downloadLink.setHref(sef.getUrl());
        downloadLink.setTarget("_blank");
        downloadLink.setZclass("btn btn-primary");
        downloadLink.setIconSclass("fa fa-arrow-down");
        downloadLink.setStyle("margin: 20px");
        vlayout.appendChild(downloadLink);

        Window window = ZKUtil.showDialog("Descargar Archivo", vlayout, "400px", null);
        window.setStyle("background: white !important");
        window.setContentStyle("background: white !important");

        UIMessages.showMessage("Clic en el link para descargar  " + sef.getEntityFile().getName());

    }

}
