package com.dynamia.modules.entityfile.actions;

import com.dynamia.modules.entityfile.enums.EntityFileType;
import com.dynamia.tools.web.actions.Action;

public interface EntityFileAction extends Action {
	

	public EntityFileType getApplicableType();
	
}
