package tools.dynamia.modules.entityfile.ui.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.modules.entityfile.StoredEntityFile;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class DownloadFileAction extends AbstractEntityFileAction {

	public DownloadFileAction() {
		setName("Descargar Archivo");
		setImage("icons:download");
		setGroup(ActionGroup.get("FILES"));
		setMenuSupported(true);
	}

	@Override
	public void actionPerformed(EntityFileActionEvent evt) {

		EntityFile file = evt.getEntityFile();
		if (file != null) {

			StoredEntityFile sef = file.getStoredEntityFile();
			if (sef != null && sef.getUrl() != null) {
				download(sef);
			} else {
				UIMessages.showMessage("No se pudo encontrar archivo " + file.getName()
						+ " en el servidor, por favor contacte con el administrador del sistema", MessageType.ERROR);
			}

		} else {
			UIMessages.showMessage("Seleccion archivo para descargar", MessageType.WARNING);
		}

	}

	private void download(StoredEntityFile sef) {

		Execution exec = Executions.getCurrent();
		exec.sendRedirect(sef.getUrl(), "_blank");

	}
}
