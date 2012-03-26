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
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author besient
 */
public class MessagePopupController implements ScreenController {

    protected Nifty nifty;
    protected Screen screen;
    private Element messagePopup;
    private Element message;
    
    private List<String> messageQueue;
    private boolean showing = false;
    
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        
        messageQueue = new LinkedList<String>();
        
    }
    
        public void addMessage(String text) {
            messageQueue.add(text);
            if (!showing) showNextMessage();
        }
    
        private void showNextMessage() {
//        Element p = nifty.createPopupWithId(PopupTypes.MESSAGE_POPUP.name(), PopupTypes.MESSAGE_POPUP.name());
//        Element e = p.findElementByName("text");
//        
//        e.getRenderer(TextRenderer.class).setText(text);
//        nifty.showPopup(nifty.getCurrentScreen(), p.getId(), null); 
        if (showing) {
            //String text = messageQueue.remove(0);
            message.getRenderer(TextRenderer.class).setText(messageQueue.get(0));
        } else {
            messagePopup = nifty.findPopupByName(PopupTypes.MESSAGE_POPUP.name());    
            if (messagePopup == null)
                messagePopup = nifty.createPopupWithId(PopupTypes.MESSAGE_POPUP.name(), PopupTypes.MESSAGE_POPUP.name());
            message = messagePopup.findElementByName("text");
            message.getRenderer(TextRenderer.class).setText(messageQueue.get(0));
            nifty.showPopup(nifty.getCurrentScreen(), messagePopup.getId(), null);
            showing = true;
        }
        

    }
    
    public void closeMessagePopup() {
        Element p = nifty.findPopupByName("MESSAGE_POPUP");

        
        messageQueue.remove(0);
        
        if (!messageQueue.isEmpty()) {
            showNextMessage();
        } else {
            if(nifty.findPopupByName("MESSAGE_POPUP") != null)
            nifty.closePopup("MESSAGE_POPUP");
            showing = false;            
        }
    }

    public void onStartScreen() {
        
    }

    public void onEndScreen() {
        
    }
    
}
