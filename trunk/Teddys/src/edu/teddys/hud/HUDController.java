/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.hud;

import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.scene.control.UpdateControl;
import edu.teddys.callables.SetHealthCallable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import edu.teddys.network.AttributeListener;

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
  private float timeToShow = 5;
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
  }

  /**
   * Set the HUD to be controlled.
   * @param hud 
   */
  public void setHUD(final HUD hud) {
    if (!hudSet) {
      this.hud = hud;

      hud.getNode().addControl(this);
      hudSet = true;
    }
  }

//  public static HUDController getInstance() {
//    if (instance == null) {
//      instance = new HUDController();
//    }
//    return instance;
//  }

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
  private void nextWeapon() {
    hud.getWeaponList().highlightNext();
  }

  /**
   * Highlight the previous entry in the weapon list.
   */
  private void previousWeapon() {
    hud.getWeaponList().highlightPrevious();
  }

  /**
   * show the weapon list.
   */
  private void showWeapons() {
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
  }

  public void onAction(String name, boolean isPressed, float tpf) {
    if (name.equals("previousWeapon")) {
      showWeapons();
      previousWeapon();
    } else if (name.equals("nextWeapon")) {
      showWeapons();
      nextWeapon();
    }
  }

  public void registerWithInput(InputManager input) {
    this.input = input;
    input.addMapping("nextWeapon", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
    input.addMapping("previousWeapon", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));

    input.addListener(this, new String[]{"nextWeapon", "previousWeapon"});
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
}