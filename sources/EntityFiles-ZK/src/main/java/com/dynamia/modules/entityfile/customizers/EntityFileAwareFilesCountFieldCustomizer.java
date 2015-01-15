package com.dynamia.modules.entityfile.customizers;

import com.dynamia.modules.entityfile.ui.FilesCountImage;
import com.dynamia.tools.integration.sterotypes.Component;
import com.dynamia.tools.viewers.Field;
import com.dynamia.tools.viewers.FieldCustomizer;

@Component
public class EntityFileAwareFilesCountFieldCustomizer implements FieldCustomizer {

	@Override
	public void customize(String viewTypeName, Field field) {
		if (field.getName().equals("filesCount")) {
			field.setComponentClass(FilesCountImage.class);
		}
	}

}
