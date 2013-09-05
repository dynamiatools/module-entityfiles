package com.dynamia.modules.entityfile.ui;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;

public class EntiyFileUpload extends Hbox{
	
	private Button explorerButton;
	private Label fileLabel;
	
	public EntiyFileUpload() {
		explorerButton = new Button("Examinar");
		explorerButton.setUpload("true");
		explorerButton.addEventListener(Events.ON_UPLOAD, onUploadListener);
	}
	
	private void fileUploaded(Media media){
		
	}
	
	private EventListener onUploadListener = new EventListener() {
		
		@Override
		public void onEvent(Event event) throws Exception {
			UploadEvent evt = (UploadEvent) event;
			fileUploaded(evt.getMedia());
			
		}
	};

}
