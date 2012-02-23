/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.player;

import edu.teddys.MegaLogger;
import edu.teddys.hud.HUDController;
import edu.teddys.network.AttributeListener;

/**
 *
 * @author cm
 */
public class HealthListener implements AttributeListener<Integer> {

  public void attributeChanged(Integer value) {
    MegaLogger.getLogger().info("Listener: Health changed to " + value + "!");
    
    // TODO: TRIGGERS HUD TO UPDATE OUTSIDE UPDATE CYCLE!!!
//    HUDController.getInstance().setHealth(value);
  }
}
