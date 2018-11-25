

package tools.dynamia.modules.entityfile.ui;

import tools.dynamia.ui.icons.AbstractIconsProvider;
import tools.dynamia.ui.icons.InstallIcons;

/**
 *
 * @author Mario Serrano Leones
 */
@InstallIcons
public class EntityFileIcons extends AbstractIconsProvider{

    @Override
    public String getPrefix() {
        return "entityfiles/icons";
    }

    @Override
    public String getExtension() {
        return "png";
    }
    
}
