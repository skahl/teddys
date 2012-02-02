/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.player;

import edu.teddys.MegaLogger;
import edu.teddys.hud.HUDController;
import edu.teddys.network.AttributeListener;
import edu.teddys.network.TeddyClient;

/**
 *
 * @author cm
 */
public class HealthListener implements AttributeListener<Integer> {

  public void attributeChanged(Integer value) {
    MegaLogger.getLogger().info("Listener: Health changed to " + value + "!");
    HUDController.getInstance().setHealth(TeddyClient.getInstance().getData().getHealth());
  }
}
