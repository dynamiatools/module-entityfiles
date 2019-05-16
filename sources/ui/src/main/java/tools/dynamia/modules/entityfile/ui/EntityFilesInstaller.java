
package tools.dynamia.modules.entityfile.ui;

/*-
 * #%L
 * Dynamia Modules - EntityFiles - UI
 * %%
 * Copyright (C) 2016 - 2019 Dynamia Soluciones IT SAS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import tools.dynamia.integration.sterotypes.Provider;
import tools.dynamia.navigation.Module;
import tools.dynamia.navigation.ModuleProvider;
import tools.dynamia.navigation.PageGroup;
import tools.dynamia.zk.crud.cfg.ConfigPage;

/**
 *
 * @author Mario Serrano Leones
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
