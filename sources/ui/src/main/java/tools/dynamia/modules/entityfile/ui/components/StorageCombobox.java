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

package tools.dynamia.modules.entityfile.ui.components;

import java.util.Optional;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;

import tools.dynamia.integration.Containers;
import tools.dynamia.modules.entityfile.EntityFileStorage;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;
import tools.dynamia.zk.util.ZKUtil;

public class StorageCombobox extends Combobox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3507817129731334840L;

	static {
		ComponentAliasIndex.getInstance().put("storagebox", StorageCombobox.class);
		BindingComponentIndex.getInstance().put("selected", StorageCombobox.class);
	}

	private String selected;

	public StorageCombobox() {
		setReadonly(true);

		setItemRenderer((item, data, index) -> {
			EntityFileStorage storage = (EntityFileStorage) data;
			item.setValue(storage.getId());
			item.setLabel(storage.getName());
		});

		ZKUtil.fillCombobox(this, Containers.get().findObjects(EntityFileStorage.class), true);
	}

	public String getSelected() {
		selected = null;
		if (getSelectedItem() != null) {
			selected = getSelectedItem().getValue();
		}
		return selected;
	}

	public void setSelected(String selected) {
		if (selected != this.selected) {
			this.selected = selected;
			try {
				Optional<EntityFileStorage> selectedStorage = Containers.get().findObjects(EntityFileStorage.class)
						.stream().filter(efs -> efs.getId().equals(selected)).findFirst();
				if (selectedStorage.isPresent()) {
					ListModelList model = (ListModelList) getModel();
					model.addToSelection(selectedStorage.get());
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}
