package com.dynamia.modules.entityfile.listeners;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.tools.commons.BeanUtils;
import com.dynamia.tools.commons.logger.LoggingService;
import com.dynamia.tools.commons.logger.SLF4JLoggingService;
import com.dynamia.tools.commons.reflect.PropertyInfo;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.domain.util.CrudServiceListenerAdapter;
import com.dynamia.tools.domain.util.DomainUtils;
import com.dynamia.tools.integration.sterotypes.Component;

@Component
public class TemporalEntityFileCrudListener extends CrudServiceListenerAdapter<Object> {

	@Autowired
	private EntityFileService service;

	@Autowired
	private CrudService crudService;

	private LoggingService logger = new SLF4JLoggingService(TemporalEntityFileCrudListener.class);

	@Override
	public void afterCreate(Object entity) {
		checkEntityFiles(entity);
	}

	@Override
	public void afterUpdate(Object entity) {
		checkEntityFiles(entity);
	}

	private void checkEntityFiles(Object entity) {
		if (DomainUtils.isJPAEntity(entity)) {
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
			if (entityFile!=null && entityFile.getTargetEntity() != null && entityFile.getTargetEntity().equals("temporal")) {
				service.configureEntityFile(entity, entityFile);
				crudService.update(entityFile);
			}
		} catch (Exception e) {
			logger.error("Error updating entity file for entity " + entity + ". Property: " + propertyInfo.getName(), e);
		}
	}

}
