package tools.dynamia.modules.entityfile.ui.components;

import tools.dynamia.integration.Containers;
import tools.dynamia.io.FileInfo;
import tools.dynamia.modules.entityfile.EntityFileException;
import tools.dynamia.modules.entityfile.UploadedFileInfo;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.service.EntityFileService;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;
import tools.dynamia.zk.ui.Uploadlink;

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
		}
	}

	private void configureFileInfo() {
		setUploadedFile(new FileInfo(entityFile.getRealFile()));
		setLabel(entityFile.getName());
	}

	@Override
	protected void onFileUpload() {

		try {
			entityFile = service.createTemporalEntityFile(new UploadedFileInfo(getUploadedFile()));
			setLabel(entityFile.getName());
		} catch (Exception e) {
			throw new EntityFileException(e);
		}

	}
}
