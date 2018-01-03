package tools.dynamia.modules.entityfile.ui.util;

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
