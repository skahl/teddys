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
 *
 * @author besient
 */
public class PauseMenu extends MessagePopupController {
    
    private Application app;
    
    private InputManager input;
    
    private boolean enabled;
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen);
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