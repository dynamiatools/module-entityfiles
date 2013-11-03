/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile;

import com.dynamia.tools.web.cfg.ConfigPage;
import com.dynamia.tools.web.navigation.Module;
import com.dynamia.tools.web.navigation.ModuleProvider;
import com.dynamia.tools.web.navigation.PageGroup;
import org.springframework.stereotype.Component;

/**
 *
 * @author mario
 */
@Component("EntityFileModule")
public class Installer implements ModuleProvider {

    @Override
    public Module getModule() {
        Module module = new Module("system", "Sistema");
        module.setPosition(10);
        module.setIcon("icons:tool");
        {
            PageGroup pg = new PageGroup("config", "Configuracion");
            module.addPageGroup(pg);
            {
                pg.addPage(new ConfigPage("entityFile", "Archivos", "EntityFileCFG"));
                
            }
        }
        return module;
    }

}
