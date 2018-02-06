/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.entityfile.ui.components;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import tools.dynamia.io.FileInfo;
import tools.dynamia.zk.crud.ui.ChildrenLoader;
import tools.dynamia.zk.crud.ui.EntityTreeModel;
import tools.dynamia.zk.crud.ui.EntityTreeNode;
import tools.dynamia.zk.crud.ui.LazyEntityTreeNode;

/**
 *
 * @author mario
 */
public class DirectoryExplorer extends Window implements ChildrenLoader<FileInfo>, EventListener<Event> {

    private String value;
    private EntityTreeModel<FileInfo> treeModel;
    private Tree tree;
    private EntityTreeNode<FileInfo> rootNode;
    private boolean showHiddenFolders;

    public DirectoryExplorer() {
        init();
    }

    public void reset() {
        initModel();
    }

    private void init() {
        tree = new Tree();
        tree.setHflex("1");
        tree.setVflex("1");
        tree.addEventListener(Events.ON_CLICK, this);
        tree.setItemRenderer(new DirectoryTreeItemRenderer());
        appendChild(tree);

        setVflex("1");
        setHflex("1");

        initModel();
    }

    private void initModel() {
        FileInfo file = new FileInfo(new File("/"));

        rootNode = new EntityTreeNode<FileInfo>(file);
        treeModel = new EntityTreeModel<FileInfo>(rootNode);
        for (EntityTreeNode<FileInfo> entityTreeNode : getSubdirectories(file)) {
            rootNode.addChild(entityTreeNode);
        }
        tree.setModel(treeModel);

    }

    private Collection<EntityTreeNode<FileInfo>> getSubdirectories(FileInfo file) {
        File[] subs = file.getFile().listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    if (!isShowHiddenFolders()) {
                        return !pathname.isHidden() && !pathname.getName().startsWith(".");
                    }
                    return true;
                }
                return false;
            }
        });

        List<EntityTreeNode<FileInfo>> subdirectories = new ArrayList<EntityTreeNode<FileInfo>>();
        if (subs != null) {
            for (File sub : subs) {
                subdirectories.add(new DirectoryTreeNode(new FileInfo(sub), this));
            }
        }

        Collections.sort(subdirectories, new Comparator<EntityTreeNode<FileInfo>>() {

            @Override
            public int compare(EntityTreeNode<FileInfo> o1, EntityTreeNode<FileInfo> o2) {
                return o1.getData().getName().compareTo(o2.getData().getName());
            }
        });

        return subdirectories;
    }

    @Override
    public void loadChildren(LazyEntityTreeNode<FileInfo> node) {
        for (EntityTreeNode<FileInfo> treeNode : getSubdirectories(node.getData())) {
            node.addChild(treeNode);
        }
    }

    @Override
    public void onEvent(Event event) {
        Treeitem item = tree.getSelectedItem();
        if (item != null) {
            DirectoryTreeNode node = item.getValue();
            setValue(node.getData().getFile().getAbsolutePath());
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        Events.postEvent(new Event(Events.ON_CHANGE, this, value));
    }

    public boolean isShowHiddenFolders() {
        return showHiddenFolders;
    }

    public void setShowHiddenFolders(boolean showHiddenFolders) {
        this.showHiddenFolders = showHiddenFolders;
    }

}
