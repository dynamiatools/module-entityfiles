package tools.dynamia.modules.entityfile.ui.customizers;

import tools.dynamia.integration.sterotypes.Component;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.ui.components.EntityFileDownloadlink;
import tools.dynamia.modules.entityfile.ui.components.EntityFileImage;
import tools.dynamia.modules.entityfile.ui.components.EntityFileUploadlink;
import tools.dynamia.viewers.Field;
import tools.dynamia.viewers.FieldCustomizer;

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
