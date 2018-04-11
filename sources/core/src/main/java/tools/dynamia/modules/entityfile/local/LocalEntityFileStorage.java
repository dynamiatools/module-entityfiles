package tools.dynamia.modules.entityfile.local;

import java.io.File;
import java.io.IOException;

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

@Service
public class LocalEntityFileStorage implements EntityFileStorage {

    public static final String ID = "LocalStorage";
    private static final String LOCAL_FILES_LOCATION = "LOCAL_FILES_LOCATION";
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
        String url = serverPath + LOCAL_FILE_HANDLER + entityFile.getName() + "?uuid=" + entityFile.getUuid();
        url = url.replace(" ", "%20");
        return url;
    }

    public File getParentDir() {
        String path = appParams.getValue(LOCAL_FILES_LOCATION, DEFAULT_LOCATION);
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
