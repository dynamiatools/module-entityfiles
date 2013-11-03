/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dynamia.modules.entityfile;

import com.dynamia.tools.web.icons.AbstractIconsProvider;
import com.dynamia.tools.web.icons.InstallIcons;

/**
 *
 * @author mario
 */
@InstallIcons
public class EntityFileIcons extends AbstractIconsProvider{

    @Override
    public String getPrefix() {
        return "entityfiles/icons";
    }

    @Override
    public String getExtension() {
        return "png";
    }
    
}
