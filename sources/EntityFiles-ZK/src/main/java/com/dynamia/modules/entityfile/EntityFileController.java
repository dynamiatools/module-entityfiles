package com.dynamia.modules.entityfile;

import java.util.List;

import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.modules.entityfile.enums.EntityFileType;
import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.tools.domain.AbstractEntity;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.web.crud.TreeCrudController;
import com.dynamia.tools.web.ui.EntityTreeNode;
import com.dynamia.tools.web.ui.LazyEntityTreeNode;

public class EntityFileController extends TreeCrudController<EntityFile> {

    private EntityFileService service;
    private AbstractEntity targetEntity;

    /**
     *
     */
    private static final long serialVersionUID = 7926996145692421296L;

    public EntityFileController() {
        this.service = Containers.get().findObject(EntityFileService.class);
        setParentName("parent");
    }

    @Override
    protected List<EntityFile> loadRoots() {
        System.out.println("LOading main entities");
        return service.getEntityFiles(targetEntity);
    }

    @Override
    protected List<EntityFile> loadChildren(EntityFile parentEntity) {
        System.out.println("Loading sub_files of " + parentEntity.getName());
        return service.getEntityFiles(targetEntity, parentEntity);
    }

    @Override
    protected void afterCreate() {
        if (getSelected() != null && getSelected().getType() == EntityFileType.DIRECTORY) {
            getEntity().setParent(getSelected());
        }
    }

    @Override
    protected EntityTreeNode<EntityFile> newNode(EntityFile entity) {
        EntityTreeNode node = null;

        switch (entity.getType()) {
            case DIRECTORY:
                node = new LazyEntityTreeNode(entity, entity.getName(), this);
                node.setIcon("folder");
                node.setOnOpenListener(this);
                break;
            case FILE:
                node = new EntityTreeNode(entity, entity.getName());
                node.setIcon(entity.getExtension());
                break;
            default:
                break;
        }

        return node;
    }

    public void setTargetEntity(AbstractEntity targetEntity) {
        this.targetEntity = targetEntity;
    }

    public AbstractEntity getTargetEntity() {
        return targetEntity;
    }

}
