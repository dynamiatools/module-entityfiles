package com.dynamia.modules.entityfile.util;

import org.zkoss.util.media.Media;

import com.dynamia.modules.entityfile.UploadedFileInfo;

public class EntityFileUtils {

	public static UploadedFileInfo build(Media media) {
		UploadedFileInfo info = new UploadedFileInfo(media.getName(), media.getContentType(), media.getStreamData());

		return info;

	}

}
