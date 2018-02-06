package tools.dynamia.modules.entityfile;

import tools.dynamia.modules.entityfile.domain.EntityFile;

public interface EntityFileStorage {

    String getId();

    String getName();

    void upload(EntityFile entityFile, UploadedFileInfo fileInfo);

    StoredEntityFile download(EntityFile entityFile);

    void delete(EntityFile entityFile);

}
