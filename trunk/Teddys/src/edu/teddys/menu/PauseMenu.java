/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.menu;

import com.jme3.app.Application;
import com.jme3.input.InputManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
/**
 * ScreenController for the pause menu
 * @author besient
 */
public class PauseMenu extends MessagePopupController {
    
    private Application app;
    
    private InputManager input;
    
    private boolean enabled;
    
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen);
    }


    /**
     * Setter method for the application object.
     * @param app 
     */
    public void setApplication(Application app) {
        this.app = app;
    }
    
    /**
     * Hides the menu.
     */
    public void returnToGame() {
        nifty.gotoScreen(MenuTypes.BLANK.name());
    }
    
    /**
     * Disconnect from the server.
     */
    public void disconnect() {
        
    }
    
    /**
     * Stop the application.
     */
    public void exit() {
        app.stop();
    }
    
}