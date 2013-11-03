/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author mario
 */
@Entity
public class EntityImage extends EntityFile {

    @OneToOne
    private EntityImage thumbnail;

    private String width;
    private String height;

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public EntityImage getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(EntityImage thumbnail) {
        this.thumbnail = thumbnail;
    }

}
