/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.menu;

import com.jme3.app.Application;
import com.jme3.input.InputManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import edu.teddys.network.SessionClientData;
import edu.teddys.objects.player.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ScreenController for the pause menu
 * @author besient
 */
public class PauseMenu extends MessagePopupController {

    private Application app;
    private InputManager input;
    private boolean enabled;
    private final int numEntries = 5;
    private List<Element> ranks, names, kills, deaths;
    private List<String> scores;

    /**
     * {@inheritDoc} 
     */
    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen);

        scores = new ArrayList<String>();
        ranks = new ArrayList<Element>();
        names = new ArrayList<Element>();
        kills = new ArrayList<Element>();
        deaths = new ArrayList<Element>();
        for (int i = 0; i < numEntries; i++) {
            ranks.add(nifty.getScreen("PAUSE_MENU").findElementByName("rank" + i));
            names.add(nifty.getScreen("PAUSE_MENU").findElementByName("name" + i));
            kills.add(nifty.getScreen("PAUSE_MENU").findElementByName("kill" + i));
            deaths.add(nifty.getScreen("PAUSE_MENU").findElementByName("death" + i));
        }
    }

    @Override
    public void onStartScreen() {
        updateScores();
    }

    private void updateScores() {
        int i = 0;
        for (Player p : Player.getInstanceList()) {
            if (i < numEntries) {
                SessionClientData data = p.getData().getSession();
                ranks.get(i).getRenderer(TextRenderer.class).setText(String.valueOf(i));
                names.get(i).getRenderer(TextRenderer.class).setText(p.getData().getName());
                //kills.get(i).getRenderer(TextRenderer.class).setText(String.valueOf(data.getKills()));
                //deaths.get(i).getRenderer(TextRenderer.class).setText(String.valueOf(data.getDeaths()));
                i++;
            } else {
                return;
            }
        }

//        for (int i = 0; i < scores.size(); i++) {
//            String[] score = scores.get(i).split(":");
//            ranks.get(i).getRenderer(TextRenderer.class).setText(String.valueOf(i));
//            names.get(i).getRenderer(TextRenderer.class).setText(score[0]);
//            kills.get(i).getRenderer(TextRenderer.class).setText(score[1]);
//            deaths.get(i).getRenderer(TextRenderer.class).setText(score[2]);
//        }
    }

    public void setScore(String score) {
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