/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.entityfile.ui;

import org.springframework.stereotype.Component;

import tools.dynamia.navigation.Module;
import tools.dynamia.navigation.ModuleProvider;
import tools.dynamia.navigation.PageGroup;
import tools.dynamia.zk.crud.cfg.ConfigPage;

/**
 *
 * @author mario
 */
@Component("EntityFileModule")
public class Installer implements ModuleProvider {

	@Override
	public Module getModule() {
		Module module = new Module("system", "Sistema");
		module.setPosition(Integer.MAX_VALUE);
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
