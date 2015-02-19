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
