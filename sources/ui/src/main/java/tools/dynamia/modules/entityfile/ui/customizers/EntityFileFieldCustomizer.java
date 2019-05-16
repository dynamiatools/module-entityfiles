package tools.dynamia.modules.entityfile.ui.customizers;

/*-
 * #%L
 * Dynamia Modules - EntityFiles - UI
 * %%
 * Copyright (C) 2016 - 2019 Dynamia Soluciones IT SAS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

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
