/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.menu;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import edu.teddys.BaseGame;
import edu.teddys.hud.HUDController;
import edu.teddys.menu.MenuTypes;

/**
 *
 * @author besient
 */
public class PauseMenu implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    
    private Application app;
    
    private InputManager input;
    
    private boolean enabled;
    
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() {
        
    }

    public void onEndScreen() {
        
    }

    public void setApplication(Application app) {
        this.app = app;
    }
    
    public void returnToGame() {
        nifty.gotoScreen(MenuTypes.BLANK.name());
    }
    
    public void showOptionsScreen() {
        
    }
    
    public void disconnect() {
        
    }
    
    public void exit() {
        app.stop();
    }
    
}