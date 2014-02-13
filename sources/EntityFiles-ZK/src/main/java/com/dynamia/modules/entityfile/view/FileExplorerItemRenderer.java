package com.dynamia.modules.entityfile.view;

import com.dynamia.modules.entityfile.ImageViewer;
import java.text.DateFormat;
import org.zkoss.zk.ui.event.Event;

import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.modules.entityfile.enums.EntityFileType;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.web.ui.EntityTreeItemRenderer;
import com.dynamia.tools.web.ui.EntityTreeNode;
import org.zkoss.zk.ui.event.EventListener;

public class FileExplorerItemRenderer extends EntityTreeItemRenderer {

    private Menupopup filesMenupopup;
    private Menupopup directoriesMenupopup;
    private DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM);

    @Override
    protected void addColumns(Treeitem item, Treerow row, EntityTreeNode node) {
        System.out.println("Agregando mas columnas");
        final EntityFile entityFile = (EntityFile) node.getEntity();
        if (entityFile != null) {

            //new Treecell(entityFile.getDescription()).setParent(row);
            new Treecell(entityFile.getCreator()).setParent(row);
            new Treecell(formatter.format(entityFile.getCreationDate())).setParent(row);
            if (entityFile.getType() == EntityFileType.DIRECTORY) {
                new Treecell("--").setParent(row);
            } else {
                new Treecell(entityFile.getSizeAsString()).setParent(row);
            }

            switch (entityFile.getType()) {
                case DIRECTORY:
                    item.setContext(directoriesMenupopup.getId());
                    break;
                case FILE:
                    item.setContext(filesMenupopup.getId());
                    break;
                default:
                    break;
            }
        } else {// null is the root node
            item.setContext(directoriesMenupopup.getId());
        }
        row.addEventListener("onDoubleClick", new EventListener() {

            @Override
            public void onEvent(Event event) throws Exception {
                view(entityFile);
            }
        });
    }

    public void view(EntityFile file) {
        try {
            //EntityFileService entityFileService = Containers.get().findObject(EntityFileService.class);
            //String newFileName = entityFileService.getConfiguration().getRepository() + "/" + file.getId();
            //System.out.println("X: " + newFileName);
            ImageViewer iv = Containers.get().findObject(ImageViewer.class);
            iv.view(file);
        } catch (Exception e) {
        }

    }

    public void setFilesMenupopup(Menupopup menupopup) {
        this.filesMenupopup = menupopup;
    }

    public void setDirectoriesMenupopup(Menupopup menupopup) {
        this.directoriesMenupopup = menupopup;

    }
}
