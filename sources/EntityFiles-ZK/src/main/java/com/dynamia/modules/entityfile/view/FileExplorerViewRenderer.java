package com.dynamia.modules.entityfile.view;

import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;

import com.dynamia.tools.domain.AbstractEntity;
import com.dynamia.tools.viewers.View;
import com.dynamia.tools.viewers.ViewDescriptor;
import com.dynamia.tools.viewers.ViewRenderer;
import com.dynamia.tools.web.ui.RootTreeNode;

@SuppressWarnings("rawtypes")
public class FileExplorerViewRenderer<T extends AbstractEntity> implements ViewRenderer<T> {

	@Override
	public View<T> render(ViewDescriptor descriptor, T value) {
		FileExplorerView<T> view = new FileExplorerView<T>();
		view.setViewDescriptor(descriptor);
		view.setValue(value);
		renderHeaders(view.getTree());

		RootTreeNode rootNode = new RootTreeNode("Archivos de " + value.toString(), "folder");
		rootNode.setOpen(true);
		rootNode.setVisible(true);

		FileExplorerController controller = new FileExplorerController(view, rootNode);
		view.setController(controller);
		controller.setTargetEntity(value);
		controller.init();
		return view;
	}

	private void renderHeaders(Tree tree) {
		new Treecols().setParent(tree);

		newcol("Nombre", "3", tree);
		newcol("Descripcion", "5", tree);
		newcol("Autor", "3", tree);
		newcol("Fecha", "2", tree).setAlign("center");
		newcol("Tamaño", "2", tree).setAlign("right");
	}

	private Treecol newcol(String label, String hflex, Tree tree) {
		Treecol col = new Treecol(label);
		col.setParent(tree.getTreecols());
		if (hflex != null) {
			col.setHflex(hflex);
		}
		return col;
	}

}
