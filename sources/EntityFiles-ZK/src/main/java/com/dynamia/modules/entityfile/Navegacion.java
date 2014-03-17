/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile;

import com.dynamia.modules.entityfile.domain.EntityFile;
import org.zkoss.zul.Toolbarbutton;

/**
 *
 * @author programador
 */
public class Navegacion extends Toolbarbutton {

    private EntityFile entityFile;
    private int index = 0;

    public EntityFile getEntityFile() {
        return entityFile;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setEntityFile(EntityFile entityFile) {
        this.entityFile = entityFile;
        if (entityFile != null) {
            setLabel(entityFile.getName());
        }
    }
}
