/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile.ui;

import com.dynamia.tools.viewers.zk.BindingComponentIndex;
import com.dynamia.tools.viewers.zk.ComponentAliasIndex;
import com.dynamia.tools.web.ui.EntityPickerBox;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;

/**
 *
 * @author mario
 */
public class DirectoryBox extends Bandbox {

    static {
        BindingComponentIndex.getInstance().put("value", EntityPickerBox.class);
        ComponentAliasIndex.getInstance().add(DirectoryBox.class);
    }

    private DirectoryExplorer explorer;
    private Bandpopup popup;

    public DirectoryBox() {
        this(null);
    }

    public DirectoryBox(String value) throws WrongValueException {
        super(value);
        explorer = new DirectoryExplorer();
        popup = new Bandpopup();
        popup.appendChild(explorer);
        
        popup.setWidth("400px");
        popup.setHeight("300px");
        
        appendChild(popup);

        explorer.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

            @Override
            public void onEvent(Event event) throws Exception {
                setValue(event.getData().toString());
                Events.postEvent(new Event(Events.ON_CHANGE, DirectoryBox.this, getValue()));
            }
        });

    }

    

}