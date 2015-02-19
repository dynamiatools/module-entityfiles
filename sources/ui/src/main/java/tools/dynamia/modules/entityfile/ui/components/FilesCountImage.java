package tools.dynamia.modules.entityfile.ui.components;

import org.zkoss.zul.A;

import tools.dynamia.ui.icons.IconSize;
import tools.dynamia.ui.icons.IconsTheme;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;

public class FilesCountImage extends A {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IconSize iconSize = IconSize.SMALL;
	private String icon = "attachment";
	private int value;

	static {
		BindingComponentIndex.getInstance().put("value", FilesCountImage.class);
		ComponentAliasIndex.getInstance().add("filescountimage", FilesCountImage.class);
	}

	public FilesCountImage() {

	}

	public FilesCountImage(int value) {
		this.value = value;
	}

	public void setValue(int value) {
		try {
			int count = value;
			if (count > 0) {
				setImage(IconsTheme.get().getIcon(icon).getRealPath(iconSize));
				setTooltiptext(count + " archivos adjuntos");
			} else {
				setImage(null);
				setTooltiptext(null);
			}
			setLabel("");
		} catch (Exception e) {
			setLabel("");
			setImage(null);
		}
	}

	public int getValue() {
		return value;
	}

	public IconSize getIconSize() {
		return iconSize;
	}

	public void setIconSize(IconSize iconSize) {
		this.iconSize = iconSize;
	}

	public void setIconSize(String iconSize) {
		this.iconSize = IconSize.valueOf(iconSize.toUpperCase());
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
