package com.dynamia.modules.entityfile.actions;

import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.modules.entityfile.enums.EntityFileType;
import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.tools.web.actions.ActionGroup;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;
import com.dynamia.tools.web.util.Callback;

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
                UIMessages.showQuestion("Esta seguro que desea borrar el archivo/directorio " + entityFile.getName() + "?", new Callback() {

                    @Override
                    public void doSomething() {
                        service.delete(entityFile);
                        if (entityFile.getType() == EntityFileType.DIRECTORY) {
                            UIMessages.showMessage("Carpeta borrada correctamente");
                        } else {
                            UIMessages.showMessage("Archivo borrado correctamente");
                        }
                        evt.getController().doQuery();
                    }
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
