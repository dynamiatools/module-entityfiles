/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.entityfile.ui.components;

import tools.dynamia.io.FileInfo;
import tools.dynamia.zk.crud.ui.ChildrenLoader;
import tools.dynamia.zk.crud.ui.LazyEntityTreeNode;

/**
 *
 * @author mario
 */
public class DirectoryTreeNode extends LazyEntityTreeNode<FileInfo> {

    public DirectoryTreeNode(FileInfo data, ChildrenLoader<FileInfo> loader) {
        super(data, loader);
    }

    public DirectoryTreeNode(FileInfo data) {
        super(data);
    }

}
