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
				evt.getView().getController().doQuery();
				UIMessages.showMessage("Archivo(s) Cargado(s) Correctamente");
			} else {
				UIMessages.showMessage("Debe seleccionar al menos un archivo", MessageType.ERROR);
			}
		});

	}
}
