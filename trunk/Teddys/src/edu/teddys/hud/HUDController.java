/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.hud;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.scene.control.UpdateControl;
import edu.teddys.GameModeEnum;
import edu.teddys.callables.SetHealthCallable;
import edu.teddys.input.ActionControllerEnum;
import java.util.ArrayList;
import java.util.List;
import edu.teddys.network.AttributeListener;
import edu.teddys.network.TeddyServer;
import edu.teddys.states.Game;
import java.util.Iterator;

/**
 * The HUD's controller class
 * 
 * @author besient
 */
public class HUDController extends UpdateControl implements ActionListener {

  private List<String> messages;
  private final int numMessages = 5;
  private int messagesReceived = 0;
  private boolean hudSet = false;
  private AttributeListener healthListener, currentItemListener, currentWeaponListener;
  private static HUDController instance = null;
  private HUD hud;
  private boolean weaponsShown;
  private float timeToShow = 3;
  private float timeShown = 0;
  private InputManager input;

  /**
   * Contructor
   */
  public HUDController() {

    messages = new ArrayList<String>();

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

    Game gameInstance = Game.getInstance();
    hud = HUD.getInstance(gameInstance.getApp().getGuiNode(),
            gameInstance.getAssetManager(),
            gameInstance.getApp().getSettings().getWidth(),
            gameInstance.getApp().getSettings().getHeight(),
            GameModeEnum.DEATHMATCH);

    if(!TeddyServer.getInstance().isRunning()) {
      hud.show();
    }

    //registerWithInput(gameInstance.getInputManager());
  }

  public static HUDController getInstance() {
    if (instance == null) {
      instance = new HUDController();
    }
    return instance;
  }

  /**
   * Set the HUD to be controlled.
   * @param hud 
   */
  public void setHUD(final HUD hud) {
    if (!hudSet) {
      this.hud = hud;

      //hud.getParent().addControl(this);
      hudSet = true;
    }
  }

  /**
   * Add a message at the bottom of the HUD's message area.
   * @param message 
   */
  public void addMessage(String message) {

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

  /**
   * 
   * Should be called via SetHealthCallable.
   * 
   * @see SetHealthCallable
   * 
   * @param health The new health value
   */
  public void setHealth(int health) {
    hud.setHealth(health);
  }

  public void setJetpackEnergy(int energy) {
    hud.setJetpackEnergy(energy);
  }

  /**
   * Highlight the next entry in the weapon list.
   */
  public void nextWeapon() {
    hud.getWeaponList().highlightNext();
  }

  /**
   * Highlight the previous entry in the weapon list.
   */
  public void previousWeapon() {
    hud.getWeaponList().highlightPrevious();
  }

  /**
   * show the weapon list.
   */
  public void showWeapons() {
    hud.getWeaponList().show();
    weaponsShown = true;
    timeShown = 0;
  }

  /**
   * Hide the weapon list.
   */
  private void hideWeapons() {
    hud.getWeaponList().hide();
    weaponsShown = false;
  }

  /**
   * Display the specified entry of the weapon list.
   * @param name 
   */
  public void selectWeapon(String name) {
      //showWeapons();
      hud.selectWeapon(name);
  }

  public void onAction(String name, boolean isPressed, float tpf) {
    if (name.equals(ActionControllerEnum.PREVIOUS_WEAPON.name())) {
      showWeapons();
      previousWeapon();
    } else if (name.equals(ActionControllerEnum.NEXT_WEAPON.name())) {
      showWeapons();
      nextWeapon();
    }
  }

  public final void registerWithInput(InputManager input) {
    this.input = input;

    input.addListener(this, new String[]{ActionControllerEnum.NEXT_WEAPON.name(),
              ActionControllerEnum.PREVIOUS_WEAPON.name()});
  }

  @Override
  public void update(float tpf) {
    if (weaponsShown) {
      timeShown += tpf;
      if (timeShown > timeToShow) {
        hideWeapons();
      }
    }
  }

  public HUD getHUD() {
    return hud;
  }
}