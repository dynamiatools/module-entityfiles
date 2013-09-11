package com.dynamia.modules.entityfile.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.util.media.Media;
import org.zkoss.zul.Fileupload;

import com.dynamia.modules.entityfile.UploadedFileInfo;
import com.dynamia.modules.entityfile.enums.EntityFileType;
import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.modules.entityfile.util.EntityFileUtils;
import com.dynamia.tools.web.actions.ActionGroup;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.util.ZKUtil;

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
	public EntityFileType getApplicableType() {
		return EntityFileType.DIRECTORY;
	}

	@Override
	public void actionPerformed(EntityFileActionEvent evt) {

		Media[] medias = Fileupload.get(-1);
		if (medias != null) {
			for (Media media : medias) {
				UploadedFileInfo info = EntityFileUtils.build(media);
				info.setParent(evt.getEntityFile());
				service.createEntityFile(info, evt.getTargetEntity());
			}
			evt.getView().getController().reloadSelectedNode();
			ZKUtil.showMessage("Archivos Cargados Correctamente");
		} else {
			ZKUtil.showMessage("Debe seleccionar al menos un archivo", MessageType.ERROR);
		}

	}
}
