/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile.domain;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.tools.integration.Containers;

/**
 *
 * @author ronald
 */
@Entity
@Table(name = "entity_photo")
public class EntityPhoto extends BasicEntityFile {

    @Transient
    private byte[] file;
    @Transient
    private EntityPhoto newEntityFile;

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public EntityPhoto getNewEntityFile() {
        return newEntityFile;
    }

    public void setNewEntityFile(EntityPhoto newEntityFile) {
        this.newEntityFile = newEntityFile;
    }

    @PostLoad
    public void loadImage() {
        System.out.println("LOAD PHOTO");
        if (getFile() == null) {
            if (getId() != null) {
                EntityFileService entityFileService = Containers.get().findObject(EntityFileService.class);
                String newFileName = entityFileService.getConfiguration().getRepository() + "/" + getId();
                File filex = new File(newFileName);

                try {
                    InputStream inputStream = new FileInputStream(filex);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte buf[] = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.close();
                    inputStream.close();
                    setFile(out.toByteArray());
                } catch (Exception e) {
                }
            }
        }
    }

    public void copiar(EntityPhoto entityPhoto) {
        entityPhoto.setContentType(getContentType());
        entityPhoto.setCreationDate(getCreationDate());
        entityPhoto.setCreationTime(getCreationTime());
        entityPhoto.setCreator(getCreator());
        entityPhoto.setDescription(getDescription());
        entityPhoto.setExtension(getExtension());
        entityPhoto.setFile(getFile());
        entityPhoto.setId(getId());
        entityPhoto.setLastUpdate(getLastUpdate());
        entityPhoto.setLastUpdater(getLastUpdater());
        entityPhoto.setName(getName());
        entityPhoto.setParentDirectory(getParentDirectory());
        entityPhoto.setShared(isShared());
        entityPhoto.setSize(getSize());
        entityPhoto.setState(getState());
        entityPhoto.setTargetEntity(getTargetEntity());
        entityPhoto.setTargetEntityId(getTargetEntityId());
        entityPhoto.setType(getType());
    }

    @Override
    public EntityPhoto clone() {
        EntityPhoto entityPhoto = new EntityPhoto();
        copiar(entityPhoto);
        return entityPhoto;
    }
}
