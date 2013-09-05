/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile.service.impl;

import com.dynamia.modules.entityfile.domain.EntityPhoto;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dynamia.modules.entityfile.EntityFileException;
import com.dynamia.modules.entityfile.FilesConfig;
import com.dynamia.modules.entityfile.UploadedFileInfo;
import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.modules.entityfile.domain.EntityImage;
import com.dynamia.modules.entityfile.domain.EntityNoPhoto;
import com.dynamia.modules.entityfile.domain.enums.EntityFileState;
import com.dynamia.modules.entityfile.enums.EntityFileType;
import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.modules.entityfile.utils.ImageUtil;
import com.dynamia.tools.commons.StringUtils;
import com.dynamia.tools.domain.AbstractEntity;
import com.dynamia.tools.domain.query.QueryConditions;
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.io.IOUtils;
import org.springframework.transaction.annotation.Propagation;

/**
 *
 * @author djinn
 */
@Service
public class EntityFileServiceImpl implements EntityFileService {

    @Autowired
    private CrudService crudService;

    @Override
    public EntityFile createSubdirectory(EntityFile parent, String name, String description) {
        return createDir(parent, null, name, description);
    }

    @Override
    public EntityFile createDirectory(AbstractEntity entity, String name, String description) {
        return createDir(null, entity, name, description);
    }

    private EntityFile createDir(EntityFile parent, AbstractEntity targetEntity, String name, String descripcion) {
        EntityFile entityFile = new EntityFile();
        entityFile.setParentDirectory(parent);
        entityFile.setName(name);
        entityFile.setDescription(descripcion);
        if (targetEntity != null) {
            entityFile.setTargetEntity(targetEntity.getClass().getName());
            entityFile.setTargetEntityId((Long) targetEntity.getId());
        }
        entityFile.setShared(true);
        entityFile.setType(EntityFileType.DIRECTORY);
        entityFile = crudService.save(entityFile);
        return entityFile;
    }

    @Override
    @Transactional
    public EntityFile createEntityFile(UploadedFileInfo fileInfo, String className, Long classId) {
        return createEntityFile(fileInfo, className, classId, null);
    }

    @Override
    @Transactional
    public EntityFile createEntityFile(UploadedFileInfo fileInfo, String className, Long classId, String deString) {
        EntityFile entityFile = new EntityFile();
        entityFile.setDescription(deString);
        entityFile.setContentType(fileInfo.getContentType());
        entityFile.setName(fileInfo.getFullName());
        entityFile.setExtension(StringUtils.getFilenameExtension(fileInfo.getFullName()));
        entityFile.setTargetEntity(className);
        entityFile.setTargetEntityId(classId);
        entityFile.setType(EntityFileType.FILE);
        entityFile.setParentDirectory(fileInfo.getParent());
        entityFile.setState(EntityFileState.VALID);
        crudService.create(entityFile);

        String newFileName = getConfiguration().getRepository() + "/" + entityFile.getId();
        try {
            File realFile = new File(newFileName);
            realFile.createNewFile();
            IOUtils.copy(fileInfo.getInputStream(), realFile);
            System.out.println("File Saved: " + realFile.getName() + "  length: " + realFile.length());
            crudService.updateField(entityFile, "size", realFile.length());

        } catch (IOException e) {
            throw new EntityFileException("Error writing file " + fileInfo.getFullName() + " no new location " + newFileName, e);
        }

        return entityFile;
    }

    @Override
    @Transactional
    public EntityFile createEntityFile(UploadedFileInfo fileInfo, AbstractEntity targetEntity) {
        return createEntityFile(fileInfo, targetEntity.getClass().getName(), ((Long) targetEntity.getId()));
    }

    @Override
    @Transactional
    public void delete(EntityFile entityFile) {
        crudService.updateField(entityFile, "state", EntityFileState.DELETED);
    }

    @Override
    public List<EntityFile> getEntityFiles(AbstractEntity entity) {
        return getEntityFiles(entity.getClass(), entity.getId(), null);
    }

    @Override
    public List<EntityFile> getEntityFiles(AbstractEntity entity, EntityFile parentDirectory) {
        return getEntityFiles(entity.getClass(), entity.getId(), parentDirectory);
    }

