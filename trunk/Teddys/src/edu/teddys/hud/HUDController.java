/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.hud;

import edu.teddys.BaseGame;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import edu.teddys.network.AttributeListener;
import edu.teddys.states.Game;
import java.util.logging.Level;

/**
 *
 * @author besient
 */
public class HUDController {


  private List<String> messages;
  private final int numMessages = 5;
  private int messagesReceived = 0;
  private boolean hudSet = false;
  private AttributeListener healthListener, currentItemListener, currentWeaponListener;
  private static HUDController instance = null;
  private HUD hud;

  private HUDController() {     
    messages = new ArrayList<String>();
      
      healthListener = new AttributeListener<Integer>() {

        public void attributeChanged(Integer value) {
           hud.setHealth(value);
        }
      };

    currentItemListener = new AttributeListener() {

        public void attributeChanged(Object value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };

    currentWeaponListener = new AttributeListener() {

        public void attributeChanged(Object value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }; 
  }

  public void setHUD(final HUD hud) {
      this.hud = hud;
      hudSet = true;
  }
  public static HUDController getInstance() {
    if (instance == null) {
      instance = new HUDController();
    }
    return instance;
  }


  public void addMessage(String message) {

    BaseGame.getLogger().log(Level.INFO, "HUD: {0}", message);
    //Game.hud.setMessage(0, message);

    messages.add(0, message);
    messagesReceived++;
    
    if (hudSet) {
      int j = 0;

      if (messagesReceived < numMessages) {
        j = numMessages - messagesReceived;
      }
      Iterator it = messages.iterator();
      for (int i = j; i < numMessages; i++) {
        hud.setMessage(i, (String) it.next());
      }
    }
  }
  
  public void setHealth(int health) {
      hud.setHealth(health);
  }
  
  public void setJetpackEnergy(int energy) {
      hud.setJetpackEnergy(energy);
  }
}