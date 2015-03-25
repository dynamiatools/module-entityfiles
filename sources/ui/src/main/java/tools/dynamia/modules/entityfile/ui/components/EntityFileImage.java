package tools.dynamia.modules.entityfile.ui.components;

import java.io.IOException;

import org.zkoss.image.AImage;
import org.zkoss.zul.Image;

import tools.dynamia.io.IOUtils;
import tools.dynamia.io.Resource;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;

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
	private String noPhotoPath = "classpath:/web/tools/images/no-photo.jpg";

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
		} else {
			try {
				if (noPhotoPath != null) {
					Resource photoResource = IOUtils.getResource(noPhotoPath);
					setContent(new AImage(photoResource.getFilename(), photoResource.getInputStream()));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
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

	public String getNoPhotoPath() {
		return noPhotoPath;
	}

	public void setNoPhotoPath(String noPhotoPath) {
		this.noPhotoPath = noPhotoPath;
		setValue(getValue());
	}

}
