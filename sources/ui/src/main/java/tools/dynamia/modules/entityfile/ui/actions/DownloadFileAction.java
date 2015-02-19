package tools.dynamia.modules.entityfile.ui.actions;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zul.Filedownload;

import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.service.EntityFileService;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class DownloadFileAction extends AbstractEntityFileAction {

    @Autowired
    private EntityFileService service;

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
            try {
                InputStream is = service.download(file);
                Filedownload.save(is, file.getContentType(), file.getName());
                UIMessages.showMessage("El archivo " + file.getName() + " se ha enviado a su equipo");
            } catch (FileNotFoundException e) {
                UIMessages.showMessage("No se pudo encontrar archivo " + file.getName() + " en el servidor, por favor contacte con el administrador del sistema", MessageType.ERROR);
            }
        } else {
            UIMessages.showMessage("Seleccion archivo para descargar", MessageType.WARNING);
        }

    }

}
