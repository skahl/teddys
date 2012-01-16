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
    
    public enum AppStateEnum {
        GAME,
        PAUSE,
        MENU
    };
    
    private static AppStateSwitcher instance = null;
    private AppStateManager manager;
    
    private AppStateSwitcher(AppStateManager manager) {
        this.manager = manager;
    }
    
    public void activateState(AppStateEnum state) {
        switch (state) {
            case GAME:
                manager.getState(Menu.class).setEnabled(false);
                manager.getState(Pause.class).setEnabled(false);
                manager.getState(Game.class).setEnabled(true);
                break;
            case MENU:
                manager.getState(Menu.class).setEnabled(true);
                manager.getState(Pause.class).setEnabled(false);
                manager.getState(Game.class).setEnabled(false);
                break;
            case PAUSE:
                manager.getState(Menu.class).setEnabled(false);
                manager.getState(Pause.class).setEnabled(true);
                manager.getState(Game.class).setEnabled(false);
                
        }
    }
    
        
    public static AppStateSwitcher getInstance(AppStateManager manager) {
        if (instance == null) 
            instance = new AppStateSwitcher(manager);        
        return instance;
    }
}