    @Override
    public List<EntityFile> getEntityFiles(Class clazz, Serializable id, EntityFile parentDirectory) {
        QueryParameters params = new QueryParameters();
        params.setAutocreateSearcheableStrings(false);
        params.add("targetEntity", QueryConditions.eq(clazz.getName()));
        params.add("targetEntityId", QueryConditions.eq(id));
        params.add("state", QueryConditions.notEq(EntityFileState.DELETED));
        if (parentDirectory == null) {
            params.add("parentDirectory", QueryConditions.isNull());
        } else {
            params.add("parentDirectory", parentDirectory);
        }
        params.orderBy("type", true);
        return crudService.find(EntityFile.class, params);
    }

    @Transactional
    @Override
    public List<EntityImage> getEntityImages(AbstractEntity entity) {
        QueryParameters params = new QueryParameters();
        params.setAutocreateSearcheableStrings(false);
        params.add("targetEntity", QueryConditions.eq(entity.getClass().getName()));
        params.add("targetEntityId", QueryConditions.eq(entity.getId()));
        params.add("state", QueryConditions.notEq(EntityFileState.DELETED));
        /*
         * if (parentDirectory == null) { params.add("parentDirectory",
         * QueryConditions.isNull()); } else { params.add("parentDirectory",
         * parentDirectory);
        }
         */
        params.orderBy("type", true);
        return crudService.find(EntityImage.class, params);
    }

    @Override
    public FilesConfig getConfiguration() {
        FilesConfig fileConfig = Containers.get().findObject(FilesConfig.class);
        if (fileConfig == null) {
            fileConfig = new FilesConfig();
        }
        return fileConfig;

    }

    @Override
    public InputStream download(EntityFile file) throws FileNotFoundException {
        String filePath = getConfiguration().getRepository() + "/" + file.getId();
        return new FileInputStream(new File(filePath));
    }

    @Transactional
    @Override
    public EntityImage createEntityImage(UploadedFileInfo fileInfo, String className, Long classId, String deString) {
        EntityImage entityImage = new EntityImage();
        entityImage.setDescription(deString);
        entityImage.setContentType(fileInfo.getContentType());
        entityImage.setName(fileInfo.getFullName());
        entityImage.setExtension(StringUtils.getFilenameExtension(fileInfo.getFullName()));
        entityImage.setTargetEntity(className);
        entityImage.setTargetEntityId(classId);
        entityImage.setType(EntityFileType.FILE);
        entityImage.setParentDirectory(fileInfo.getParent());
        entityImage.setState(EntityFileState.VALID);
        crudService.create(entityImage);

        String archivoDestino = getConfiguration().getRepository() + "/" + entityImage.getId();
        String archivoRedimencionado = getConfiguration().getRepository() + "/" + entityImage.getId();
        String archivoThumbnail = getConfiguration().getRepository() + "/t" + entityImage.getId();
        try {            
            
            FilesConfig filesConfig=getConfiguration();
            File file=File.createTempFile("img", "ent");
            IOUtils.copy(fileInfo.getInputStream(), file);
            
            File realFile = new File(archivoRedimencionado);
            realFile.createNewFile();
            ImageUtil.resizeJPEGImage(file, realFile, filesConfig.getMaxSize(), filesConfig.getMaxSize());
            
            File thumbNail = new File(archivoThumbnail);
            thumbNail.createNewFile();
            ImageUtil.resizeJPEGImage(file, thumbNail, filesConfig.getMaxThumbnail(), filesConfig.getMaxThumbnail());
            //IOUtils.copy(fileInfo.getInputStream(), realFile);
            System.out.println("File Saved: " + thumbNail.getName() + "  length: " + thumbNail.length());
            
            file.delete();
            
            crudService.updateField(entityImage, "size", realFile.length());

        } catch (IOException e) {
            throw new EntityFileException("Error writing file " + fileInfo.getFullName() + " no new location " + archivoRedimencionado, e);
        }
        
        /*String newFileName = getConfiguration().getRepository() + "/" + entityImage.getId();
        try {
            File realFile = new File(newFileName);
            realFile.createNewFile();
            IOUtils.copy(fileInfo.getInputStream(), realFile);
            System.out.println("File Saved: " + realFile.getName() + "  length: " + realFile.length());
            crudService.updateField(entityImage, "size", realFile.length());

        } catch (IOException e) {
            throw new EntityFileException("Error writing file " + fileInfo.getFullName() + " no new location " + newFileName, e);
        }*/

        return entityImage;
    }

