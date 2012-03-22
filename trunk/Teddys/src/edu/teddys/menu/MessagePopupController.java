/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.menu;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author besient
 */
public class MessagePopupController implements ScreenController {

    protected Nifty nifty;
    protected Screen screen;
    private Element messagePopup;
    private Element message;
    
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }
    
        public void showMessagePopup(String text) {
//        Element p = nifty.createPopupWithId(PopupTypes.MESSAGE_POPUP.name(), PopupTypes.MESSAGE_POPUP.name());
//        Element e = p.findElementByName("text");
//        
//        e.getRenderer(TextRenderer.class).setText(text);
//        nifty.showPopup(nifty.getCurrentScreen(), p.getId(), null); 
        
        messagePopup = nifty.createPopupWithId(PopupTypes.MESSAGE_POPUP.name(), PopupTypes.MESSAGE_POPUP.name());
        message = messagePopup.findElementByName("text");
        message.getRenderer(TextRenderer.class).setText(text);
        nifty.showPopup(nifty.getCurrentScreen(), messagePopup.getId(), null);

    }
    
    public void closeMessagePopup() {
        Element p = nifty.findPopupByName("MESSAGE_POPUP");

        if(nifty.findPopupByName("MESSAGE_POPUP") != null)
        nifty.closePopup("MESSAGE_POPUP");

    }

    public void onStartScreen() {
        
    }

    public void onEndScreen() {
        
    }
    
}
