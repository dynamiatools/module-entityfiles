/*
 * Copyright (C)  2020. Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 *  All Rights Reserved.
 *
 * This file is free software: you can redistribute it and/or modify it  under the terms of the
 *  GNU Lesser General Public License (LGPL v3) as published by the Free Software Foundation,
 *   either version 3 of the License, or (at your option) any later version.
 *
 *  This file is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *   without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *   See the GNU Lesser General Public License for more details. You should have received a copy of the
 *   GNU Lesser General Public License along with this file.
 *   If not, see <https://www.gnu.org/licenses/>.
 *
 */

package tools.dynamia.modules.entityfile.listeners;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.commons.BeanUtils;
import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.commons.reflect.PropertyInfo;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.domain.util.CrudServiceListenerAdapter;
import tools.dynamia.domain.util.DomainUtils;
import tools.dynamia.integration.sterotypes.Component;
import tools.dynamia.modules.entityfile.EntityFileAware;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.service.EntityFileService;

@Component
public class TemporalEntityFileCrudListener extends CrudServiceListenerAdapter<Object> {

	@Autowired
	private EntityFileService service;

	@Autowired
	private CrudService crudService;

	private LoggingService logger = new SLF4JLoggingService(TemporalEntityFileCrudListener.class);

	public TemporalEntityFileCrudListener() {
		System.out.println("Init " + getClass());
	}

	@Override
	public void afterCreate(Object entity) {

		checkEntityFiles(entity);
	}

	@Override
	public void afterUpdate(Object entity) {
		checkEntityFiles(entity);
	}

	private void checkEntityFiles(Object entity) {
		if (DomainUtils.isEntity(entity) && !(entity instanceof EntityFile)) {
			List<PropertyInfo> properties = BeanUtils.getPropertiesInfo(entity.getClass());
			for (PropertyInfo propertyInfo : properties) {
				if (propertyInfo.is(EntityFile.class)) {
					updateEntityFile(entity, propertyInfo);
				}
			}
		}
	}

	private void updateEntityFile(Object entity, PropertyInfo propertyInfo) {
		try {

			EntityFile entityFile = (EntityFile) BeanUtils.invokeGetMethod(entity, propertyInfo);
			if (entityFile != null && entityFile.getTargetEntity() != null && entityFile.getTargetEntity().equals("temporal")) {
				service.configureEntityFile(entity, entityFile);
				crudService.update(entityFile);
				if (entity instanceof EntityFileAware) {
					EntityFileAware entityFileAware = (EntityFileAware) entity;
					if (entityFileAware.getFilesCount() == null) {
						entityFileAware.setFilesCount(1L);
					} else {
						entityFileAware.setFilesCount(entityFileAware.getFilesCount() + 1);
					}
					crudService.update(entity);
				}
			}
		} catch (Exception e) {
			logger.error("Error updating entity file for entity " + entity + ". Property: " + propertyInfo.getName(), e);
		}
	}

}
