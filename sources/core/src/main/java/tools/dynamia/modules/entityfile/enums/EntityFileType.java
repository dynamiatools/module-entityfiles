/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.entityfile.enums;

/**
 *
 * @author djinn
 */
public enum EntityFileType {

	DIRECTORY, FILE, IMAGE;

	public static EntityFileType getFileType(String extension) {
		final String IMAGE_EXTENSIONS = "png,jpg,gif,jpeg,tiff,svg";
		if (IMAGE_EXTENSIONS.contains(extension)) {
			return EntityFileType.IMAGE;
		} else {
			return EntityFileType.FILE;
		}
	}
}
