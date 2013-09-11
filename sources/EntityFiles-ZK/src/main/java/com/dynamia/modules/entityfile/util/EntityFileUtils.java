package com.dynamia.modules.entityfile.util;

import org.zkoss.util.media.Media;

import com.dynamia.modules.entityfile.UploadedFileInfo;
import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.tools.viewers.zk.ui.Viewer;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.util.ZKUtil;

public class EntityFileUtils {

    public static UploadedFileInfo build(Media media) {
        UploadedFileInfo info = new UploadedFileInfo(media.getName(), media.getContentType(), media.getStreamData());

        return info;
    }

    public static void showFileExplorer(Object obj) {
        if (obj != null) {

            Viewer viewer = new Viewer("fileExplorer", EntityFile.class, obj);
            ZKUtil.showDialog("Archivos Asociados", viewer, "80%", "70%");

        } else {
            ZKUtil.showMessage("Debe seleccionar un elemento para ver los archivos asociados", MessageType.INFO);
        }
    }

}
