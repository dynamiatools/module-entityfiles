
/*
 * Copyright (C)  2020. Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 *  All Rights Reserved.
 *
 * This file is free software: you can redistribute it and/or modify it  under the terms of the
 *  GNU Lesser General Public License (LGPL v3) as published by the Free Software Foundation,
 *   either version 3 of the License, or (at your option) any later version.
 *
 *  This file is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *   without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *   See the GNU Lesser General Public License for more details. You should have received a copy of the
 *   GNU Lesser General Public License along with this file.
 *   If not, see <https://www.gnu.org/licenses/>.
 *
 */

package tools.dynamia.modules.entityfile.ui;

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
