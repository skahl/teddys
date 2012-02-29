/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.menu;

import com.jme3.app.Application;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import edu.teddys.BaseGame;
import edu.teddys.input.InputSettings;
import edu.teddys.input.ActionControllerEnum;
import edu.teddys.input.AnalogControllerEnum;
/**
 *
 * @author besient
 */
public class OptionsMenu implements ScreenController, RawInputListener {

    private Nifty nifty;
    private Application app;
    
    private Element inputPopup;
    
    
    private ActionControllerEnum selectAction = null;
    private AnalogControllerEnum selectAnalog = null;
    
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;        
        inputPopup = nifty.createPopup("INPUT_POPUP");
    }
    
    public void setApplication(Application app) {
        this.app = app;
    }
    
    private void loadKeys(Application app) {
        InputSettings settings = InputSettings.getInstance();
        settings.loadSettings(((BaseGame)app).getSettings());
        
        nifty.getScreen("OPTIONS_MENU").findElementByName("JETPACK_LABEL")
                .getRenderer(TextRenderer.class).setText(settings.getKey(AnalogControllerEnum.JETPACK)[0].getName());
        nifty.getScreen("OPTIONS_MENU").findElementByName("MOVE_LEFT_LABEL")
                .getRenderer(TextRenderer.class).setText(settings.getKey(AnalogControllerEnum.MOVE_LEFT)[0].getName());
        nifty.getScreen("OPTIONS_MENU").findElementByName("MOVE_RIGHT_LABEL")
                .getRenderer(TextRenderer.class).setText(settings.getKey(AnalogControllerEnum.MOVE_RIGHT)[0].getName());
        nifty.getScreen("OPTIONS_MENU").findElementByName("WEAPON_LABEL")
                .getRenderer(TextRenderer.class).setText(settings.getKey(AnalogControllerEnum.WEAPON)[0].getName());
        }

    public void onStartScreen() {
        loadKeys(app);
    }    
        
    public void onEndScreen() {
        
    }
    
    public void back() {
        nifty.gotoScreen(MenuTypes.MAIN_MENU.name());
    }
    
    public void save() {
        InputSettings.getInstance().saveSettings(((BaseGame)app).getSettings());
    }

    
    public void activateInputSelection(String key) {
        try {
            selectAction = ActionControllerEnum.valueOf(key);
            selectAnalog = null;
        } catch (IllegalArgumentException e1) {
            try {
                selectAnalog = AnalogControllerEnum.valueOf(key);
                selectAction = null;
            } catch(IllegalArgumentException e2) {
                
            }
        }
        
        app.getInputManager().addRawInputListener(this);
        nifty.showPopup(nifty.getCurrentScreen(), inputPopup.getId(), null);
    }
    
    public void onKeyEvent(KeyInputEvent evt) {
        if (selectAction != null) {
            InputSettings.getInstance().setKey(selectAction, evt.getKeyCode());
            Element niftyElement = nifty.getCurrentScreen().findElementByName(selectAction.name().concat("_LABEL"));
            niftyElement.getRenderer(TextRenderer.class).setText(String.valueOf(evt.getKeyChar()));
        } else if (selectAnalog != null) {
            InputSettings.getInstance().setKey(selectAnalog, evt.getKeyCode());
            Element niftyElement = nifty.getCurrentScreen().findElementByName(selectAnalog.name().concat("_LABEL"));
            niftyElement.getRenderer(TextRenderer.class).setText(String.valueOf(evt.getKeyChar()));
        } else {
            
        }       
        evt.setConsumed();
        app.getInputManager().removeRawInputListener(this);
        nifty.closePopup(inputPopup.getId());
            
    }
    
    public void onMouseButtonEvent(MouseButtonEvent evt) {
        if (evt.isPressed()) {
            if (selectAction != null) {
                InputSettings.getInstance().setMouseButton(selectAction, evt.getButtonIndex());
                Element niftyElement = nifty.getCurrentScreen().findElementByName(selectAction.name().concat("_LABEL"));
                niftyElement.getRenderer(TextRenderer.class).setText("MOUSE_"+evt.getButtonIndex());
            } else if (selectAnalog != null) {
                InputSettings.getInstance().setMouseButton(selectAnalog, evt.getButtonIndex());
                Element niftyElement = nifty.getCurrentScreen().findElementByName(selectAnalog.name().concat("_LABEL"));
                niftyElement.getRenderer(TextRenderer.class).setText("MOUSE_"+evt.getButtonIndex());
            } else {
            
            }
            evt.setConsumed();
            app.getInputManager().removeRawInputListener(this);
            nifty.closePopup(inputPopup.getId());
        }
    }

    public void beginInput() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void endInput() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onJoyAxisEvent(JoyAxisEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onJoyButtonEvent(JoyButtonEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onMouseMotionEvent(MouseMotionEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onTouchEvent(TouchEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }   
}
