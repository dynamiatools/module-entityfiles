package com.dynamia.modules.entityfile.actions;

import java.util.Map;

import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudControllerAPI;
import com.dynamia.tools.web.crud.GenericCrudView;

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
