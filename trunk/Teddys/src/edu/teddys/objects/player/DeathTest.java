/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.player;

import edu.teddys.MegaLogger;
import edu.teddys.callables.TeddyDeadCallable;
import edu.teddys.network.AttributeListener;
import edu.teddys.states.Game;

/**
 *
 * @author cm
 */
public class DeathTest implements AttributeListener<Boolean> {

  public void attributeChanged(Boolean value) {
    MegaLogger.getLogger().info("Your teddy is dead. Muahahahaha!");
    
    Game.getInstance().getApp().enqueue(new TeddyDeadCallable(Player.getInstance(Player.LOCAL_PLAYER)));
  }
  
}
