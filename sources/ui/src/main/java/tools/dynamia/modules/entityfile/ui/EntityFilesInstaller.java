/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.entityfile.ui;

import tools.dynamia.integration.sterotypes.Provider;
import tools.dynamia.navigation.Module;
import tools.dynamia.navigation.ModuleProvider;
import tools.dynamia.navigation.PageGroup;
import tools.dynamia.zk.crud.cfg.ConfigPage;

/**
 *
 * @author mario
 */
@Provider
public class EntityFilesInstaller implements ModuleProvider {

	@Override
	public Module getModule() {
		Module module = Module.getRef("system");

		PageGroup pg = new PageGroup("config", "Configuracion");
		module.addPageGroup(pg);
		{
			pg.addPage(new ConfigPage("entityFile", "Archivos", "EntityFileCFG"));

		}

		return module;
	}

}
