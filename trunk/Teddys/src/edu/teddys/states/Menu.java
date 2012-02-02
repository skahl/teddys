
package edu.teddys.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import de.lessvoid.nifty.Nifty;
import edu.teddys.BaseGame;
import edu.teddys.controls.MappingEnum;
import edu.teddys.menu.MenuTypes;

/**
 *
 * @author skahl
 */
public class Menu extends AbstractAppState {
    
//    private boolean enabled;
    private BaseGame app;
    private InputManager inputManager;
    
    private Nifty nifty;
    
    
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
    
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }
    
    @Override
    public void setEnabled(boolean isActive) {
        if(isActive && !this.isEnabled()) {
            // activate
            this.initKeys(true);
            
            nifty.gotoScreen(MenuTypes.MAIN_MENU.name());
            inputManager.setCursorVisible(true);
            super.setEnabled(true);
            
        } else if(!isActive && this.isEnabled()) {
            // deactivate
            this.initKeys(false);
            
            nifty.gotoScreen(MenuTypes.BLANK.name());
            inputManager.setCursorVisible(false);
            super.setEnabled(false);
        }
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (BaseGame)app;
        this.inputManager = this.app.getInputManager();
        super.setEnabled(false);
        
        nifty = ((BaseGame)app).getNifty();
        
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
