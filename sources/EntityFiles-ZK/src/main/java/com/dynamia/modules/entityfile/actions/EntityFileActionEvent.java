package com.dynamia.modules.entityfile.actions;

import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.modules.entityfile.view.FileExplorerView;
import com.dynamia.tools.domain.AbstractEntity;
import com.dynamia.tools.web.actions.ActionEvent;

public class EntityFileActionEvent extends ActionEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AbstractEntity targetEntity;
	private FileExplorerView view;

	public EntityFileActionEvent(EntityFile data, AbstractEntity targetEntity, FileExplorerView view, Object source) {
		super(data, source);
		this.targetEntity = targetEntity;
		this.view = view;
	}

	public EntityFile getEntityFile() {
		return (EntityFile) getData();
	}

	public AbstractEntity getTargetEntity() {
		return targetEntity;
	}

	public FileExplorerView getView() {
		return view;
	}

}
