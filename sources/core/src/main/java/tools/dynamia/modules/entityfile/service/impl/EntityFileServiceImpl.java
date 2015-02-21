/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.entityfile.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tools.dynamia.commons.BeanUtils;
import tools.dynamia.commons.StringUtils;
import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.domain.query.Parameters;
import tools.dynamia.domain.query.QueryConditions;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.domain.util.DomainUtils;
import tools.dynamia.integration.Containers;
import tools.dynamia.integration.scheduling.Task;
import tools.dynamia.io.IOUtils;
import tools.dynamia.modules.entityfile.EntityFileAware;
import tools.dynamia.modules.entityfile.EntityFileException;
import tools.dynamia.modules.entityfile.FilesConfig;
import tools.dynamia.modules.entityfile.UploadedFileInfo;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.domain.enums.EntityFileState;
import tools.dynamia.modules.entityfile.enums.EntityFileType;
import tools.dynamia.modules.entityfile.service.EntityFileService;
import tools.dynamia.modules.saas.AccountContext;

/**
 *
 * @author djinn
 */
@Service
public class EntityFileServiceImpl implements EntityFileService {

	private LoggingService logger = new SLF4JLoggingService(EntityFileService.class);

	@Autowired
	private CrudService crudService;

	@Autowired
	private Parameters appParams;

	@Override
	public EntityFile createDirectory(EntityFile parent, String name, String description) {
		return createDir(parent, null, name, description);
	}

	@Override
	public EntityFile createDirectory(Object entity, String name, String description) {
		return createDir(null, entity, name, description);
	}

	private EntityFile createDir(EntityFile parent, Object targetEntity, String name, String descripcion) {
		EntityFile entityFile = new EntityFile();
		entityFile.setAccount(AccountContext.getCurrent().getAccount());
		entityFile.setParent(parent);
		entityFile.setName(name);
		entityFile.setState(EntityFileState.VALID);
		entityFile.setDescription(descripcion);
		if (targetEntity != null) {
			configureEntityFile(targetEntity, entityFile);
		} else if (parent != null) {
			entityFile.setTargetEntity(parent.getTargetEntity());
			entityFile.setTargetEntityId(parent.getTargetEntityId());
			entityFile.setTargetEntitySId(parent.getTargetEntitySId());
		}
		entityFile.setShared(true);
		entityFile.setType(EntityFileType.DIRECTORY);
		entityFile = crudService.save(entityFile);
		return entityFile;
	}

	@Override
	@Transactional
	public EntityFile createEntityFile(UploadedFileInfo fileInfo, Object target, String description) {
		target = crudService.reload(target);
		logger.info("Creating new entity file for " + target + ", file: " + fileInfo.getFullName());
		EntityFile entityFile = new EntityFile();
		entityFile.setAccount(AccountContext.getCurrent().getAccount());
		entityFile.setDescription(description);
		entityFile.setContentType(fileInfo.getContentType());
		entityFile.setName(fileInfo.getFullName());
		entityFile.setExtension(StringUtils.getFilenameExtension(fileInfo.getFullName()));

		configureEntityFile(target, entityFile);

		entityFile.setType(EntityFileType.getFileType(entityFile.getExtension()));
		entityFile.setParent(fileInfo.getParent());
		entityFile.setState(EntityFileState.VALID);
		crudService.create(entityFile);
		syncEntityFileAware(target);

		File realFile = getRealFile(entityFile);
		try {
			realFile.getParentFile().mkdirs();
			realFile.createNewFile();
			realFile.setExecutable(false);

			IOUtils.copy(fileInfo.getInputStream(), realFile);
			System.out.println("File Saved: " + realFile.getName() + "  length: " + realFile.length());
			crudService.updateField(entityFile, "size", realFile.length());

		} catch (IOException e) {
			throw new EntityFileException(
					"Error writing file " + fileInfo.getFullName() + " on new location " + realFile.getAbsolutePath(), e);
		}

		return entityFile;
	}

	@Override
	public void configureEntityFile(Object target, EntityFile entityFile) {
		if (target != null) {
			if (DomainUtils.isJPAEntity(target)) {
				entityFile.setTargetEntity(target.getClass().getName());
				Serializable id = DomainUtils.getJPAIdValue(target);

				if (id == null) {
					throw new EntityFileException("Null id for entity " + target.getClass() + " -> " + target);
				}

				if (id instanceof Long) {
					entityFile.setTargetEntityId((Long) id);
				} else {
					entityFile.setTargetEntitySId(id.toString());
				}
			} else {
				throw new EntityFileException("Target entity " + target.getClass() + " -> " + target + " is not a JPA Entity");
			}
		} else {
			entityFile.setTargetEntity("temporal");
		}
	}

	@Override
	@Transactional
	public EntityFile createEntityFile(UploadedFileInfo fileInfo, Object target) {
		return createEntityFile(fileInfo, target, null);
	}

