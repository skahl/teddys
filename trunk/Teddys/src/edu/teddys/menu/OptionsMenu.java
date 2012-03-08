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
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import edu.teddys.BaseGame;
import edu.teddys.SupportedSettings;
import edu.teddys.input.InputSettings;
import edu.teddys.input.ActionControllerEnum;
import edu.teddys.input.AnalogControllerEnum;
import java.util.Arrays;
/**
 *
 * @author besient
 */
public class OptionsMenu implements ScreenController, RawInputListener {

    private Nifty nifty;
    private Screen screen;
    private Application app;
    
    private Element inputPopup, systemLayer, controlsLayer;
    
    private DropDown<String> resolution_dd, bpp_dd, aa_dd;
    private CheckBox fullscreen_cb, vsync_cb;
    
    
    private ActionControllerEnum selectAction = null;
    private AnalogControllerEnum selectAnalog = null;
    
    private final String labelSuffix = "_LABEL";
    
    private enum SettingLayers {
        CONTROLS,
        SYSTEM
    };
    
    private SettingLayers activeLayer = SettingLayers.SYSTEM;
     
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        inputPopup = nifty.createPopup("INPUT_POPUP");
        systemLayer = nifty.getScreen("OPTIONS_MENU").findElementByName("system_layer");
        controlsLayer = nifty.getScreen("OPTIONS_MENU").findElementByName("controls_layer");        
        
        resolution_dd = screen.findNiftyControl("RESOLUTION_DROPDOWN", DropDown.class);
        bpp_dd = screen.findNiftyControl("BPP_DROPDOWN", DropDown.class);
        aa_dd = screen.findNiftyControl("AA_DROPDOWN", DropDown.class);
        
        resolution_dd.addAllItems(Arrays.asList(SupportedSettings.getResolutions()));
        bpp_dd.addItem("16 bpp");
        bpp_dd.addItem("24 bpp");
        aa_dd.addAllItems(Arrays.asList(SupportedSettings.getAAs()));
        
        fullscreen_cb = screen.findNiftyControl("FULLSCREEN_CHECKBOX", CheckBox.class);
        vsync_cb = screen.findNiftyControl("VSYNC_CHECKBOX", CheckBox.class);
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
        activeLayer = SettingLayers.SYSTEM;
    }
    
    public void showControls() {
      screen.removeLayerElement(systemLayer);
      screen.addLayerElement(controlsLayer);
      activeLayer = SettingLayers.CONTROLS;
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
        switch (activeLayer) {
            case CONTROLS:
                InputSettings.getInstance().saveSettings();
                break;
            case SYSTEM:
                SupportedSettings.verifyAndSaveCurrentSelection(((BaseGame)app).getSettings(),
                        resolution_dd.getSelection(), 
                        bpp_dd.getSelection(), aa_dd.getSelection(), 
                        SupportedSettings.getFrequencies(resolution_dd.getSelection())[0], 
                        fullscreen_cb.isChecked(), vsync_cb.isChecked()); 
       }
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