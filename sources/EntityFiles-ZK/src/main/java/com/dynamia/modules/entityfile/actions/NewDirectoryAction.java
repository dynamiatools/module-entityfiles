package com.dynamia.modules.entityfile.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.modules.entityfile.enums.EntityFileType;
import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.tools.web.actions.ActionGroup;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.util.ZKUtil;

//@InstallAction
public class NewDirectoryAction extends AbstractEntityFileAction {

	@Autowired
	private EntityFileService service;

	public NewDirectoryAction() {
		setName("Nuevo Directorio");
		setImage("icons:folder");
		setGroup(ActionGroup.get("FILES"));
	}

	@Override
	public EntityFileType getApplicableType() {
		return EntityFileType.DIRECTORY;
	}

	@Override
	public void actionPerformed(EntityFileActionEvent evt) {
		String dirName = ZKUtil.showInputDialog("Nombre directorio", String.class);
		if (dirName != null && !dirName.isEmpty()) {
			if (evt.getEntityFile() != null && evt.getEntityFile().getType() == EntityFileType.DIRECTORY) {
				service.createDirectory(evt.getEntityFile(), dirName, "");
			} else {
				service.createDirectory(evt.getTargetEntity(), dirName, "");
			}
			evt.getView().getController().reloadSelectedNode();
		} else {
			ZKUtil.showMessage("Ingrese nombre del nuevo directorio", MessageType.ERROR);
		}
	}

}
