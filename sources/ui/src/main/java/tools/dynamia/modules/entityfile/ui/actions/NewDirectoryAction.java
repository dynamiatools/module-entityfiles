package tools.dynamia.modules.entityfile.ui.actions;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.modules.entityfile.enums.EntityFileType;
import tools.dynamia.modules.entityfile.service.EntityFileService;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.ui.InputPanel;

@InstallAction
public class NewDirectoryAction extends AbstractEntityFileAction {

    @Autowired
    private EntityFileService service;

    public NewDirectoryAction() {
        setName("Nuevo Directorio");
        setImage("folder2");
        setGroup(ActionGroup.get("FILES"));
        setMenuSupported(true);
    }

    @Override
    public void actionPerformed(final EntityFileActionEvent evt) {
        InputPanel inputPanel = new InputPanel("Nombre de Directorio", "", String.class);
        inputPanel.showDialog();
        inputPanel.addEventListener(InputPanel.ON_INPUT, event -> {
            String dirName = (String) event.getData();
            if (dirName != null && !dirName.isEmpty()) {
                if (evt.getEntityFile() != null && evt.getEntityFile().getType() == EntityFileType.DIRECTORY) {
                    service.createDirectory(evt.getEntityFile(), dirName, "");
                } else {
                    service.createDirectory(evt.getTargetEntity(), dirName, "");
                }
                evt.getView().getController().doQuery();
            } else {
                UIMessages.showMessage("Ingrese nombre del nuevo directorio", MessageType.ERROR);
            }
        });

    }

}
