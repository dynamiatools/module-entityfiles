/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfiles.photo;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.dynamia.tools.domain.AbstractEntity;
import com.dynamia.tools.web.util.ZKUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.zul.Include;
import org.zkoss.zul.Window;

/**
 *
 * @author ronald
 */
public class PhotoUtil extends ZKUtil {

    public static Object openDialog(String uri, String title, Object data, String height, String width) {
        return openDialog(uri, title, data, height, width, null);
    }
    public static Object openDialog(String uri, String title, Object data, String height, String width, HashMap<String,Object> params) {
        Object retorno = null;
        try {
            final Window dialog = createWindow(uri, title, data, retorno,params);

            if (height != null) {
                dialog.setHeight(height);
            }

            if (width != null) {
                dialog.setWidth(width);
            }
            dialog.doModal();

        } catch (Exception ex) {
            Logger.getLogger(ZKUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public static Window createWindow(String uri, String title, Object data, Object retorno, HashMap<String,Object> params) {

        final Window dialog = new Window(title, "normal", true);

        if (uri != null) {
            Include inc = new Include(uri);
            inc.setParent(dialog);
            inc.setDynamicProperty("dialog", dialog);
            inc.setDynamicProperty("data", data);
            inc.setDynamicProperty("retorno", retorno);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                inc.setDynamicProperty(entry.getKey(), entry.getValue());
            }
            if (data instanceof AbstractEntity) {
                inc.setDynamicProperty("entity", data);
            }
        }
        dialog.setPage(getFirstPage());
        return dialog;
    }
}
