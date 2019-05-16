package tools.dynamia.modules.entityfile.ui.components;

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

import java.io.FileInputStream;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Toolbarbutton;

import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.ui.util.EntityFileUtils;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;

public class EntityFileDownloadlink extends Toolbarbutton {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2182747459195865750L;

	static {
		BindingComponentIndex.getInstance().put("value", EntityFileDownloadlink.class);
		ComponentAliasIndex.getInstance().put("entityfileDownloadlink", EntityFileDownloadlink.class);
	}

	private EntityFile entityFile;

	public EntityFileDownloadlink() {
		addEventListener(Events.ON_CLICK, event -> {
			if (entityFile != null) {
				EntityFileUtils.showDownloadDialog(entityFile.getStoredEntityFile());
			}
		});
	}

	public EntityFile getValue() {
		return entityFile;
	}

	public void setValue(EntityFile entityFile) {
		this.entityFile = entityFile;
		setLabel(entityFile.getName());
		setTooltiptext(entityFile.getDescription());
	}

}
