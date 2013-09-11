/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile.domain;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;

/**
 *
 * @author ronald
 */
@Entity
@Table(name = "entity_no_photo")

public class EntityNoPhoto extends EntityPhoto {

    @Override
    public void loadImage() {
        System.out.println("LOAD NO PHOTO");
        try {
            Media media = new AImage(getClass().getResource("/web/images/no_photo.png"));
            InputStream inputStream = media.getStreamData();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
            setFile(out.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    @Override
    public EntityNoPhoto clone() { 
        EntityNoPhoto entityNoPhoto = new EntityNoPhoto();
        copiar(entityNoPhoto);
        return entityNoPhoto;
    }
    public EntityPhoto getEntityPhoto(){
        EntityPhoto entityPhoto=new EntityPhoto();
        copiar(entityPhoto);
        entityPhoto.setNewEntityFile(getNewEntityFile());
        return entityPhoto;
    }
}
