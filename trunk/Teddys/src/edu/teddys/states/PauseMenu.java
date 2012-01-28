/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.states;

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
public class PauseMenu extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    
    private BaseGame game;
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
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public void setEnabled(boolean isActive) {
        
        if(isActive && !this.isEnabled()) {
            // activate
            HUDController.getInstance().addMessage("Enabling Menu...");
            input.setCursorVisible(true);
            if (nifty != null)
                nifty.gotoScreen(MenuTypes.PAUSE_MENU.name());
            else 
                HUDController.getInstance().addMessage("Error: Nifty is null!");
            this.enabled = true;
            
        } else if(!isActive && this.isEnabled()) {
            // deactivate
            
            input.setCursorVisible(false);
            nifty.gotoScreen(MenuTypes.BLANK.name());
            this.enabled = false;
        }
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        game = (BaseGame) app;
        input = app.getInputManager();
    }
    
    public void returnToGame() {
        nifty.gotoScreen(MenuTypes.BLANK.name());
    }
    
    public void showOptionsScreen() {
        
    }
    
    public void disconnect() {
        
    }
    
    public void exit() {
        game.stop();
    }
    
}