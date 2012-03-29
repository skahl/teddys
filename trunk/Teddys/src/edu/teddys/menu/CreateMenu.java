/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.menu;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.screen.Screen;

/**
 *
 * @author besient
 */
public class CreateMenu extends MessagePopupController {
    
    private DropDown<String> map_dd;
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen);
        
        map_dd = screen.findNiftyControl("map_dd", DropDown.class);        
    }
    
    /**
     * Creates a new game on the selected map.
     */
    public void create() {
        
    }
    
    /**
     * Return to the main menu.
     */
    public void back() {
        nifty.gotoScreen(MenuTypes.MAIN_MENU.name());
    }
    
}
