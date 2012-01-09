/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.states;

import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;

/**
 *
 * @author besient
 */
public class AppStateSwitcher {
    
    private static AppStateSwitcher instance = null;
    private AppStateManager manager;
    
    private AppStateSwitcher(AppStateManager manager) {
        this.manager = manager;
    }
    
    public void setState(AppState state) {
        manager.detach(manager.getState(AppState.class));
        manager.attach(state);
    }
    
    public static AppStateSwitcher getInstance(AppStateManager manager) {
        if (instance == null) 
            instance = new AppStateSwitcher(manager);        
        return instance;
    }
}
