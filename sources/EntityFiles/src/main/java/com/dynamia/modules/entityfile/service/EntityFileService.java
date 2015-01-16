package com.dynamia.modules.entityfile.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import com.dynamia.modules.entityfile.EntityFileAware;
import com.dynamia.modules.entityfile.EntityFileException;
import com.dynamia.modules.entityfile.FilesConfig;
import com.dynamia.modules.entityfile.UploadedFileInfo;
import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.tools.commons.BeanUtils;
import com.dynamia.tools.domain.AbstractEntity;
import com.dynamia.tools.domain.util.DomainUtils;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.transaction.annotation.Transactional;

// TODO: Auto-generated Javadoc
/**
 * The Interface EntityFileService.
 *
 * @author Mario Serrano
 */
public interface EntityFileService {

	/**
	 * Creates the directory.
	 *
	 * @param ownerEntity
	 *            the owner entity
	 * @param name
	 *            the name
	 * @param description
	 *            the description
	 * @return the entity file
	 */
	public EntityFile createDirectory(Object ownerEntity, String name, String description);

	/**
	 * Creates the directory.
	 *
	 * @param parent
	 *            the parent
	 * @param name
	 *            the name
	 * @param description
	 *            the description
	 * @return the entity file
	 */
	public EntityFile createDirectory(EntityFile parent, String name, String description);

	/**
	 * Creates the entity file.
	 *
	 * @param fileInfo
	 *            the file info
	 * @param target
	 *            the target
	 * @param desc
	 *            the desc
	 * @return the entity file
	 */
	public EntityFile createEntityFile(UploadedFileInfo fileInfo, Object target, String desc);

	/**
	 * Creates the entity file.
	 *
	 * @param fileInfo
	 *            the file info
	 * @param targetEntity
	 *            the target entity
	 * @return the entity file
	 */
	public EntityFile createEntityFile(UploadedFileInfo fileInfo, Object targetEntity);

	/**
	 * Gets the configuration.
	 *
	 * @return the configuration
	 */
	public abstract FilesConfig getConfiguration();

	/**
	 * Gets the entity files.
	 *
	 * @param clazz
	 *            the clazz
	 * @param id
	 *            the id
	 * @param parentDirectory
	 *            the parent directory
	 * @return the entity files
	 */
	public abstract List<EntityFile> getEntityFiles(Class clazz, Serializable id, EntityFile parentDirectory);

	/**
	 * Gets the entity files.
	 *
	 * @param entity
	 *            the entity
	 * @param parentDirectory
	 *            the parent directory
	 * @return the entity files
	 */
	public abstract List<EntityFile> getEntityFiles(Object entity, EntityFile parentDirectory);

	/**
	 * Gets the entity files.
	 *
	 * @param entity
	 *            the entity
	 * @return the entity files
	 */
	public abstract List<EntityFile> getEntityFiles(Object entity);

	/**
	 * Download.
	 *
	 * @param file
	 *            the file
	 * @return the input stream
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public InputStream download(EntityFile file) throws FileNotFoundException;

	/**
	 * Delete.
	 *
	 * @param entityFile
	 *            the entity file
	 */
	public abstract void delete(EntityFile entityFile);

	/**
	 * Sync entity file aware.
	 */
	public abstract void syncEntityFileAware();

	/**
	 * Gets the real file.
	 *
	 * @param file
	 *            the file
	 * @return the real file
	 */
	public abstract File getRealFile(EntityFile file);

	/**
	 * Creates the temporal entity file.
	 *
	 * @param fileInfo
	 *            the file info
	 * @return the entity file
	 */
	public abstract EntityFile createTemporalEntityFile(UploadedFileInfo fileInfo);

	/**
	 * Configure entity file. Setup targetEntity and targetEntityId for
	 * EntityFile
	 *
	 * @param target
	 *            the target
	 * @param entityFile
	 *            the entity file
	 */
	public abstract void configureEntityFile(Object target, EntityFile entityFile);

	public abstract void syncEntityFileAware(Object target);

}
