package tools.dynamia.modules.entityfile.ui.actions;

import org.zkoss.zul.Messagebox;
import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.actions.ReadableOnly;

@InstallAction
public class ViewFileURLAction extends AbstractEntityFileAction implements ReadableOnly {

    public ViewFileURLAction() {
        setName("URL");
        setGroup(ActionGroup.get("FILES"));
        setMenuSupported(true);
    }

    @Override
    public void actionPerformed(EntityFileActionEvent evt) {
        if (evt.getEntityFile() != null) {
            Messagebox.show(evt.getEntityFile().getStoredEntityFile().getUrl());
        }
    }
}
