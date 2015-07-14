package tools.dynamia.modules.entityfile.ui.components;

import java.io.File;

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
		}
	}

	private void configureFileInfo() {
		setUploadedFile(new FileInfo(new File("")));
		setLabel(entityFile.getName());
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
