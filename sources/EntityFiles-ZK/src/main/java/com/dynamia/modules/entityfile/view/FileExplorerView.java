package com.dynamia.modules.entityfile.view;

import java.util.Collection;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.North;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.dynamia.modules.entityfile.actions.EntityFileAction;
import com.dynamia.modules.entityfile.actions.EntityFileActionEvent;
import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.tools.domain.AbstractEntity;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.viewers.View;
import com.dynamia.tools.viewers.ViewDescriptor;
import com.dynamia.tools.web.actions.ActionEvent;
import com.dynamia.tools.web.actions.ActionEventBuilder;
import com.dynamia.tools.web.actions.ActionRenderer;
import com.dynamia.tools.web.crud.actions.renderers.MenuitemActionRenderer;
import com.dynamia.tools.web.crud.actions.renderers.ToolbarbuttonActionRenderer;
import com.dynamia.tools.web.ui.EntityTreeNode;

@SuppressWarnings("rawtypes")
public class FileExplorerView<T extends AbstractEntity> extends Window implements View<T>, ActionEventBuilder {

    /**
     *
     */
    private static final long serialVersionUID = -1068942478956221776L;
    private T value;
    private ViewDescriptor viewDescriptor;
    private Tree tree;
    private Toolbar toolbarActions;
    private Menupopup directoriesMenuActions;
    private Menupopup filesMenuActions;
    private Borderlayout layout;
    private FileExplorerController controller;
    private View parentView;

    public FileExplorerView() {
        setWidth("100%");
        setHeight("100%");

        tree = new Tree();
        tree.setVflex(true);
        tree.setWidth("100%");
        tree.setZclass("z-dottree");

        toolbarActions = new Toolbar();
        directoriesMenuActions = new Menupopup();
        directoriesMenuActions.setId("directoriesMenuActions");
        directoriesMenuActions.setParent(this);

        filesMenuActions = new Menupopup();
        filesMenuActions.setId("filesMenuActions");
        filesMenuActions.setParent(this);

        FileExplorerItemRenderer itemRenderer = new FileExplorerItemRenderer();
        itemRenderer.setFilesMenupopup(filesMenuActions);
        itemRenderer.setDirectoriesMenupopup(directoriesMenuActions);
        tree.setItemRenderer(itemRenderer);

        layout = new Borderlayout();
        layout.setWidth("100%");
        layout.setHeight("100%");
        layout.setParent(this);

        North north = new North();
        north.setParent(layout);
        toolbarActions.setParent(north);

        Center center = new Center();
        center.setParent(layout);
        tree.setParent(center);

        renderActions();
    }

    public Tree getTree() {
        return tree;
    }

    public Toolbar getToolbar() {
        return toolbarActions;
    }

    public void setController(FileExplorerController controller) {
        this.controller = controller;
    }

    public FileExplorerController getController() {
        return controller;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public void setViewDescriptor(ViewDescriptor viewDescriptor) {
        this.viewDescriptor = viewDescriptor;

    }

    @Override
    public ViewDescriptor getViewDescriptor() {
        return viewDescriptor;
    }

    private void renderActions() {

        Collection<EntityFileAction> actions = Containers.get().findObjects(EntityFileAction.class);
        MenuitemActionRenderer menuRenderer = new MenuitemActionRenderer();
        if (actions != null && !actions.isEmpty()) {
            for (EntityFileAction entityFileAction : actions) {
                ActionRenderer renderer = entityFileAction.getRenderer();
                if (renderer == null) {
                    renderer = new ToolbarbuttonActionRenderer();
                }
                Component comp = (Component) renderer.render(entityFileAction, this);
                comp.setParent(toolbarActions);

                Menuitem menuitem = (Menuitem) menuRenderer.render(entityFileAction, this);
                if (entityFileAction.getApplicableType() != null) {
                    switch (entityFileAction.getApplicableType()) {
                        case DIRECTORY:
                            menuitem.setParent(directoriesMenuActions);
                            break;
                        case FILE:
                            menuitem.setParent(filesMenuActions);
                            break;
                        default:
                            break;
                    }
                } else {
                    menuitem.setParent(directoriesMenuActions);
                    ((Component) menuitem.clone()).setParent(filesMenuActions);
                }
            }
        }
    }

    @Override
    public ActionEvent buildActionEvent(Object source, Map<String, Object> params) {

        EntityFile data = null;
        if (tree.getSelectedCount() > 0) {
            EntityTreeNode node = (EntityTreeNode) tree.getSelectedItem().getValue();
            data = (EntityFile) node.getEntity();
        }

        return new EntityFileActionEvent(data, value, this, source);
    }

    public View getParentView() {
        return parentView;
    }

    public void setParentView(View parentView) {
        this.parentView = parentView;
    }

}
