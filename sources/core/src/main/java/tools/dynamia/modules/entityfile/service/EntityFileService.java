package tools.dynamia.modules.entityfile.service;

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
    EntityFile createDirectory(Object ownerEntity, String name, String description);

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
    EntityFile createDirectory(EntityFile parent, String name, String description);

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
    EntityFile createEntityFile(UploadedFileInfo fileInfo, Object target, String desc);

	/**
	 * Creates the entity file.
	 *
	 * @param fileInfo
	 *            the file info
	 * @param targetEntity
	 *            the target entity
	 * @return the entity file
	 */
    EntityFile createEntityFile(UploadedFileInfo fileInfo, Object targetEntity);

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
    List<EntityFile> getEntityFiles(Class clazz, Serializable id, EntityFile parentDirectory);

	/**
	 * Gets the entity files.
	 *
	 * @param entity
	 *            the entity
	 * @param parentDirectory
	 *            the parent directory
	 * @return the entity files
	 */
    List<EntityFile> getEntityFiles(Object entity, EntityFile parentDirectory);

	/**
	 * Gets the entity files.
	 *
	 * @param entity
	 *            the entity
	 * @return the entity files
	 */
    List<EntityFile> getEntityFiles(Object entity);

	/**
	 * Delete.
	 *
	 * @param entityFile
	 *            the entity file
	 */
    void delete(EntityFile entityFile);

	/**
	 * Sync entity file aware.
	 */
    void syncEntityFileAware();

	/**
	 * Get an stored entity file instance for download
	 * @param entityFile
	 * @return
	 */
    StoredEntityFile download(EntityFile entityFile);

	/**
	 * Download the EntityFile internal file to a local output file, this is
	 * usefull when entityfiles are stored in difernte localtion
	 * 
	 * @param entityFile
	 * @param outputFile
	 */
    void download(EntityFile entityFile, File outputFile);

	/**
	 * Creates the temporal entity file.
	 *
	 * @param fileInfo
	 *            the file info
	 * @return the entity file
	 */
    EntityFile createTemporalEntityFile(UploadedFileInfo fileInfo);

	/**
	 * Configure entity file. Setup targetEntity and targetEntityId for
	 * EntityFile
	 *
	 * @param target
	 *            the target
	 * @param entityFile
	 *            the entity file
	 */
    void configureEntityFile(Object target, EntityFile entityFile);

	void syncEntityFileAware(Object target);

	EntityFile getEntityFile(String uuid);

}
