/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.menu;

import com.jme3.app.Application;
import com.jme3.input.InputManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import edu.teddys.BaseGame;

/**
 * ScreenController for the main menu
 * 
 * @author besient
 */
public class MainMenu extends MessagePopupController {

    
    private Application app;
    private float width, height;
    
    private InputManager input;
    
    private boolean enabled;
    
    private NiftyImage teddy1, teddy2, teddy3, teddy4, teddy5;
    private Element teddyImage, popupElement;
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen);
        
        teddyImage = nifty.getCurrentScreen().findElementByName("teddy_image");
        teddy1 = nifty.getRenderEngine().createImage("Interface/GUI/teddy2.png", false);
        teddy2 = nifty.getRenderEngine().createImage("Interface/GUI/teddy3.png", false);
        teddy3 = nifty.getRenderEngine().createImage("Interface/GUI/teddy4.png", false);
        teddy4 = nifty.getRenderEngine().createImage("Interface/GUI/teddy5.png", false);
        teddy5 = nifty.getRenderEngine().createImage("Interface/GUI/teddy6.png", false);

        popupElement = nifty.createPopup(PopupTypes.EXIT_POPUP.name());
    }

    /**
     * Setter method for the application object
     * @param app 
     */
    public void setApplication(Application app) {
        this.app = app;
        input = app.getInputManager();
        width = ((BaseGame)app).getSettings().getWidth();
        height = ((BaseGame)app).getSettings().getHeight();
    }
    
    /**
     * Update the weapon's orientation depending on the mouse position.
     */
    public void processMousePosition() {
        float mouseY = input.getCursorPosition().y;
       
        if (mouseY < height / 5) {
            teddyImage.getRenderer(ImageRenderer.class).setImage(teddy5);
        } else if (mouseY < 2 * height / 5) {
            teddyImage.getRenderer(ImageRenderer.class).setImage(teddy4);
        } else if (mouseY < 3 * height / 5) {
            teddyImage.getRenderer(ImageRenderer.class).setImage(teddy3);
        } else if (mouseY < 4 * height / 5) {
            teddyImage.getRenderer(ImageRenderer.class).setImage(teddy2);
        } else {
            teddyImage.getRenderer(ImageRenderer.class).setImage(teddy1);
        }
    }
    

    /**
     * Shows the join screen.
     */
    public void showJoinScreen() {
        nifty.gotoScreen(MenuTypes.JOIN_GAME.name());
    }
    
    /**
     * Shows the create screen.
     */
    public void showCreateScreen() {
        nifty.gotoScreen(MenuTypes.CREATE_GAME.name());
    }
    
    /**
     * Shows the options screen.
     */
    public void showOptionsScreen() {
        nifty.gotoScreen(MenuTypes.OPTIONS_MENU.name());
    }
    
    /**
     * Shows the credits screen.
     */
    public void showCreditsScreen() {
        nifty.gotoScreen(MenuTypes.CREDITS.name());
    }
    
    /**
     * Return to the main menu.
     */
    public void back() {
        nifty.gotoScreen(MenuTypes.MAIN_MENU.name());
    }
    
    /**
     * Shows the exit confirmation dialog.
     */
    public void reallyQuit() {
        nifty.showPopup(nifty.getCurrentScreen(), popupElement.getId(), null);
    }
    
    /**
     * Closes the exit confirmation dialog.
     */
    public void closePopup() {
        nifty.closePopup(popupElement.getId());
    }
    
    /**
     * Stops the application.
     */
    public void exit() {
        app.stop();
    }


    
}