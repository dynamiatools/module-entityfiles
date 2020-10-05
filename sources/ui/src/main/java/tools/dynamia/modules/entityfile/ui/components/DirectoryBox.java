
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

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;

import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;

/**
 *
 * @author Mario Serrano Leones
 */
public class DirectoryBox extends Bandbox {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7769832324226733919L;

	static {
        BindingComponentIndex.getInstance().put("value", DirectoryBox.class);
        ComponentAliasIndex.getInstance().add(DirectoryBox.class);
    }

    private DirectoryExplorer explorer;
    private Bandpopup popup;

    public DirectoryBox() {
        this(null);
    }

    public DirectoryBox(String value) throws WrongValueException {
        super(value);
        explorer = new DirectoryExplorer();
        popup = new Bandpopup();
        popup.appendChild(explorer);
        
        popup.setWidth("400px");
        popup.setHeight("300px");
        
        appendChild(popup);

        explorer.addEventListener(Events.ON_CHANGE, event -> {
		    setValue(event.getData().toString());
		    Events.postEvent(new Event(Events.ON_CHANGE, DirectoryBox.this, getValue()));
		});

    }

    

}
