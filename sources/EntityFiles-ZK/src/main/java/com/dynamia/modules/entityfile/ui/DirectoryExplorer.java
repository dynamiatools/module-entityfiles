/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile.ui;

import com.dynamia.tools.io.FileInfo;
import com.dynamia.tools.web.ui.ChildrenLoader;
import com.dynamia.tools.web.ui.LazyTreeNode;
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
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

/**
 *
 * @author mario
 */
public class DirectoryExplorer extends Window implements ChildrenLoader<FileInfo>, EventListener<Event> {

    private String value;
    private TreeModel<FileInfo> treeModel;
    private Tree tree;
    private TreeNode rootNode;
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

        rootNode = new DirectoryTreeNode(file);
        loadChildren((LazyTreeNode<FileInfo>) rootNode);
        treeModel = new DefaultTreeModel(rootNode);
        tree.setModel(treeModel);

    }

    private Collection<TreeNode<FileInfo>> getSubdirectories(FileInfo file) {
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

        List<TreeNode<FileInfo>> subdirectories = new ArrayList<TreeNode<FileInfo>>();
        if (subs != null) {
            for (File sub : subs) {
                subdirectories.add(new DirectoryTreeNode(new FileInfo(sub), this));
            }
        }

        Collections.sort(subdirectories, new Comparator<TreeNode<FileInfo>>() {

            @Override
            public int compare(TreeNode<FileInfo> o1, TreeNode<FileInfo> o2) {
                return o1.getData().getName().compareTo(o2.getData().getName());
            }
        });

        return subdirectories;
    }

    @Override
    public void loadChildren(LazyTreeNode<FileInfo> node) {
        for (TreeNode<FileInfo> treeNode : getSubdirectories(node.getData())) {
            node.add(treeNode);
        }
    }

    @Override
    public void onEvent(Event event) throws Exception {
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
