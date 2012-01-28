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
import edu.teddys.menu.MenuTypes;
import edu.teddys.states.AppStateSwitcher;

/**
 *
 * @author besient
 */
public class MainMenu extends AbstractAppState implements ScreenController {

    Nifty nifty;
    Screen screen;
    
    BaseGame game;
    InputManager input;
    
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
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        game = (BaseGame) app;
        input = game.getInputManager();
        
        enabled = false;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public void setEnabled(boolean isActive) {
        if (isActive && !this.isEnabled()) {
            
            input.setCursorVisible(true);
            enabled = true;
        } else if (!isActive && this.isEnabled()) {
            
            input.setCursorVisible(false);
            enabled = false;
        }
    }
    
    public void showJoinScreen() {
        nifty.gotoScreen(MenuTypes.BLANK.name());
    }
    
    public void showCreateScreen() {
        nifty.gotoScreen(MenuTypes.CREATE_GAME.name());
    }
    
    public void showOptionsScreen() {
        nifty.gotoScreen(MenuTypes.OPTIONS_MENU.name());
    }
    
    public void showCreditsScreen() {
        nifty.gotoScreen(MenuTypes.CREDITS.name());
    }
    
    public void back() {
        nifty.gotoScreen(MenuTypes.MAIN_MENU.name());
    }
    
    public void exit() {
        game.stop();
    }

    
}