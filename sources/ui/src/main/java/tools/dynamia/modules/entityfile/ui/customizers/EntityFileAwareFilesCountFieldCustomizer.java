package tools.dynamia.modules.entityfile.ui.customizers;

import tools.dynamia.integration.sterotypes.Component;
import tools.dynamia.modules.entityfile.ui.components.FilesCountImage;
import tools.dynamia.viewers.Field;
import tools.dynamia.viewers.FieldCustomizer;

@Component
public class EntityFileAwareFilesCountFieldCustomizer implements FieldCustomizer {

	@Override
	public void customize(String viewTypeName, Field field) {
		if (field.getName().equals("filesCount")) {
			field.setComponentClass(FilesCountImage.class);
		}
	}

}
