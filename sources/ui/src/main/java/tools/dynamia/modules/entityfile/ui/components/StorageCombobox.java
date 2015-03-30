package tools.dynamia.modules.entityfile.ui.components;

import java.util.Optional;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;

import tools.dynamia.integration.Containers;
import tools.dynamia.modules.entityfile.EntityFileStorage;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;
import tools.dynamia.zk.util.ZKUtil;

public class StorageCombobox extends Combobox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3507817129731334840L;

	static {
		ComponentAliasIndex.getInstance().put("storagebox", StorageCombobox.class);
		BindingComponentIndex.getInstance().put("selected", StorageCombobox.class);
	}

	private String selected;

	public StorageCombobox() {
		setReadonly(true);

		setItemRenderer((item, data, index) -> {
			EntityFileStorage storage = (EntityFileStorage) data;
			item.setValue(storage.getId());
			item.setLabel(storage.getName());
		});

		ZKUtil.fillCombobox(this, Containers.get().findObjects(EntityFileStorage.class), true);
	}

	public String getSelected() {
		selected = null;
		if (getSelectedItem() != null) {
			selected = getSelectedItem().getValue();
		}
		return selected;
	}

	public void setSelected(String selected) {
		if (selected != this.selected) {
			this.selected = selected;
			try {
				Optional<EntityFileStorage> selectedStorage = Containers.get().findObjects(EntityFileStorage.class)
						.stream().filter(efs -> efs.getId().equals(selected)).findFirst();
				if (selectedStorage.isPresent()) {
					ListModelList model = (ListModelList) getModel();
					model.addToSelection(selectedStorage.get());
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}
