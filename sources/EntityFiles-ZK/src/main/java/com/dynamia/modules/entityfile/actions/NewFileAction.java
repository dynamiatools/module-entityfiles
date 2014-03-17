package com.dynamia.modules.entityfile.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.util.media.Media;
import org.zkoss.zul.Fileupload;

import com.dynamia.modules.entityfile.UploadedFileInfo;
import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.modules.entityfile.util.EntityFileUtils;
import com.dynamia.tools.web.actions.ActionGroup;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;

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

        Fileupload.get(-1,new EventListener<UploadEvent>() {

            @Override
            public void onEvent(UploadEvent event) throws Exception {
                
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
            }
        });

    }
}
