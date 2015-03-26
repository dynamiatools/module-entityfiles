package tools.dynamia.modules.entityfile;

import java.io.File;
import java.io.Serializable;

import tools.dynamia.modules.entityfile.domain.EntityFile;

public abstract class StoredEntityFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 421213041955145817L;
	private EntityFile entityFile;
	private String url;
	private File realFile;

	public StoredEntityFile(EntityFile entityFile, String url, File realFile) {
		super();
		this.entityFile = entityFile;
		this.url = url;
		this.realFile = realFile;
	}

	public EntityFile getEntityFile() {
		return entityFile;
	}

	public String getUrl() {
		return url;
	}

	public String getThumbnailUrl() {
		return getThumbnailUrl(100, 100);
	}

	public abstract String getThumbnailUrl(int width, int height);

	public File getRealFile() {
		return realFile;
	}

}
