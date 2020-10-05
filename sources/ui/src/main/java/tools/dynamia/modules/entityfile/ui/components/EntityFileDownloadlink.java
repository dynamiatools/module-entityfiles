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

import java.io.FileInputStream;
import java.net.URL;

import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Toolbarbutton;

import tools.dynamia.commons.StringUtils;
import tools.dynamia.modules.entityfile.StoredEntityFile;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.ui.util.EntityFileUtils;
import tools.dynamia.web.util.HttpUtils;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;
import tools.dynamia.zk.util.ZKUtil;

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
    private boolean showLabel = true;

    public EntityFileDownloadlink() {
        setIconSclass("z-icon-paperclip");
        addEventListener(Events.ON_CLICK, event -> {
            if (entityFile != null) {
                StoredEntityFile sef = entityFile.getStoredEntityFile();
                if (sef.getRealFile() != null && sef.getRealFile().exists()) {
                    Media media = new AMedia(entityFile.getName(), entityFile.getExtension(), null, sef.getRealFile(), null);
                    Filedownload.save(media, entityFile.getName());
                } else {
                    EntityFileUtils.showDownloadDialog(entityFile.getStoredEntityFile());
                }

            }
        });
    }

    public EntityFile getValue() {
        return entityFile;
    }

    public void setValue(EntityFile entityFile) {
        this.entityFile = entityFile;
        rendererLabel();
    }

    private void rendererLabel() {
        if (showLabel) {
            setLabel(entityFile.getName());
        }
        setTooltiptext(entityFile.getName());
    }

    public boolean isShowLabel() {
        return showLabel;
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }
}
