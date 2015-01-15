package com.dynamia.modules.entityfile.ui;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import com.dynamia.modules.entityfile.EntityFileException;
import com.dynamia.modules.entityfile.UploadedFileInfo;
import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.io.FileInfo;
import com.dynamia.tools.viewers.zk.BindingComponentIndex;
import com.dynamia.tools.viewers.zk.ComponentAliasIndex;
import com.dynamia.tools.web.ui.Uploadlink;

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

	public EntityFileUploadlink() {
		addEventListener(ON_FILE_UPLOADED, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				createEntityFile();
			}
		});
	}

	public EntityFile getValue() {
		if (entityFile == null && getUploadedFile() != null) {
			createEntityFile();
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

	private void createEntityFile() {
		try {
			entityFile = service.createTemporalEntityFile(new UploadedFileInfo(getUploadedFile()));
			setLabel(entityFile.getName());
		} catch (Exception e) {
			throw new EntityFileException(e);
		}

	}
}
