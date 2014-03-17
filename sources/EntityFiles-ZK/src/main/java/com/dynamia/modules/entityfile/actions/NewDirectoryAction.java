package com.dynamia.modules.entityfile.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.modules.entityfile.enums.EntityFileType;
import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.tools.web.actions.ActionGroup;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.ui.InputPanel;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

@InstallAction
public class NewDirectoryAction extends AbstractEntityFileAction {

    @Autowired
    private EntityFileService service;

    public NewDirectoryAction() {
        setName("Nuevo Directorio");
        setImage("icons:addfolder");
        setGroup(ActionGroup.get("FILES"));
        setMenuSupported(true);
    }

    @Override
    public void actionPerformed(final EntityFileActionEvent evt) {
        InputPanel inputPanel = new InputPanel("Nombre de Directorio", null, String.class);
        inputPanel.showDialog();
        inputPanel.addEventListener(InputPanel.ON_INPUT, new EventListener<Event>() {

            @Override
            public void onEvent(Event event) throws Exception {
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
            }
        });

    }

}
