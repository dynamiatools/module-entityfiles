package com.dynamia.modules.entityfile.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zhtml.Messagebox;

import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.tools.web.actions.ActionGroup;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.util.ZKUtil;

@InstallAction
public class DeleteFileAction extends AbstractEntityFileAction {

	@Autowired
	private EntityFileService service;

	public DeleteFileAction() {
		setName("Borrar");
		setImage("icons:delete");
		setGroup(ActionGroup.get("FILES"));
	}

	@Override
	public void actionPerformed(EntityFileActionEvent evt) {
		try {
			if (evt.getEntityFile() != null) {
				int op = ZKUtil.showQuestion("¿Esta seguro que desea borrar el archivo/directorio " + evt.getEntityFile().getName() + "?", "Confirmar");
				if (op == Messagebox.YES) {
					service.delete(evt.getEntityFile());
					evt.getView().getController().removeSelectedNode();
					ZKUtil.showMessage("Archivo borrado correctamente");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ZKUtil.showMessage("Error al borrar archivo", MessageType.ERROR);
		}
	}
}
