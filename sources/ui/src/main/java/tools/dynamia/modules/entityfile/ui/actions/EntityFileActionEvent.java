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

package tools.dynamia.modules.entityfile.ui.actions;

import java.util.Map;

import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudControllerAPI;
import tools.dynamia.crud.GenericCrudView;
import tools.dynamia.modules.entityfile.domain.EntityFile;

public class EntityFileActionEvent extends CrudActionEvent {

	/**
     *
     */
	private static final long serialVersionUID = 1L;

	private Object targetEntity;

	public EntityFileActionEvent(Object targetEntity, Object data, Object source, GenericCrudView view, CrudControllerAPI controller) {
		super(data, source, view, controller);
		this.targetEntity = targetEntity;
	}

	public EntityFileActionEvent(Object targetEntity, Object data, Object source, Map<String, Object> params, GenericCrudView view,
			CrudControllerAPI controller) {
		super(data, source, params, view, controller);
		this.targetEntity = targetEntity;
	}

	public EntityFile getEntityFile() {
		return (EntityFile) getData();
	}

	public Object getTargetEntity() {
		return targetEntity;
	}

}
