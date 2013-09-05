/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.photo.controllers;

import com.dynamia.modules.entityfile.domain.EntityImage;

/**
 *
 * @author ronald
 */
public interface ImageListener {

    public void onAdd();

    public void onEdit(EntityImage entityPhoto);

    public void onDelete(EntityImage entityPhoto);
}
