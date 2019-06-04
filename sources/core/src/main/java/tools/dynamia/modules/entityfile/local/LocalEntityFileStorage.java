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

import org.springframework.beans.factory.annotation.Autowired;
import tools.dynamia.domain.query.Parameters;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.sterotypes.Service;
import tools.dynamia.io.IOUtils;
import tools.dynamia.modules.entityfile.EntityFileException;
import tools.dynamia.modules.entityfile.EntityFileStorage;
import tools.dynamia.modules.entityfile.StoredEntityFile;
import tools.dynamia.modules.entityfile.UploadedFileInfo;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.domain.enums.EntityFileState;
import tools.dynamia.web.util.HttpUtils;

import java.io.File;
import java.io.IOException;

@Service
public class LocalEntityFileStorage implements EntityFileStorage {

    public static final String ID = "LocalStorage";
    private static final String LOCAL_FILES_LOCATION = "LOCAL_FILES_LOCATION";
    private static final String LOCAL_USE_HTTPS = "LOCAL_USE_HTTPS";
    private static final String LOCAL_CONTEXT_PATH = "LOCAL_CONTEXT_PATH";
    private static final String DEFAULT_LOCATION = System.getProperty("user.home") + "/localentityfiles";
    static final String LOCAL_FILE_HANDLER = "/storage/";

    @Autowired
    private Parameters appParams;

    @Autowired
    private CrudService crudService;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Local File Storage";
    }

    @Override
    public void upload(EntityFile entityFile, UploadedFileInfo fileInfo) {
        File realFile = getRealFile(entityFile);

        try {
            IOUtils.copy(fileInfo.getInputStream(), realFile);
        } catch (IOException e) {
            throw new EntityFileException("Error upload local file " + realFile, e);
        }

    }

    @Override
    public StoredEntityFile download(EntityFile entityFile) {
        String url = generateURL(entityFile);
        StoredEntityFile sef = new LocalStoredEntityFile(entityFile, url, getRealFile(entityFile));
        return sef;
    }

    private String generateURL(EntityFile entityFile) {
        String serverPath = HttpUtils.getServerPath();
        boolean useHttps = isUseHttps();

        if (useHttps && serverPath.startsWith("http:")) {
            serverPath = serverPath.replace("http:", "https:");
        }

        String context = getContextPath();

        if (context == null) {
            context = "";
        }

        String fileName = entityFile.getName();
        fileName = fileName.replace(" ", "%20");
        String url = serverPath + context + LOCAL_FILE_HANDLER + fileName + "?uuid=" + entityFile.getUuid();

        return url;
    }

    private String getContextPath() {
        String context = System.getProperty(LOCAL_CONTEXT_PATH);
        try {
            if (context == null) {
                context = appParams.getValue(LOCAL_CONTEXT_PATH, "");
            }
        } catch (Exception e) {
            context = "";
        }
        return context;
    }

    private boolean isUseHttps() {
        boolean useHttps = false;

        String useHttpsProperty = System.getProperty(LOCAL_USE_HTTPS);
        if (useHttpsProperty != null && (useHttpsProperty.equals("true") || useHttpsProperty.equals("false"))) {
            useHttps = Boolean.parseBoolean(useHttpsProperty);
        } else {
            try {
                useHttps = Boolean.parseBoolean(appParams.getValue(LOCAL_USE_HTTPS, "false"));
            } catch (Exception e) {
                //ignore
            }
        }
        return useHttps;
    }

    public File getParentDir() {

        String path = System.getProperty(LOCAL_FILES_LOCATION);
        if (path == null) {
            path = appParams.getValue(LOCAL_FILES_LOCATION, DEFAULT_LOCATION);
        }
        return new File(path);
    }

    private File getRealFile(EntityFile entityFile) {
        String subfolder = "";
        if (entityFile.getSubfolder() != null) {
            subfolder = entityFile.getSubfolder() + "/";
        }

        String storedFileName = entityFile.getUuid();
        if (entityFile.getStoredFileName() != null && !entityFile.getStoredFileName().isEmpty()) {
            storedFileName = entityFile.getStoredFileName();
        }

        String filePath = "Account" + entityFile.getAccountId() + "/" + subfolder + storedFileName;
        File parentDir = getParentDir();
        File realFile = new File(parentDir, filePath);
        try {

            if (!realFile.getParentFile().exists()) {
                realFile.getParentFile().mkdirs();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return realFile;
    }

    @Override
    public void delete(EntityFile entityFile) {
        try {
            File realFile = getRealFile(entityFile);
            if (realFile != null && realFile.exists()) {
                realFile.delete();
            }
            entityFile.setState(EntityFileState.DELETED);
            crudService.update(entityFile);
        } catch (Exception e) {
            throw new EntityFileException("Error deleting entity file " + entityFile, e);
        }
    }

    private class LocalStoredEntityFile extends StoredEntityFile {

        /**
         *
         */
        private static final long serialVersionUID = -6295813096900514353L;

        public LocalStoredEntityFile(EntityFile entityFile, String url, File realFile) {
            super(entityFile, url, realFile);
            // TODO Auto-generated constructor stub
        }

        @Override
        public String getThumbnailUrl(int width, int height) {
            return getUrl() + "&w=" + width + "&h=" + height;
        }

    }

}
