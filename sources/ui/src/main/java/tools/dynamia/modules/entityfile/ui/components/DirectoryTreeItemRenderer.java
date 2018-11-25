
package tools.dynamia.modules.entityfile.ui.components;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

import tools.dynamia.ui.icons.IconSize;
import tools.dynamia.ui.icons.IconsTheme;

/**
 *
 * @author Mario Serrano Leones
 */
public class DirectoryTreeItemRenderer implements TreeitemRenderer<DirectoryTreeNode>, EventListener<Event> {

    @Override
    public void render(Treeitem item, DirectoryTreeNode data, int index) {

        item.setValue(data);
        item.setLabel(data.getData().getName());
        item.addEventListener(Events.ON_OPEN, this);
        item.addEventListener(Events.ON_CLOSE, this);

        setupIcon(item);
    }

    @Override
    public void onEvent(Event event) {
        Treeitem item = (Treeitem) event.getTarget();
        DirectoryTreeNode node = item.getValue();
        if (item.isOpen()) {
            node.load();
        } else {
            node.getChildren().clear();
        }

        setupIcon(item);
    }

    private void setupIcon(Treeitem item) {

        DirectoryTreeNode node = item.getValue();

        if (item.isOpen()) {
            item.setImage(IconsTheme.get().getIcon("folder-open").getRealPath(IconSize.SMALL));
        } else {
            item.setImage(IconsTheme.get().getIcon("folder").getRealPath(IconSize.SMALL));
        }

        if (!node.getData().getFile().canRead() || !node.getData().getFile().canWrite()) {
            if (item.isOpen()) {
                item.setImage(IconsTheme.get().getIcon("folder-red-open").getRealPath(IconSize.SMALL));
            } else {
                item.setImage(IconsTheme.get().getIcon("folder-red").getRealPath(IconSize.SMALL));
            }
        }
    }

}
