/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import edu.teddys.states.Game;

/**
 *
 * @author cm
 */
public class HealthListener implements AttributeListener<Integer> {

  public void attributeChanged(Integer value) {
    System.out.println("Listener: Health changed to " + value + "!");
    Game.hud.setHealth(TeddyClient.getInstance().getData().getHealth());
  }
}
