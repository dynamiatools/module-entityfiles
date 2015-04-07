package tools.dynamia.modules.entityfile.local;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.domain.query.Parameters;
import tools.dynamia.integration.sterotypes.Service;
import tools.dynamia.io.IOUtils;
import tools.dynamia.modules.entityfile.EntityFileException;
import tools.dynamia.modules.entityfile.EntityFileStorage;
import tools.dynamia.modules.entityfile.StoredEntityFile;
import tools.dynamia.modules.entityfile.UploadedFileInfo;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.web.util.HttpUtils;

@Service
public class LocalEntityFileStorage implements EntityFileStorage {

	public static final String ID = "LocalStorage";
	private static final String LOCAL_FILES_LOCATION = "LOCAL_FILES_LOCATION";
	private static final String DEFAULT_LOCATION = System.getProperty("user.home") + "/localentityfiles";
	static final String LOCAL_FILE_HANDLER = "/storage/";

	@Autowired
	private Parameters appParams;

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
			entityFile.setStorageInfo("Stored by " + getClass().getSimpleName());
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
		String url = serverPath + LOCAL_FILE_HANDLER + entityFile.getUuid();
		return url;
	}

	public File getParentDir() {
		String path = appParams.getValue(LOCAL_FILES_LOCATION, DEFAULT_LOCATION);
		return new File(path);
	}

	private File getRealFile(EntityFile entityFile) {
		String filePath = "Account" + entityFile.getAccountId() + "/" + entityFile.getUuid();
		File parentDir = getParentDir();
		File realFile = new File(parentDir, filePath);
		return realFile;
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
			return getUrl() + "?w=" + width + "&h=" + height;
		}

	}

}
