package tools.dynamia.modules.entityfile.ui.actions;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.enums.EntityFileType;
import tools.dynamia.modules.entityfile.service.EntityFileService;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class DeleteFileAction extends AbstractEntityFileAction {

    @Autowired
    private EntityFileService service;

    public DeleteFileAction() {
        setName("Borrar");
        setImage("icons:delete");
        setGroup(ActionGroup.get("FILES"));
        setMenuSupported(true);
    }

    @Override
    public void actionPerformed(final EntityFileActionEvent evt) {
        try {
            final EntityFile entityFile = evt.getEntityFile();
            if (entityFile != null) {
                UIMessages.showQuestion("Esta seguro que desea borrar el archivo/directorio " + entityFile.getName() + "?", () -> {
				    service.delete(entityFile);
				    if (entityFile.getType() == EntityFileType.DIRECTORY) {
				        UIMessages.showMessage("Carpeta borrada correctamente");
				    } else {
				        UIMessages.showMessage("Archivo borrado correctamente");
				    }
				    evt.getController().doQuery();
				});

            } else {
                UIMessages.showMessage("Seleccion archivo para borrar", MessageType.WARNING);
            }
        } catch (Exception e) {
            e.printStackTrace();
            UIMessages.showMessage("Error al borrar archivo", MessageType.ERROR);
        }
    }

}
