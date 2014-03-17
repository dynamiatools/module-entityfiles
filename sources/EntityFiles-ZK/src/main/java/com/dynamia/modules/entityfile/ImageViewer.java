/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile;

import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.tools.web.util.ZKUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zkoss.image.AImage;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Center;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Image;
import org.zkoss.zul.Window;

/**
 *
 * @author ronald
 */
@Component
public class ImageViewer {

    @Autowired
    EntityFileService entityFileService;

    public void view(EntityFile entityFile) {
        Window window = new Window();
        window.setTitle(entityFile.getName());
        window.setClosable(true);
        Caption caption = new Caption();
        caption.setParent(window);
        window.setPage(ZKUtil.getFirstPage());
        window.setWidth("80%");
        window.setHeight("80%");
        window.setVisible(true);
        Borderlayout borderLayout = new Borderlayout();
        borderLayout.setWidth("100%");
        borderLayout.setHeight("100%");
        borderLayout.setParent(window);
        Center center = new Center();
        center.setAutoscroll(true);
        center.setParent(borderLayout);
        center.setStyle("background: gray; vertical-align:middle; horizontal-align:middle");

        if (entityFile.getContentType().startsWith("image")) {
            //************
            Image image = new Image();
            image.setParent(center);
            image.setAlign("center");
            image.setStyle("border: 1px");

            String newFileName = entityFileService.getConfiguration().getRepository() + "/" + entityFile.getId();
            try {
                //Media media = new AImage();
                org.zkoss.image.Image image1 = new AImage(new File(newFileName));
                image.setContent(image1);
                window.doModal();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //****************
        } else {
            Iframe iframe = new Iframe();
            iframe.setParent(center);
            iframe.setWidth("100%");
            iframe.setHeight("100%");

            String newFileName = entityFileService.getConfiguration().getRepository() + "/" + entityFile.getId();
            File file = new File(newFileName);
            try {
                final InputStream mediais = new FileInputStream(file);
                final AMedia amedia = new AMedia(entityFile.getName(), entityFile.getExtension(), entityFile.getContentType(), mediais);
                iframe.setContent(amedia);
                if(entityFile.getContentType().equals("application/pdf")){
                    window.doModal();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            //window.doModal();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
