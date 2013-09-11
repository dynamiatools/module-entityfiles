package com.dynamia.modules.entityfile.view;

import java.util.List;

import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.tools.domain.AbstractEntity;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.web.ui.AbstractEntityTreeController;
import com.dynamia.tools.web.ui.EntityTreeNode;
import com.dynamia.tools.web.ui.RootTreeNode;

public class FileExplorerController extends AbstractEntityTreeController<EntityFile> {

	private EntityFileService service;
	private AbstractEntity targetEntity;	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7926996145692421296L;

	public FileExplorerController(FileExplorerView view, RootTreeNode rootTreeNode) {
		super(view.getTree(), rootTreeNode);
		this.targetEntity = view.getValue();
		this.service = Containers.get().findObject(EntityFileService.class);
	}
	
	@Override
	public List<EntityFile> loadMainEntities() {
		System.out.println("LOading main entities");
		return service.getEntityFiles(targetEntity);
	}

	@Override
	public List<EntityFile> loadSubentities(EntityFile parentEntity) {
		System.out.println("Loading sub_files of "+parentEntity.getName());
		return service.getEntityFiles(targetEntity, parentEntity);
	}

	@Override
	public void onEntitySelected(EntityFile selectedEntity) {
		System.out.println("Archivo seleccionado " + selectedEntity);

	}

	@Override
	public EntityTreeNode createNode(EntityFile entity) {
		EntityTreeNode node = new EntityTreeNode(entity, entity.getName());
		
		switch (entity.getType()) {
		case DIRECTORY:			
			node.setIcon("folder");
			node.setLeaf(false);
			break;
		case FILE:			
			node.setIcon(entity.getExtension());
			node.setLeaf(true);
			break;
		default:
			break;
		}

		return node;
	}

	public void setTargetEntity(AbstractEntity targetEntity) {
		this.targetEntity = targetEntity;
	}

}
