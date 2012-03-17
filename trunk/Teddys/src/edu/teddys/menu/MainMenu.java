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
import de.lessvoid.nifty.screen.ScreenController;
import edu.teddys.BaseGame;
/**
 *
 * @author besient
 */
public class MainMenu implements ScreenController {

    Nifty nifty;
    Screen screen;
    
    private Application app;
    private float width, height;
    
    InputManager input;
    
    private boolean enabled;
    
    NiftyImage teddy1, teddy2, teddy3, teddy4, teddy5;
    Element teddyImage, popupElement;
    
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        
        teddyImage = nifty.getCurrentScreen().findElementByName("teddy_image");
        teddy1 = nifty.getRenderEngine().createImage("Interface/GUI/teddy2.png", false);
        teddy2 = nifty.getRenderEngine().createImage("Interface/GUI/teddy3.png", false);
        teddy3 = nifty.getRenderEngine().createImage("Interface/GUI/teddy4.png", false);
        teddy4 = nifty.getRenderEngine().createImage("Interface/GUI/teddy5.png", false);
        teddy5 = nifty.getRenderEngine().createImage("Interface/GUI/teddy6.png", false);
        
        popupElement = nifty.createPopup("EXIT_POPUP");
    }

    public void onStartScreen() {
        
    }

    public void onEndScreen() {
        
    }
    
    public void setApplication(Application app) {
        this.app = app;
        input = app.getInputManager();
        width = ((BaseGame)app).getSettings().getWidth();
        height = ((BaseGame)app).getSettings().getHeight();
    }
    
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
    
    public void showJoinScreen() {
        nifty.gotoScreen(MenuTypes.JOIN_GAME.name());
    }
    
    public void showCreateScreen() {
        nifty.gotoScreen(MenuTypes.CREATE_GAME.name());
    }
    
    public void showOptionsScreen() {
        nifty.gotoScreen(MenuTypes.OPTIONS_MENU.name());
    }
    
    public void showCreditsScreen() {
        nifty.gotoScreen(MenuTypes.CREDITS.name());
    }
    
    public void back() {
        nifty.gotoScreen(MenuTypes.MAIN_MENU.name());
    }
    
    public void reallyQuit() {
        nifty.showPopup(nifty.getCurrentScreen(), popupElement.getId(), null);
    }
    
    public void closePopup() {
        nifty.closePopup(popupElement.getId());
    }
    
    public void exit() {
        app.stop();
    }
    
}