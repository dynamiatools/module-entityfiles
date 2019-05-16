package tools.dynamia.modules.entityfile;

/*-
 * #%L
 * Dynamia Modules - EntityFiles - Core
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

import tools.dynamia.commons.BeanUtils;
import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.commons.reflect.PropertyInfo;
import tools.dynamia.domain.DataTransferObjectPropertyProvider;
import tools.dynamia.integration.sterotypes.Provider;
import tools.dynamia.modules.entityfile.domain.EntityFile;

@Provider
public class DTOEntityFileToStringProperty implements DataTransferObjectPropertyProvider {

    private LoggingService logger = new SLF4JLoggingService(DTOEntityFileToStringProperty.class);

    @Override
    public boolean transferPropertyValue(Object dto, Object propertyValue, PropertyInfo propertyInfo) {
        if (propertyValue instanceof EntityFile) {
            EntityFile entityFile = (EntityFile) propertyValue;
            PropertyInfo dtoProperty = BeanUtils.getPropertyInfo(dto.getClass(), propertyInfo.getName());
            if (dtoProperty != null && dtoProperty.is(String.class)) {
                try {
                    String url = entityFile.getStoredEntityFile().getUrl();
                    BeanUtils.setFieldValue(dtoProperty, dto, url);
                    return true;
                } catch (Exception e) {
                    logger.error("Cannot transfer property value " + propertyValue + " to DTO  " + dto);
                }
            }
        }

        return false;
    }
}
