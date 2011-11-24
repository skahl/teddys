
package edu.teddys.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import edu.teddys.BaseGame;
import edu.teddys.controls.MappingEnum;

/**
 *
 * @author skahl
 */
public class Menu extends AbstractAppState {
    
    private boolean enabled;
    private BaseGame app;
    private InputManager inputManager;
    
    
    // ActionListener
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if(name.equals(MappingEnum.GAME_START.name()) && !keyPressed) {
                app.menuFeedback("Start Game");
            }
        }
    };
    
    
    @Override
    public void update(float tpf) {
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public void setEnabled(boolean isActive) {
        if(isActive && !this.isEnabled()) {
            // activate
            this.initKeys(true);
            
            this.enabled = true;
            
        } else if(!isActive && this.isEnabled()) {
            // deactivate
            this.initKeys(false);
            
            this.enabled = false;
        }
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (BaseGame)app;
        this.inputManager = this.app.getInputManager();
        enabled = false;
        
        //initKeys(true);
        // TODO: init menu background and other menu objects
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        
        this.setEnabled(false);
    }
    
    public void initKeys(boolean attach) {
        
        if(attach) {
            // add key mappings
            inputManager.addMapping(MappingEnum.GAME_START.name(), new KeyTrigger(KeyInput.KEY_RETURN));

            // add the action listener
            inputManager.addListener(actionListener, new String[]{MappingEnum.GAME_START.name()});
           
        } else {
            inputManager.deleteMapping(MappingEnum.GAME_START.name());
        }
        
    }
}
