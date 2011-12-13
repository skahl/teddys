/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.hud;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import edu.teddys.network.AttributeListener;

/**
 *
 * @author besient
 */
public class HUDController {

    private Nifty nifty;
    private Screen screen;
    
    private static List<String> messages;
    
    private static final int numMessages = 5;
    private static Element[] messageElements;
    private Element healthElement;
    private Element currentItemElement;
    private Element currentWeaponElement;
    
    private static int messagesReceived = 0;
    
    private static boolean isInitialized = false;
    
    private AttributeListener healthListener, currentItemListener, currentWeaponListener;

    private static HUDController instance = null;
    
    
    private HUDController() {}


    public static HUDController getInstance() {
        if(instance == null)
            instance = new HUDController();
        return instance;
    }
    
    public void init(Nifty nifty) {
        this.nifty = nifty;
        screen = nifty.getCurrentScreen();
        
        messages = new ArrayList<String>(numMessages);
        messageElements = new Element[numMessages];
        for (int i = 0; i < numMessages; i++) {
            messageElements[i] = nifty.getCurrentScreen().findElementByName("message" + Integer.toString(i));
        }
        
        healthElement = nifty.getCurrentScreen().findElementByName("healthLabel");
        
        healthListener = new AttributeListener<Integer>() {
            public void attributeChanged(Integer value) {
                healthElement.getRenderer(TextRenderer.class).setText(value.toString());
            }
        };
        
        currentItemListener = new AttributeListener() {

            public void attributeChanged(Object value) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        
        currentWeaponListener = new AttributeListener() {

            public void attributeChanged(Object value) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        
        isInitialized = true;
    }
    public void addMessage(String message) {
        
        if (isInitialized) {
        
            messages.add(0, message);
        
            int j = 0;
        
            if (++messagesReceived < numMessages)
                j = numMessages - messagesReceived;
        
            Iterator it = messages.iterator();
            for (int i = j; i < numMessages; i++) {
                messageElements[i].getRenderer(TextRenderer.class).setText((String) it.next());
            }
        }
    }    
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.hud;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import edu.teddys.network.AttributeListener;

/**
 *
 * @author besient
 */
public class HUDController {

    private Nifty nifty;
    private Screen screen;
    
    private static List<String> messages;
    
    private static final int numMessages = 5;
    private static Element[] messageElements;
    private Element healthElement;
    private Element currentItemElement;
    private Element currentWeaponElement;
    
    private static int messagesReceived = 0;
    
    private static boolean isInitialized = false;
    
    private AttributeListener healthListener, currentItemListener, currentWeaponListener;

    private static HUDController instance = null;
    
    
    private HUDController() {}


    public static HUDController getInstance() {
        if(instance == null)
            instance = new HUDController();
        return instance;
    }
    
    public void init(Nifty nifty) {
        this.nifty = nifty;
        screen = nifty.getCurrentScreen();
        
        messages = new ArrayList<String>(numMessages);
        messageElements = new Element[numMessages];
        for (int i = 0; i < numMessages; i++) {
            messageElements[i] = nifty.getCurrentScreen().findElementByName("message" + Integer.toString(i));
        }
        
        healthElement = nifty.getCurrentScreen().findElementByName("healthLabel");
        
        healthListener = new AttributeListener<Integer>() {
            public void attributeChanged(Integer value) {
                healthElement.getRenderer(TextRenderer.class).setText(value.toString());
            }
        };
        
        currentItemListener = new AttributeListener() {

            public void attributeChanged(Object value) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        
        currentWeaponListener = new AttributeListener() {

            public void attributeChanged(Object value) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        
        isInitialized = true;
    }
    public void addMessage(String message) {
        
        if (isInitialized) {
        
            messages.add(0, message);
        
            int j = 0;
        
            if (++messagesReceived < numMessages)
                j = numMessages - messagesReceived;
        
            Iterator it = messages.iterator();
            for (int i = j; i < numMessages; i++) {
                messageElements[i].getRenderer(TextRenderer.class).setText((String) it.next());
            }
        }
    }    
    
    
}

