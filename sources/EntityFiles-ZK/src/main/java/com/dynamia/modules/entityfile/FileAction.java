/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile;

/**
 *
 * @author programador
 */
import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.modules.entityfile.util.EntityFileUtils;
import com.dynamia.tools.commons.Messages;
import com.dynamia.tools.viewers.zk.ui.Viewer;
import com.dynamia.tools.web.actions.ActionGroup;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.crud.AbstractCrudAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.util.ZKUtil;

/**
 *
 * @author mario
 */
@InstallAction
public class FileAction extends AbstractCrudAction {

    public FileAction() {
        setName(Messages.get(getClass(), "Archivos"));
        setImage("icons:attachment");
        setGroup(ActionGroup.get("CRUD_OTHER"));
        setMenuSupported(true);

    }

    @Override
    public CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ);
    }

    @Override
    public void actionPerformed(CrudActionEvent evt) {
        Object obj = evt.getData();
        EntityFileUtils.showFileExplorer(obj);

    }
}
