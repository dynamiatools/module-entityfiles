/*
 * Copyright (C) 2023 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia / South America
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
