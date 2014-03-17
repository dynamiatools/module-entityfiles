/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile.ui;

import com.dynamia.tools.io.FileInfo;
import com.dynamia.tools.web.ui.ChildrenLoader;
import com.dynamia.tools.web.ui.LazyEntityTreeNode;

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
