package tools.dynamia.modules.entityfile.ui.components;

/*-
 * #%L
 * Dynamia Modules - EntityFiles - UI
 * %%
 * Copyright (C) 2016 - 2019 Dynamia Soluciones IT SAS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Image;

import tools.dynamia.io.IOUtils;
import tools.dynamia.io.Resource;
import tools.dynamia.modules.entityfile.StoredEntityFile;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;
import tools.dynamia.zk.ImageCache;

public class EntityFileImage extends Image {

    /**
     *
     */
    private static final long serialVersionUID = -2182747459195865750L;

    static {
        BindingComponentIndex.getInstance().put("value", EntityFileImage.class);
        ComponentAliasIndex.getInstance().put("entityfileImage", EntityFileImage.class);
    }

    private EntityFile entityFile;
    private boolean thumbnail = false;
    private int thumbnailHeight = 64;
    private int thumbnailWidth = 64;
    private String noPhotoPath = "/zkau/web/tools/images/no-photo.jpg";

    public EntityFile getValue() {
        return entityFile;
    }

    public void setValue(EntityFile entityFile) {
        this.entityFile = entityFile;
        loadImage();
    }

    private void loadImage() {
        if (entityFile != null) {

            StoredEntityFile sef = entityFile.getStoredEntityFile();
            setTooltiptext(entityFile.getDescription());
            if (isThumbnail()) {
                setSrc(sef.getThumbnailUrl(thumbnailWidth, thumbnailHeight));
            } else {
                setSrc(sef.getUrl());
            }
        } else {
            try {
                if (noPhotoPath != null) {
                    if (noPhotoPath.startsWith("classpath")) {
                        AImage imageContent = ImageCache.get(noPhotoPath);
                        if (imageContent == null) {
                            Resource resource = IOUtils.getResource(noPhotoPath);
                            if (resource.exists()) {
                                imageContent = new AImage(resource.getFilename(), resource.getInputStream());
                                ImageCache.add(noPhotoPath, imageContent);
                            }
                        }

                        if (imageContent != null) {
                            setContent(imageContent);
                        }

                    } else {
                        setSrc(noPhotoPath);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (isThumbnail()) {
            setStyle("max-height: " + thumbnailHeight + "px; max-width: " + thumbnailWidth + "px");
        } else {
            setStyle(null);
        }
    }

    public boolean isThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(boolean thumbnail) {
        this.thumbnail = thumbnail;

    }

    public int getThumbnailHeight() {
        return thumbnailHeight;
    }

    public void setThumbnailHeight(int thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;

    }

    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;

    }

    public String getNoPhotoPath() {
        return noPhotoPath;
    }

    public void setNoPhotoPath(String noPhotoPath) {
        this.noPhotoPath = noPhotoPath;

    }

    @Override
    public void setParent(Component parent) {
        super.setParent(parent);
        loadImage();
    }

}
