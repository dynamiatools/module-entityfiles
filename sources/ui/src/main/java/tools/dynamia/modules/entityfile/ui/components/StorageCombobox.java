package tools.dynamia.modules.entityfile.ui.components;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import tools.dynamia.integration.Containers;
import tools.dynamia.modules.entityfile.EntityFileStorage;
import tools.dynamia.zk.ComponentAliasIndex;

public class StorageCombobox extends Combobox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3507817129731334840L;

	static {
		ComponentAliasIndex.getInstance().put("storageCombobox", StorageCombobox.class);
	}

	public StorageCombobox() {
		setReadonly(true);
		getChildren().clear();
		Containers.get().findObjects(EntityFileStorage.class).forEach(efs -> {
			Comboitem item = new Comboitem();
			item.setLabel(efs.getName());
			item.setValue(efs.getId());
			appendChild(item);
		});
	}

}
