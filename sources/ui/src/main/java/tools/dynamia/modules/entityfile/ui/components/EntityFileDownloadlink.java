package tools.dynamia.modules.entityfile.ui.components;

import java.io.FileInputStream;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Toolbarbutton;

import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;

public class EntityFileDownloadlink extends Toolbarbutton {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2182747459195865750L;

	static {
		BindingComponentIndex.getInstance().put("value", EntityFileDownloadlink.class);
		ComponentAliasIndex.getInstance().put("entityfileDownloadlink", EntityFileDownloadlink.class);
	}

	private EntityFile entityFile;

	public EntityFileDownloadlink() {
		addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (entityFile != null) {
					Filedownload.save(new FileInputStream(entityFile.getRealFile()), null, entityFile.getName());
				}
			}
		});
	}

	public EntityFile getValue() {
		return entityFile;
	}

	public void setValue(EntityFile entityFile) {
		this.entityFile = entityFile;
		setLabel(entityFile.getName());
		setTooltiptext(entityFile.getDescription());
	}

}
