package com.dynamia.modules.entityfile.ui;

import java.io.IOException;

import org.zkoss.image.AImage;
import org.zkoss.zul.Image;

import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.tools.viewers.zk.BindingComponentIndex;
import com.dynamia.tools.viewers.zk.ComponentAliasIndex;

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

	public EntityFile getValue() {
		return entityFile;
	}

	public void setValue(EntityFile entityFile) {
		this.entityFile = entityFile;
		loadImage();
	}

	private void loadImage() {
		if (entityFile != null) {
			try {
				setTooltiptext(entityFile.getDescription());
				if (isThumbnail()) {
					setContent(new AImage(entityFile.getThumbnail(thumbnailHeight, thumbnailWidth)));
				} else {
					setContent(new AImage(entityFile.getRealFile()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public boolean isThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(boolean thumbnail) {
		this.thumbnail = thumbnail;
		loadImage();
	}

	public int getThumbnailHeight() {
		return thumbnailHeight;
	}

	public void setThumbnailHeight(int thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
		loadImage();
	}

	public int getThumbnailWidth() {
		return thumbnailWidth;
	}

	public void setThumbnailWidth(int thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
		loadImage();
	}

}
