/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.menu;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;

/**
 *
 * @author besient
 */
public class MenuController {
    
    public static enum MENU_TYPES {
        MAIN_MENU,
        PAUSE_MENU,
        JOIN_GAME,
        CREATE_GAME,
        CREDITS,
        BLANK
    };
    
    private static MenuController instance = null;
    
    private NiftyJmeDisplay niftyDisplay;
    private Nifty nifty;
    
    private boolean initialized = false;
    
    private MenuController() {}
    
    public void init(AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer, ViewPort guiViewPort) {
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        
        nifty.addXml("Interface/GUI/MainMenuScreen.xml");
        nifty.addXml("Interface/GUI/PauseScreen.xml");
        nifty.addXml("Interface/GUI/BlankScreen.xml");
        
        initialized = true;
    }
    
    public static MenuController getInstance() {
        if (instance == null)
            instance = new MenuController();
        return instance;
    }
    
    public void showMenu(MENU_TYPES type) {
        if (initialized) {
            nifty.gotoScreen(type.name());
        }
    }
    
    public void hideMenu() {
        if (initialized) {
            nifty.gotoScreen(MENU_TYPES.BLANK.name());
        }
    }
    
}