    /*
     * (Media media, EntityImage entityImage, AbstractEntity targetEntity) {
     *
     * entityImage.setTargetEntity(targetEntity.getClass().getName());
     * entityImage.setTargetEntityId((Long) targetEntity.getId());
     * System.out.println("SAVE!!!!!!!!"); System.out.println("SAVE:" +
     * entityImage.getName()); System.out.println("SAVE:" +
     * entityImage.getType()); System.out.println("SAVE:" +
     * entityImage.getTargetEntity()); EntityImage enp =
     * crudService.save(entityImage, entityImage.getId()); String newFileName =
     * getConfiguration().getRepository() + "/" + enp.getId(); try { File
     * realFile = new File(newFileName); realFile.createNewFile();
     * IOUtils.copy(enp.getFile(), realFile); System.out.println("File Saved: "
     * + realFile.getName() + " length: " + realFile.length());
     * crudService.updateField(entityImage, "size", realFile.length()); } catch
     * (IOException e) { e.printStackTrace(); } return enp;
    }
     */
    @Override
    public EntityPhoto createEntityPhoto(EntityPhoto entityPhoto, AbstractEntity targetEntity) {

        System.out.println("SAVE UP!!!!!!!!");
        EntityNoPhoto noPhoto = getNoPhoto();
        if (entityPhoto.getId() != null) {
            if (entityPhoto.getId() == noPhoto.getId()) {
                entityPhoto = crudService.find(EntityNoPhoto.class, entityPhoto.getId());
                return entityPhoto;
            }
        }
        entityPhoto.setTargetEntity(targetEntity.getClass().getName());
        entityPhoto.setTargetEntityId((Long) targetEntity.getId());
        System.out.println("SAVE!!!!!!!!");
        System.out.println("SAVE:" + entityPhoto.getName());
        System.out.println("SAVE:" + entityPhoto.getType());
        System.out.println("SAVE:" + entityPhoto.getTargetEntity());
        EntityPhoto enp = crudService.save(entityPhoto, entityPhoto.getId());
        String newFileName = getConfiguration().getRepository() + "/" + enp.getId();
        
        String archivoDestino = getConfiguration().getRepository() + "/" + entityPhoto.getId();
        String archivoRedimencionado = getConfiguration().getRepository() + "/" + entityPhoto.getId();
        String archivoThumbnail = getConfiguration().getRepository() + "/t" + entityPhoto.getId();
        try {            
            
            FilesConfig filesConfig=getConfiguration();
            File file=File.createTempFile("img", "ent");
            IOUtils.copy(enp.getFile(), file);
            
            File realFile = new File(archivoRedimencionado);
            realFile.createNewFile();
            ImageUtil.resizeJPEGImage(file, realFile, filesConfig.getMaxSize(), filesConfig.getMaxSize());
            
            File thumbNail = new File(archivoThumbnail);
            thumbNail.createNewFile();
            ImageUtil.resizeJPEGImage(file, thumbNail, filesConfig.getMaxThumbnail(), filesConfig.getMaxThumbnail());
            //IOUtils.copy(fileInfo.getInputStream(), realFile);
            System.out.println("File Saved: " + thumbNail.getName() + "  length: " + thumbNail.length());
            
            file.delete();
            
            crudService.updateField(entityPhoto, "size", realFile.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*try {
            File realFile = new File(newFileName);
            realFile.createNewFile();
            IOUtils.copy(enp.getFile(), realFile);
            System.out.println("File Saved: " + realFile.getName() + "  length: " + realFile.length());
            crudService.updateField(entityPhoto, "size", realFile.length());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        
        return enp;





        //if (entityPhoto.getNewEntityFile() != null) {
            /*
         * if (entityPhoto instanceof EntityNoPhoto) { EntityNoPhoto
         * entityNoPhoto = (EntityNoPhoto) entityPhoto; if
         * (entityNoPhoto.getNewEntityFile() != null) { EntityPhoto enp =
         * crudService.save(entityNoPhoto.getNewEntityFile(),
         * entityNoPhoto.getNewEntityFile().getId()); return enp; } } else {
         */
        //if (entityPhoto.getNewEntityFile() != null) {
        //EntityPhoto enp = crudService.save(entityPhoto.getNewEntityFile(), entityPhoto.getNewEntityFile().getId());
        //return enp;
        //}
        //}
        //}
        //return entityPhoto;

        /*
         * EntityPhoto entityFile = new EntityPhoto();
         * entityFile.setContentType(targetEntity.);
         * entityFile.setName(fileInfo.getFullName());
         * entityFile.setExtension(StringUtils.getFilenameExtension(fileInfo.getFullName()));
         * entityFile.setTargetEntity(targetEntity.getClass().getName());
         * entityFile.setTargetEntityId((Long) targetEntity.getId());
         * entityFile.setType(EntityFileType.FILE);
         * entityFile.setParentDirectory(fileInfo.getParent());
         * entityFile.setState(EntityFileState.VALID);
         */
        //crudService.save(entityPhoto, entityPhoto.getId());

        /*
         * String newFileName = getConfiguration().getRepository() + "/" +
         * entityPhoto.getId(); try { File realFile = new File(newFileName);
         * realFile.createNewFile(); IOUtils.copy(new
         * FileInputStream(entityPhoto.getFile()), realFile);
         * System.out.println("File Saved: " + realFile.getName() + " length: "
         * + realFile.length()); crudService.updateField(entityPhoto, "size",
         * realFile.length());
         *
         * } catch (IOException e) { throw new EntityFileException("Error
         * writing file " + entityPhoto.getName() + "." +
         * entityPhoto.getExtension() + " no new location " + newFileName, e); }
         */

        //return entityPhoto;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public EntityNoPhoto getNoPhoto() {
        List<EntityNoPhoto> lista = crudService.findAll(EntityNoPhoto.class);
        if (lista.size() > 0) {
            return lista.get(0);
        } else {
            EntityNoPhoto entityNoPhoto = new EntityNoPhoto();
            entityNoPhoto.setName("Sin Asignar");
            entityNoPhoto.setType(EntityFileType.FILE);
            entityNoPhoto.setTargetEntity(EntityNoPhoto.class.getName());
            entityNoPhoto = crudService.save(entityNoPhoto, entityNoPhoto.getId());
            return entityNoPhoto;
        }
    }

    @Transactional
    @Override
    public EntityPhoto updateEntityPhoto(EntityPhoto image, EntityPhoto imageTrabajo) {
        System.out.println("IMG_:" + image);
        System.out.println("IMG_:" + image.getId());
        try {
            System.out.println("IMG_TRAB:" + imageTrabajo);
            System.out.println("IMG_TRAB:" + imageTrabajo.getId());
        } catch (Exception e) {
        }
        //GUARDAR HERENCIA
        if (image.getId() != getNoPhoto().getId()) {
            EntityPhoto entityPhoto = image.clone();
            entityPhoto.setId(null);
            entityPhoto.setState(EntityFileState.DELETED);
            entityPhoto = crudService.save(entityPhoto, entityPhoto.getId());
            String archivoOriginal = getConfiguration().getRepository() + "/" + image.getId();
            String archivoNuevo = getConfiguration().getRepository() + "/" + entityPhoto.getId();
            System.out.println("" + archivoOriginal);
            System.out.println("" + archivoNuevo);
            File realFile = new File(archivoOriginal);
            File realDestino = new File(archivoNuevo);
            realFile.renameTo(realDestino);
        }
        //MODIFICAR PADRE
        if (imageTrabajo.getId() == null) {
            if (image.getId() != getNoPhoto().getId()) {
                System.out.println("CASO 1");
                Long idx = image.getId();
                String clase = image.getTargetEntity();
                Long idClase = image.getTargetEntityId();
                imageTrabajo.copiar(image);
                image.setId(idx);
                image.setTargetEntity(clase);
                image.setTargetEntityId(idClase);
                String newFileName = getConfiguration().getRepository() + "/" + image.getId();
                try {
                    File realFile = new File(newFileName);
                    realFile.createNewFile();
                    IOUtils.copy(image.getFile(), realFile);
                    image.setSize(realFile.length());
                    //crudService.updateField(image, "size", );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image = crudService.save(image, image.getId());
            } else {
                //EntityPhoto nuevo=new EntityPhoto();
                //byte[] bytes=imageTrabajo.getFile();
                System.out.println("CASO 2");
                imageTrabajo.setTargetEntity(EntityPhoto.class.getName());
                image = crudService.save(imageTrabajo, imageTrabajo.getId());
                String newFileName = getConfiguration().getRepository() + "/" + image.getId();
                try {
                    File realFile = new File(newFileName);
                    realFile.createNewFile();
                    IOUtils.copy(image.getFile(), realFile);
                    crudService.updateField(image, "size", realFile.length());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            image = getNoPhoto();
        }
        //GUARDAR PADRE
        return image;
    }
}
