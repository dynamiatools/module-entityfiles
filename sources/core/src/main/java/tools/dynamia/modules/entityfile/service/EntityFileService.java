package tools.dynamia.modules.entityfile.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import tools.dynamia.modules.entityfile.StoredEntityFile;
import tools.dynamia.modules.entityfile.UploadedFileInfo;
import tools.dynamia.modules.entityfile.domain.EntityFile;

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
	 * Gets the StoreEntityFile from current EntityFileStorage file.
	 *
	 * @param file
	 *            the file
	 * @return the real file
	 */
	public abstract StoredEntityFile download(EntityFile entityFile);

	/**
	 * Download the EntityFile internal file to a local output file, this is usefull when entityfiles 
	 * are stored in difernte localtion
	 * @param entityFile
	 * @param outputFile
	 */
	public abstract void download(EntityFile entityFile, File outputFile);

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

	public EntityFile getEntityFile(String uuid);

}
