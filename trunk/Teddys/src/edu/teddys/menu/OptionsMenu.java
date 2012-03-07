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
    private Screen screen;
    private Application app;
    
    private Element inputPopup, systemLayer, controlsLayer;
    
    
    private ActionControllerEnum selectAction = null;
    private AnalogControllerEnum selectAnalog = null;
    
    private final String labelSuffix = "_LABEL";
    
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        inputPopup = nifty.createPopup("INPUT_POPUP");
        systemLayer = nifty.getScreen("OPTIONS_MENU").findElementByName("system_layer");
        controlsLayer = nifty.getScreen("OPTIONS_MENU").findElementByName("controls_layer");
    }
    
    public void setApplication(Application app) {
        this.app = app;
    }
    
    private void loadKeys() {
        InputSettings settings = InputSettings.getInstance();
        
        nifty.getScreen("OPTIONS_MENU").findElementByName("JETPACK_LABEL")
                .getRenderer(TextRenderer.class).setText(settings.getName(AnalogControllerEnum.JETPACK));
        nifty.getScreen("OPTIONS_MENU").findElementByName("MOVE_LEFT_LABEL")
                .getRenderer(TextRenderer.class).setText(settings.getName(AnalogControllerEnum.MOVE_LEFT));
        nifty.getScreen("OPTIONS_MENU").findElementByName("MOVE_RIGHT_LABEL")
                .getRenderer(TextRenderer.class).setText(settings.getName(AnalogControllerEnum.MOVE_RIGHT));
        nifty.getScreen("OPTIONS_MENU").findElementByName("WEAPON_LABEL")
                .getRenderer(TextRenderer.class).setText(settings.getName(AnalogControllerEnum.WEAPON));
        }

    public void onStartScreen() {
        loadKeys();
    }    
        
    public void onEndScreen() {
        
    }
    
    public void back() {
        nifty.gotoScreen(MenuTypes.MAIN_MENU.name());
    }
    
    public void showSystem() {
        screen.removeLayerElement(controlsLayer);
        screen.addLayerElement(systemLayer);
    }
    
    public void showControls() {
      screen.removeLayerElement(systemLayer);
      screen.addLayerElement(controlsLayer); 
    }

    public void activateInputSelection(String key) {
        try {
            selectAnalog = AnalogControllerEnum.valueOf(key);
            selectAction = null; 
        } catch (IllegalArgumentException e1) {
            try {
                selectAction = ActionControllerEnum.valueOf(key);
                selectAnalog = null; 
            } catch(IllegalArgumentException e2) {
                
            }
        }
        
        app.getInputManager().addRawInputListener(this);
        nifty.showPopup(nifty.getCurrentScreen(), inputPopup.getId(), null);
    }
    
    public void save() {
        InputSettings.getInstance().saveSettings();
    }
    
    public void onKeyEvent(KeyInputEvent evt) {
        String keyChar;
        if (evt.getKeyChar() == ' ') {
            keyChar = String.valueOf(evt.getKeyCode());
        } else {
            keyChar = String.valueOf(evt.getKeyChar());
        }
        if (selectAnalog != null) {
            InputSettings.getInstance().setKey(selectAnalog, evt.getKeyCode(), keyChar);
        } else if (selectAction != null) {
            InputSettings.getInstance().setKey(selectAction, evt.getKeyCode(), keyChar);
        } else {
            
        }       
        evt.setConsumed();
        app.getInputManager().removeRawInputListener(this);
        nifty.closePopup(inputPopup.getId());
        loadKeys();    
    }
    
    public void onMouseButtonEvent(MouseButtonEvent evt) {
        if (evt.isPressed()) {
            if (selectAction != null) {
                InputSettings.getInstance().setMouseButton(selectAction, evt.getButtonIndex());
            } else if (selectAnalog != null) {
                InputSettings.getInstance().setMouseButton(selectAnalog, evt.getButtonIndex());
            } else {
            
            }
            evt.setConsumed();
            app.getInputManager().removeRawInputListener(this);
            nifty.closePopup(inputPopup.getId());
            loadKeys();
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