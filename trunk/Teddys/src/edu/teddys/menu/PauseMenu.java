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
import edu.teddys.network.ClientSessionData;
import edu.teddys.objects.player.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
            kills.add(nifty.getScreen("PAUSE_MENU").findElementByName("kills" + i));
            deaths.add(nifty.getScreen("PAUSE_MENU").findElementByName("deaths" + i));
        }
    }

    @Override
    public void onStartScreen() {
        updateScores();
    }

    /**
     * Retrieves the player data and displays them in order.
     */
    private void updateScores() {
        
        Player[] players = new Player[Player.getInstanceList().size()]; 
        Player.getInstanceList().toArray(players);
        Comparator comp = new Comparator() {
            public int compare(Object t, Object t1) {
                float r1;
                if (((Player) t).getData().getSession().getDeaths()==0)
                    r1 = ((Player) t).getData().getSession().getKills();
                else
                    r1 = ((Player) t).getData().getSession().getKills() /
                        ((Player) t).getData().getSession().getDeaths();
                float r2;
                if (((Player) t1).getData().getSession().getDeaths()==0)
                    r2 = ((Player) t1).getData().getSession().getKills();
                else
                    r2 = ((Player) t1).getData().getSession().getKills() /
                        ((Player) t1).getData().getSession().getDeaths();
                if (r1 < r2) return -1;
                else if (r1 > r2) return 1;
                else return 0;
            }            
        };
        Arrays.sort(players, comp);
        
        for (int i = 0; i < players.length; i++) {
            ClientSessionData data = players[i].getData().getSession();            
            
            if (i < numEntries) {
                
                ranks.get(i).getRenderer(TextRenderer.class).setText(String.valueOf(i+1));
                names.get(i).getRenderer(TextRenderer.class).setText(players[i].getData().getName());
                kills.get(i).getRenderer(TextRenderer.class).setText(String.valueOf(data.getKills()));
                deaths.get(i).getRenderer(TextRenderer.class).setText(String.valueOf(data.getDeaths()));
                
            } else {
                return;
            }
        }


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