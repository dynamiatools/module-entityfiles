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

import tools.dynamia.commons.Messages;
import tools.dynamia.integration.Containers;
import tools.dynamia.io.FileInfo;
import tools.dynamia.modules.entityfile.EntityFileException;
import tools.dynamia.modules.entityfile.UploadedFileInfo;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.service.EntityFileService;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;
import tools.dynamia.zk.ui.Uploadlink;

import java.io.File;

public class EntityFileUploadlink extends Uploadlink {

    /**
     *
     */
    private static final long serialVersionUID = -2182747459195865750L;

    static {
        BindingComponentIndex.getInstance().put("value", EntityFileUploadlink.class);
        ComponentAliasIndex.getInstance().put("entityfileUploadlink", EntityFileUploadlink.class);
    }

    private EntityFile entityFile;
    private EntityFileService service = Containers.get().findObject(EntityFileService.class);
    private boolean shared;
    private String subfolder;
    private String storedFileName;

    public EntityFile getValue() {
        if (entityFile == null && getUploadedFile() != null) {
            onFileUpload();
        }

        return entityFile;
    }

    public void setValue(EntityFile entityFile) {
        this.entityFile = entityFile;
        if (entityFile != null) {
            configureFileInfo();
        } else {
            setUploadedFile(null);
            setLabel(Messages.get(Uploadlink.class, "upload"));
        }
    }

    private void configureFileInfo() {
        setUploadedFile(new FileInfo(new File("")));
        setLabel(entityFile.getName());
        setClientDataAttribute("uuid", entityFile.getUuid());
    }

    @Override
    protected void onFileUpload() {

        try {
            UploadedFileInfo uploadedFileInfo = new UploadedFileInfo(getUploadedFile());
            uploadedFileInfo.setShared(isShared());
            uploadedFileInfo.setSubfolder(getSubfolder());
            if (storedFileName != null && !storedFileName.isEmpty()) {
                if (storedFileName.equals("real")) {
                    uploadedFileInfo.setStoredFileName(getUploadedFile().getName());
                } else {
                    uploadedFileInfo.setStoredFileName(storedFileName);
                }
            }
            entityFile = service.createTemporalEntityFile(uploadedFileInfo);

            setLabel(entityFile.getName());

        } catch (Exception e) {
            throw new EntityFileException(e);
        }

    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public String getSubfolder() {
        return subfolder;
    }

    public void setSubfolder(String subfolder) {
        this.subfolder = subfolder;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

}
