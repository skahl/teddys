/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.states;

import edu.teddys.controls.RotationControl;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import edu.teddys.BaseGame;

/**
 *
 * @author skahl
 */
public class Pause extends AbstractAppState {
    
    private boolean enabled;
    private BaseGame app;
    
    
    @Override
    public void update(float tpf) {
        
        // TODO: Pause app state update code
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public void setEnabled(boolean isActive) {
        if(isActive && !this.isEnabled()) {
            // activate
            
            // TODO: Pause app state enabled code
            
            this.enabled = true;
            
        } else if(!isActive && this.isEnabled()) {
            // deactivate

            // TODO: Pause app state disabled code
            
            this.enabled = false;
        }
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (BaseGame)app;
        enabled = false;
        
        // TODO: Pause app state init code
        
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        
        // TODO: Pause app state cleanup code
        
        this.setEnabled(false);
    }
}
