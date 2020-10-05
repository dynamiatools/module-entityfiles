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

import org.zkoss.zul.A;

import tools.dynamia.ui.icons.IconSize;
import tools.dynamia.ui.icons.IconsTheme;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;
import tools.dynamia.zk.util.ZKUtil;

public class FilesCountImage extends A {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IconSize iconSize = IconSize.SMALL;
	private String icon = "attachment";
	private int value;

	static {
		BindingComponentIndex.getInstance().put("value", FilesCountImage.class);
		ComponentAliasIndex.getInstance().add("filescountimage", FilesCountImage.class);
	}

	public FilesCountImage() {

	}

	public FilesCountImage(int value) {
		this.value = value;
	}

	public void setValue(int value) {
		try {
			int count = value;
			if (count > 0) {
				ZKUtil.configureComponentIcon(icon, this, iconSize);
				setTooltiptext(count + " archivos adjuntos");
			} else {
				setImage(null);
				getChildren().clear();
				setTooltiptext(null);
			}
			setLabel("");
		} catch (Exception e) {
			setLabel("");
			setImage(null);
		}
	}

	public int getValue() {
		return value;
	}

	public IconSize getIconSize() {
		return iconSize;
	}

	public void setIconSize(IconSize iconSize) {
		this.iconSize = iconSize;
	}

	public void setIconSize(String iconSize) {
		this.iconSize = IconSize.valueOf(iconSize.toUpperCase());
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
