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
import edu.teddys.BaseGame;
import edu.teddys.SupportedSettings;
import edu.teddys.input.InputSettings;
import edu.teddys.input.ActionControllerEnum;
import edu.teddys.input.AnalogControllerEnum;
import java.util.Arrays;
/**
 * ScreenController for the options menu.
 * @author besient
 */
public class OptionsMenu extends MessagePopupController implements RawInputListener {

    private Application app;
    
    private Element inputPopup, unsupported_popup, systemLayer, controlsLayer;
    
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
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public void bind(Nifty nifty, Screen screen) {

        super.bind(nifty, screen);
        
        inputPopup = nifty.createPopup(PopupTypes.INPUT_POPUP.name());
        unsupported_popup = nifty.createPopup("UNSUPPORTED_POPUP");
        systemLayer = nifty.getScreen("OPTIONS_MENU").findElementByName("system_layer");
        controlsLayer = nifty.getScreen("OPTIONS_MENU").findElementByName("controls_layer");    
        
        screen.removeLayerElement(controlsLayer);
        
        resolution_dd = screen.findNiftyControl("RESOLUTION_DROPDOWN", DropDown.class);
        bpp_dd = screen.findNiftyControl("BPP_DROPDOWN", DropDown.class);
        aa_dd = screen.findNiftyControl("AA_DROPDOWN", DropDown.class);
        
        resolution_dd.addAllItems(Arrays.asList(SupportedSettings.getResolutions()));
        bpp_dd.addItem("16 bpp");
        bpp_dd.addItem("24 bpp");
        aa_dd.addAllItems(Arrays.asList(SupportedSettings.getAAs()));
        
        fullscreen_cb = screen.findNiftyControl("FULLSCREEN_CHECKBOX", CheckBox.class);
        vsync_cb = screen.findNiftyControl("VSYNC_CHECKBOX", CheckBox.class);
        
        loadKeys();
    }
    
    /**
     * Setter method for the application object.
     * @param app 
     */
    public void setApplication(Application app) {
        this.app = app;
    }

    /**
     * Updates the resolution list depending on whether fullscreen is selected or not.
     */
    public void updateResolutions() {
        if (fullscreen_cb.isChecked()) {
            resolution_dd.addAllItems(Arrays.asList(SupportedSettings.getWindowedResolutions()));
        } else {
            resolution_dd.addAllItems(Arrays.asList(SupportedSettings.getResolutions()));
        }
    }
    
    /**
     * Displays the current key bindings.
     */
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
        nifty.getScreen("OPTIONS_MENU").findElementByName("NEXT_WEAPON_LABEL")
                .getRenderer(TextRenderer.class).setText(settings.getName(ActionControllerEnum.NEXT_WEAPON));
        nifty.getScreen("OPTIONS_MENU").findElementByName("PREVIOUS_WEAPON_LABEL")
                .getRenderer(TextRenderer.class).setText(settings.getName(ActionControllerEnum.PREVIOUS_WEAPON));
        
        
        }

    
    /**
     * Return to the main menu.
     */
    public void back() {
        nifty.gotoScreen(MenuTypes.MAIN_MENU.name());
    }
    
    /**
     * Show the system page.
     */
    public void showSystem() {
        if (activeLayer != SettingLayers.SYSTEM) {
            screen.removeLayerElement(controlsLayer);
            screen.addLayerElement(systemLayer);
            activeLayer = SettingLayers.SYSTEM;
        }
    }
    
    /**
     * Show the controls page
     */
    public void showControls() {
        if (activeLayer != SettingLayers.CONTROLS) {
            screen.removeLayerElement(systemLayer);
            screen.addLayerElement(controlsLayer);
            activeLayer = SettingLayers.CONTROLS;
        }
    }

    /**
     * Ask the user to press a key and attach the input listener.
     * @param key 
     */
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
    
    /**
     * Saves the current settings.
     */
    public void save() {
        switch (activeLayer) {
            case CONTROLS:
                InputSettings.getInstance().saveSettings();
                break;
            case SYSTEM:
                boolean valid = SupportedSettings.verifyAndSaveCurrentSelection(((BaseGame)app).getSettings(),
                        resolution_dd.getSelection(), 
                        bpp_dd.getSelection(), aa_dd.getSelection(), 
                        SupportedSettings.getFrequencies(resolution_dd.getSelection())[0], 
                        fullscreen_cb.isChecked(), vsync_cb.isChecked());
                if (!valid) nifty.showPopup(nifty.getCurrentScreen(), unsupported_popup.getId(), null);
       }
    }
    
    /**
     * Process the user's key selection and close the popup. 
     */
    public void onKeyEvent(KeyInputEvent evt) {
        String keyChar = String.valueOf(evt.getKeyChar());
        if (keyChar.equals(" ")) {
            keyChar = String.valueOf(evt.getKeyCode());            
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
    
    /**
     * Process the user's mouse button selection and close the popup. 
     */
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
    
    /**
     * Process the user's mouse wheel selection and close the popup.
     */
    public void onMouseMotionEvent(MouseMotionEvent evt) {
        int delta = evt.getDeltaWheel();
        if (delta != 0 && (selectAction != null)) {
            if (delta < 0)
                InputSettings.getInstance().setMouseWheel(selectAction, false);
            else 
                InputSettings.getInstance().setMouseWheel(selectAction, true);
            
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

    public void onTouchEvent(TouchEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }   
}