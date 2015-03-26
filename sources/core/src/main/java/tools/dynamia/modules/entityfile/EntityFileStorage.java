package tools.dynamia.modules.entityfile;

import tools.dynamia.modules.entityfile.domain.EntityFile;

public interface EntityFileStorage {

	public String getId();

	public String getName();

	public void upload(EntityFile entityFile, UploadedFileInfo fileInfo);

	public StoredEntityFile download(EntityFile entityFile);

}