	@Override
	@Transactional
	public EntityFile createTemporalEntityFile(UploadedFileInfo fileInfo) {
		EntityFile temp = createEntityFile(fileInfo, null);
		temp.setTargetEntity("temporal");
		temp.setTargetEntityId(System.currentTimeMillis());

		return temp;
	}

	@Override
	@Transactional
	public void delete(EntityFile entityFile) {
		crudService.updateField(entityFile, "state", EntityFileState.DELETED);
	}

	@Override
	public List<EntityFile> getEntityFiles(Object entity) {

		return getEntityFiles(entity.getClass(), DomainUtils.getJPAIdValue(entity), null);
	}

	@Override
	public List<EntityFile> getEntityFiles(Object entity, EntityFile parentDirectory) {
		return getEntityFiles(entity.getClass(), DomainUtils.getJPAIdValue(entity), parentDirectory);
	}

	@Override
	public List<EntityFile> getEntityFiles(Class clazz, Serializable id, EntityFile parentDirectory) {
		QueryParameters params = new QueryParameters();
		params.setAutocreateSearcheableStrings(false);
		params.add("targetEntity", QueryConditions.eq(clazz.getName()));

		if (id instanceof Long) {
			params.add("targetEntityId", QueryConditions.eq(id));
		} else {
			params.add("targetEntitySId", QueryConditions.eq(id.toString()));
		}
		params.add("state", QueryConditions.notEq(EntityFileState.DELETED));
		if (parentDirectory == null) {
			params.add("parent", QueryConditions.isNull());
		} else {
			params.add("parent", parentDirectory);
		}
		params.orderBy("type", true);
		return crudService.find(EntityFile.class, params);
	}

	private long counttEntityFiles(Class clazz, Serializable id) {
		QueryParameters params = new QueryParameters();
		params.setAutocreateSearcheableStrings(false);
		params.add("targetEntity", QueryConditions.eq(clazz.getName()));
		if (id instanceof Long) {
			params.add("targetEntityId", QueryConditions.eq(id));
		} else {
			params.add("targetEntitySId", QueryConditions.eq(id.toString()));
		}
		params.add("state", EntityFileState.VALID);
		params.add("type", QueryConditions.in(EntityFileType.FILE, EntityFileType.IMAGE));

		return crudService.count(EntityFile.class, params);
	}

	@Override
	public FilesConfig getConfiguration() {
		FilesConfig fileConfig = Containers.get().findObject(FilesConfig.class);
		if (fileConfig == null) {
			String loc = appParams.getValue(FilesConfig.FILES_LOCATION);
			if (loc != null && !loc.isEmpty()) {
				fileConfig = new FilesConfig(loc);
			}
		}

		if (fileConfig == null) {
			fileConfig = new FilesConfig();
		}
		return fileConfig;

	}

	@Override
	public InputStream download(EntityFile file) throws FileNotFoundException {
		if (file == null || file.getId() == null) {
			throw new FileNotFoundException("EntityFile is null");
		}

		File realFile = getRealFile(file);
		return new FileInputStream(realFile);
	}

	@Override
	@PostConstruct
	public void syncEntityFileAware() {
		List<String> targetEntities = crudService.getPropertyValues(EntityFile.class, "targetEntity");
		if (targetEntities != null) {
			logger.info("Syncing EntityFileAware entities");
			for (final String entityClassName : targetEntities) {
				if (!entityClassName.equals("temporal")) {
					try {
						Object object = BeanUtils.newInstance(entityClassName);
						if (object instanceof EntityFileAware) {
							crudService.executeWithinTransaction(new Task() {
								@Override
								public void doWork() {
									logger.info("Processing batch EntityFileAware for " + entityClassName);
									String updateQuery = "update "
											+ entityClassName
											+ " e set e.filesCount = (select count(ef.id) from EntityFile ef where ef.targetEntityId = e.id and ef.state = :state and ef.type in (:types) and ef.targetEntity='"
											+ entityClassName + "')";
									QueryParameters parameters = QueryParameters.with("state", EntityFileState.VALID)
											.add("types", Arrays.asList(EntityFileType.FILE, EntityFileType.IMAGE));
									crudService.execute(updateQuery, parameters);
								}
							});
						}
					} catch (Exception e) {
						logger.warn("Cannot sync EntityFile " + entityClassName + ". Error: " + e.getMessage());
					}
				}
			}
		}
	}

	@Override
	public File getRealFile(EntityFile file) {
		String filePath = getConfiguration().getRepository() + "/Account" + file.getAccount().getId() + "/" + file.getId();
		return new File(filePath);
	}

	@Override
	@Transactional
	public void syncEntityFileAware(Object target) {
		if (target != null && target instanceof EntityFileAware) {
			logger.info("Processing EntityFileAware for " + target.getClass() + " - " + target);
			EntityFileAware efa = (EntityFileAware) target;
			efa.setFilesCount(counttEntityFiles(target.getClass(), DomainUtils.getJPAIdValue(target)));
			crudService.update(target);
		}
	}

}
