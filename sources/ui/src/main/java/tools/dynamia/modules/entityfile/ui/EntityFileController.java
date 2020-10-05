/*
 * Copyright (C)  2020. Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 *  All Rights Reserved.
 *
 * This file is free software: you can redistribute it and/or modify it  under the terms of the
 *  GNU Lesser General Public License (LGPL v3) as published by the Free Software Foundation,
 *   either version 3 of the License, or (at your option) any later version.
 *
 *  This file is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *   without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *   See the GNU Lesser General Public License for more details. You should have received a copy of the
 *   GNU Lesser General Public License along with this file.
 *   If not, see <https://www.gnu.org/licenses/>.
 *
 */

package tools.dynamia.modules.entityfile.ui;

import java.util.List;

import tools.dynamia.integration.Containers;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.enums.EntityFileType;
import tools.dynamia.modules.entityfile.service.EntityFileService;
import tools.dynamia.zk.crud.TreeCrudController;
import tools.dynamia.zk.crud.ui.EntityTreeNode;
import tools.dynamia.zk.crud.ui.LazyEntityTreeNode;

public class EntityFileController extends TreeCrudController<EntityFile> {

	private EntityFileService service;
	private Object targetEntity;

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
		case IMAGE:
			node = new EntityTreeNode(entity, entity.getName());
			node.setIcon(entity.getExtension());
			break;
		default:
			break;
		}

		return node;
	}

	public void setTargetEntity(Object targetEntity) {
		this.targetEntity = targetEntity;
	}

	public Object getTargetEntity() {
		return targetEntity;
	}

}
