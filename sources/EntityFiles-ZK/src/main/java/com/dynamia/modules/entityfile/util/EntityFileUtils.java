package com.dynamia.modules.entityfile.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.dynamia.modules.entityfile.EntityFileController;

import org.zkoss.util.media.Media;

import com.dynamia.modules.entityfile.UploadedFileInfo;
import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.tools.domain.AbstractEntity;
import com.dynamia.tools.viewers.zk.ui.Viewer;
import com.dynamia.tools.web.crud.CrudView;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;
import com.dynamia.tools.web.util.ZKUtil;

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

			Viewer viewer = new Viewer("crud", EntityFile.class);
			CrudView view = (CrudView) viewer.getView();
			EntityFileController controller = (EntityFileController) view.getController();
			controller.setTargetEntity((AbstractEntity) obj);
			controller.doQuery();
			ZKUtil.showDialog("Archivos Asociados", viewer, "80%", "80%");

		} else {
			UIMessages.showMessage("Debe seleccionar un elemento para ver los archivos asociados", MessageType.INFO);
		}
	}

}
