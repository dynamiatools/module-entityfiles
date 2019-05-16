package tools.dynamia.modules.entityfile.local;

/*-
 * #%L
 * Dynamia Modules - EntityFiles - Core
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

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import tools.dynamia.commons.StringUtils;
import tools.dynamia.integration.Containers;
import tools.dynamia.io.IOUtils;
import tools.dynamia.io.ImageUtil;
import tools.dynamia.io.impl.SpringResource;
import tools.dynamia.modules.entityfile.EntityFileAccountProvider;
import tools.dynamia.modules.entityfile.StoredEntityFile;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.enums.EntityFileType;
import tools.dynamia.modules.entityfile.service.EntityFileService;

public class LocalEntityFileStorageHandler extends ResourceHttpRequestHandler {

    private static final String UUID = "/uuid/";
	private LocalEntityFileStorage storage;
    private EntityFileService service;
    private EntityFileAccountProvider accountProvider;

    @Override
    protected Resource getResource(HttpServletRequest request) {
        if (service == null) {
            service = Containers.get().findObject(EntityFileService.class);
        }
        if (storage == null) {
            storage = Containers.get().findObject(LocalEntityFileStorage.class);
        }

        if (accountProvider == null) {
            accountProvider = Containers.get().findObject(EntityFileAccountProvider.class);
            if (accountProvider == null) {
                accountProvider = () -> 0L;
            }
        }

        File file = null;
        String uuid = getParam(request, "uuid", null);
        
        if(uuid==null){
        	String path = request.getPathInfo();
        	if(path.contains(UUID)){
        		uuid = path.substring(path.lastIndexOf(UUID)+UUID.length());
        		uuid = StringUtils.removeFilenameExtension(uuid);
        	}
        }
        
        if (uuid == null) {
            return null;
        }

        Long currentAccountId = accountProvider.getAccountId();
        EntityFile entityFile = service.getEntityFile(uuid);

        if (entityFile != null && (entityFile.isShared() || entityFile.getAccountId().equals(currentAccountId))) {

            StoredEntityFile storedEntityFile = storage.download(entityFile);

            file = storedEntityFile.getRealFile();

            if (entityFile.getType() == EntityFileType.IMAGE) {

                if (isThumbnail(request)) {
                    file = createOrLoadThumbnail(file, entityFile, request);
                }

                if (!file.exists()) {
                    SpringResource notFoundResource = (SpringResource) IOUtils.getResource("classpath:/web/tools/images/no-photo.jpg");
                    return notFoundResource.getInternalResource();
                }
            }
        }
        if (file != null) {
            return new FileSystemResource(file);
        } else {
            return null;
        }
    }

    private boolean isThumbnail(HttpServletRequest request) {
        return getParam(request, "w", null) != null && getParam(request, "h", null) != null;
    }

    private File createOrLoadThumbnail(File realImg, EntityFile entityFile, HttpServletRequest request) {

        String w = getParam(request, "w", "200");
        String h = getParam(request, "h", "200");
        String subfolder = w + "x" + h;

        File realThumbImg = new File(realImg.getParentFile(), subfolder + "/" + realImg.getName());
        if (!realThumbImg.exists()) {
            if (realImg.exists()) {
                ImageUtil.resizeImage(realImg, realThumbImg, entityFile.getExtension(), Integer.parseInt(w), Integer.parseInt(h));
            }
        }
        return realThumbImg;

    }

    public String getParam(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null || value.trim().isEmpty()) {
            value = defaultValue;
        }
        return value;
    }
}
