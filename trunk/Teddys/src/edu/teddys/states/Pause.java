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
import edu.teddys.BaseGame;
import edu.teddys.menu.MenuTypes;

/**
 *
 * @author skahl
 */
public class Pause extends AbstractAppState {
    
//    private boolean enabled;
    private BaseGame app;
    private InputManager inputManager;
    
    private Nifty nifty;
    
    
    @Override
    public void update(float tpf) {
        
        // TODO: Pause app state update code
    }
    
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }
    
    @Override
    public void setEnabled(boolean isActive) {
        super.setEnabled(isActive);
        if(isActive) {
            // activate
            
            // TODO: Pause app state enabled code
            
            inputManager.setCursorVisible(true);
            nifty.gotoScreen(MenuTypes.PAUSE_MENU.name());
            
        } else {
            // deactivate

            // TODO: Pause app state disabled code
            
            nifty.gotoScreen(MenuTypes.BLANK.name());
            inputManager.setCursorVisible(false);
        }
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        if (!isInitialized()) {
            super.initialize(stateManager, app);
            this.app = (BaseGame) app;
            super.setEnabled(false);
            this.inputManager = this.app.getInputManager();
            nifty = ((BaseGame) app).getNifty();

            // TODO: Pause app state init code
        }
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        
        // TODO: Pause app state cleanup code
        
        this.setEnabled(false);
    }
}
