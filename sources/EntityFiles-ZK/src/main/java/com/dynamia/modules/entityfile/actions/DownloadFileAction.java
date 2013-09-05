package com.dynamia.modules.entityfile.actions;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zul.Filedownload;

import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.modules.entityfile.enums.EntityFileType;
import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.tools.web.actions.ActionGroup;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.util.ZKUtil;

@InstallAction
public class DownloadFileAction extends AbstractEntityFileAction {

	@Autowired
	private EntityFileService service;

	public DownloadFileAction() {
		setName("Descargar Archivo");
		setImage("icons:download");
		setGroup(ActionGroup.get("FILES"));
	}

	@Override
	public EntityFileType getApplicableType() {
		return EntityFileType.FILE;
	}

	@Override
	public void actionPerformed(EntityFileActionEvent evt) {
		EntityFile file = evt.getEntityFile();

		try {
			InputStream is = service.download(file);
			Filedownload.save(is, file.getContentType(), file.getName());
			ZKUtil.showMessage("El archivo " + file.getName() + " se ha enviado a su equipo");
		} catch (FileNotFoundException e) {
			ZKUtil.showMessage("No se pudo encontrar archivo " + file.getName() + " en el servidor, por favor contacte con el administrador del sistema", MessageType.ERROR);
		}
	}

}
