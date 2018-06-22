/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.entityfile.ui.actions;

/**
 *
 * @author programador
 */
import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.Messages;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.modules.entityfile.ui.util.EntityFileUtils;

/**
 *
 * @author Mario Serrano Leones
 */
@InstallAction
public class FileAction extends AbstractCrudAction {

    public FileAction() {
        setName(Messages.get(getClass(), "Archivos"));
        setImage("attachment");
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
