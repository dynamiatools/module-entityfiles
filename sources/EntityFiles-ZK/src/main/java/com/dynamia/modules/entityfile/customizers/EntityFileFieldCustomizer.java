package com.dynamia.modules.entityfile.customizers;

import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.modules.entityfile.ui.EntityFileDownloadlink;
import com.dynamia.modules.entityfile.ui.EntityFileImage;
import com.dynamia.modules.entityfile.ui.EntityFileUploadlink;
import com.dynamia.tools.integration.sterotypes.Component;
import com.dynamia.tools.viewers.Field;
import com.dynamia.tools.viewers.FieldCustomizer;

@Component
public class EntityFileFieldCustomizer implements FieldCustomizer {

	@Override
	public void customize(String viewTypeName, Field field) {

		if (field.getFieldClass() == EntityFile.class && field.getComponentClass() != EntityFileImage.class) {

			if (viewTypeName.equals("form")) {
				field.setComponentClass(EntityFileUploadlink.class);
			} else if (viewTypeName.equals("table")) {
				field.setComponentClass(EntityFileDownloadlink.class);
			}
		}
	}

}
