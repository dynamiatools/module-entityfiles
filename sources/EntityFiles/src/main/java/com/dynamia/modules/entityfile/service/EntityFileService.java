/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile.service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import com.dynamia.modules.entityfile.FilesConfig;
import com.dynamia.modules.entityfile.UploadedFileInfo;
import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.tools.domain.AbstractEntity;

/**
 *
 * @author djinn
 */
public interface EntityFileService {

    public EntityFile createDirectory(AbstractEntity ownerEntity, String name, String description);

    public EntityFile createDirectory(EntityFile parent, String name, String description);

    public EntityFile createEntityFile(UploadedFileInfo fileInfo, String className, Long classId);

    public EntityFile createEntityFile(UploadedFileInfo fileInfo, String className, Long classId, String desc);

    public EntityFile createEntityFile(UploadedFileInfo fileInfo, AbstractEntity targetEntity);

    public abstract FilesConfig getConfiguration();

    public abstract List<EntityFile> getEntityFiles(Class clazz, Serializable id, EntityFile parentDirectory);

    public abstract List<EntityFile> getEntityFiles(AbstractEntity entity, EntityFile parentDirectory);

    public abstract List<EntityFile> getEntityFiles(AbstractEntity entity);

    public InputStream download(EntityFile file) throws FileNotFoundException;

    public abstract void delete(EntityFile entityFile);

}
