/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.dynamia.modules.entityfile.ui;

import tools.dynamia.ui.icons.AbstractIconsProvider;
import tools.dynamia.ui.icons.InstallIcons;

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
