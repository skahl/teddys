/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.player;

import edu.teddys.MegaLogger;
import edu.teddys.network.AttributeListener;

/**
 *
 * @author cm
 */
public class DeathTest implements AttributeListener<Boolean> {

  public void attributeChanged(Boolean value) {
    MegaLogger.getLogger().info("Your teddy is dead. Muahahahaha!");
  }
  
}
