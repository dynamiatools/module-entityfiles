
package tools.dynamia.modules.entityfile.ui.components;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;

import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;

/**
 *
 * @author Mario Serrano Leones
 */
public class DirectoryBox extends Bandbox {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7769832324226733919L;

	static {
        BindingComponentIndex.getInstance().put("value", DirectoryBox.class);
        ComponentAliasIndex.getInstance().add(DirectoryBox.class);
    }

    private DirectoryExplorer explorer;
    private Bandpopup popup;

    public DirectoryBox() {
        this(null);
    }

    public DirectoryBox(String value) throws WrongValueException {
        super(value);
        explorer = new DirectoryExplorer();
        popup = new Bandpopup();
        popup.appendChild(explorer);
        
        popup.setWidth("400px");
        popup.setHeight("300px");
        
        appendChild(popup);

        explorer.addEventListener(Events.ON_CHANGE, event -> {
		    setValue(event.getData().toString());
		    Events.postEvent(new Event(Events.ON_CHANGE, DirectoryBox.this, getValue()));
		});

    }

    

}
