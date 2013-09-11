/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;

import com.dynamia.modules.entityfile.UploadedFileInfo;
import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.modules.entityfile.enums.EntityFileType;
import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.tools.domain.AbstractEntity;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.web.crud.CrudController;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.util.ZKUtil;

/**
 * se aplica a : bancos/listado.zul bancos/nuevo.zul
 *
 */
@Component("generic")
@Scope("prototype")
public class FilesController extends CrudController<EntityFile> {

    private Listbox list;
    private Object object;
    private Toolbar rutaPos;
    @Autowired
    private EntityFileService entityFileService;
    private NavegationButton principal = new NavegationButton();
    private NavegationButton ubicacionActual = new NavegationButton();
    private int indexActual = 0;
    private Textbox nombreCarpeta;
    private Textbox descripcionCarpeta;
    private AbstractEntity targetEntity;

    @Override
    protected void afterPageLoaded() {
        object = requestScope.get("data");
        if (object instanceof EntityFile) {
        } else if (object instanceof AbstractEntity) {
            targetEntity = (AbstractEntity) object;
            doQuery();
            principal.setIndex(0);
            principal.setEntityFile(null);
            principal.setLabel(" / ");
            principal.setStyle("font-weight:bold");
            principal.addEventListener("onClick", navListener);
            ubicacionActual = principal;
            if (rutaPos != null) {
                rutaPos.getChildren().add(principal);
            }
        }
    }
    private EventListener navListener = new EventListener() {

        @Override
        public void onEvent(Event event) throws Exception {
            ubicacionActual = (NavegationButton) event.getTarget();
            doQuery();
            list.setModel(new ListModelList(getQueryResult()));
        }
    };

    public void nuevaCarpeta() {
        NavegationButton nav = ubicacionActual;
        Object envio = null;
        if (nav.getEntityFile() == null) {
            envio = object;
        } else {
            envio = nav.getEntityFile();
        }
        ZKUtil.showDialog("/zkau/web/entityfiles/agregarCarpeta.zul", "Agregar Carpeta", envio);
        doQuery();
    }

    public void comentar(EntityFile entityFile) {
    }

    public void crearCarpeta() {
        if (object != null) {
            if (object instanceof EntityFile) {
                EntityFile parentFolder = (EntityFile) object;
                if (parentFolder.getType() == EntityFileType.DIRECTORY) {
                    entityFileService.createSubdirectory(parentFolder, nombreCarpeta.getValue(), descripcionCarpeta.getValue());
                } else {
                    ZKUtil.showMessage("Solo se pueden crear subcarpetas a carpetas");
                }
            } else if (object instanceof AbstractEntity) {
                AbstractEntity parentEntity = (AbstractEntity) object;
                entityFileService.createDirectory(parentEntity, nombreCarpeta.getValue(), descripcionCarpeta.getValue());
            }
            closeCurrentDialog();
        }
    }

    @Override
    public void query() {
        reposicionar(ubicacionActual);
        List result = new LinkedList();
        if (object instanceof AbstractEntity) {
            AbstractEntity targetEntity = (AbstractEntity) object;
            if (ubicacionActual.getEntityFile() == null) {
                result = entityFileService.getEntityFiles(targetEntity);
            } else {
                result = entityFileService.getEntityFiles(targetEntity, ubicacionActual.getEntityFile());
            }
        }
        setQueryResult(result);
    }

    public void eliminar() {
        if (list.getSelectedItem() != null) {
            EntityFile entityFile = (EntityFile) list.getSelectedItem().getValue();
            crudService.delete(EntityFile.class, entityFile.getId());
            ZKUtil.showMessage("Archivo Eliminado!");
            onClick$query();
        } else {
            ZKUtil.showMessage("Seleccione el archivo que desea eliminar", MessageType.INFO);
        }
    }

    public static EntityFile adjuntar(final AbstractEntity entity) {
        EntityFileService efs = Containers.get().findObject(EntityFileService.class);

        Media[] uploadFiles = Fileupload.get(-1);
        System.out.println("UPLOAD FILES");
        for (Media media : uploadFiles) {
            System.out.println(media);
            /*UploadedFileInfo fileInfo = new UploadedFileInfo();
            fileInfo.setFullName(media.getName());
            fileInfo.setContentType(media.getContentType());
            fileInfo.setInputStream(media.getStreamData());
            EntityFile entityFile = efs.createEntityFile(fileInfo, entity);*/
            EntityFile entityFile = adjuntar(entity, media);
                    
            return entityFile;
        }
        return null;
    }
    
    public static EntityFile adjuntar(final AbstractEntity entity, Media media) {
        EntityFileService efs = Containers.get().findObject(EntityFileService.class);
        UploadedFileInfo fileInfo = new UploadedFileInfo();
            fileInfo.setFullName(media.getName());
            fileInfo.setContentType(media.getContentType());
            fileInfo.setInputStream(media.getStreamData());
            return efs.createEntityFile(fileInfo, entity);
    }
    public void agregarArchivo() {
        try {
            adjuntar(targetEntity);
        } catch (Exception ex) {
            ZKUtil.showMessage("Error subiendo archivos", MessageType.ERROR);
            ex.printStackTrace();
        }
        doQuery();
    }

    public void goTo(EntityFile entityFile) {
        if (entityFile.getType() == EntityFileType.DIRECTORY) {
            NavegationButton nav = new NavegationButton();
            nav.setEntityFile(entityFile);
            addNavegationLevel(nav);
        } else {
            download(entityFile);
        }
    }

    private void download(EntityFile entityFile) {
        // TODO Auto-generated method stub
    }

    public void reposicionar(NavegationButton navegacion) {
        indexActual = 0;
        if (navegacion != null) {
            indexActual = navegacion.getIndex();
        }
        List lista = new LinkedList();
        for (Object obj : rutaPos.getChildren()) {
            if (obj instanceof NavegationButton) {
                NavegationButton nav = (NavegationButton) obj;
                if (nav.getIndex() <= indexActual) {
                    lista.add(obj);
                }
            }
        }
        rutaPos.getChildren().clear();
        rutaPos.getChildren().addAll(lista);

    }

    private void addNavegationLevel(NavegationButton nav) {
        indexActual = indexActual + 1;
        nav.setIndex(indexActual);
        nav.setLabel(" > " + nav.getLabel());
        nav.addEventListener("onClick", navListener);
        nav.setStyle("font-weight:bold");
        ubicacionActual = nav;
        query();
        list.setModel(new ListModelList(getQueryResult()));
        rutaPos.getChildren().add(nav);
    }

    class NavegationButton extends Toolbarbutton {

        private EntityFile entityFile;
        private int index = 0;

        public EntityFile getEntityFile() {
            return entityFile;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void setEntityFile(EntityFile entityFile) {
            this.entityFile = entityFile;
            if (entityFile != null) {
                setLabel(entityFile.getName());
            }
        }
    }
}
