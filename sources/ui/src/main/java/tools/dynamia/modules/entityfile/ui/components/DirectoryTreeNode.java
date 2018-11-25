
package tools.dynamia.modules.entityfile.ui.components;

import tools.dynamia.io.FileInfo;
import tools.dynamia.zk.crud.ui.ChildrenLoader;
import tools.dynamia.zk.crud.ui.LazyEntityTreeNode;

/**
 *
 * @author Mario Serrano Leones
 */
public class DirectoryTreeNode extends LazyEntityTreeNode<FileInfo> {

    public DirectoryTreeNode(FileInfo data, ChildrenLoader<FileInfo> loader) {
        super(data, loader);
    }

    public DirectoryTreeNode(FileInfo data) {
        super(data);
    }

}
