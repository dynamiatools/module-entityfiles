package com.dynamia.modules.entityfile.view;

import org.springframework.stereotype.Component;

import com.dynamia.tools.viewers.ViewRenderer;
import com.dynamia.tools.viewers.ViewType;

@Component
public class FileExplorerViewType implements ViewType {

	@Override
	public String getName() {
		return "fileExplorer";
	}

	@Override
	public ViewRenderer getViewRenderer() {
		return new FileExplorerViewRenderer();
	}

}
