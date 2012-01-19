/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import com.jme3.math.Vector3f;
import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import edu.teddys.network.TeddyClient;
import edu.teddys.network.messages.client.ManMessageSendPosition;
import edu.teddys.states.Game;

/**
 *
 * @author cm
 */
public class SendPositionControllerThread extends Thread {

  @Override
  public void run() {
    //TODO dismiss old values
    for (;;) {
      // get the current position
      Vector3f playerVector = Game.getInstance().getPlayer().getPlayerControl().getPhysicsLocation();
      TeddyClient.getInstance().getData().setPosition(playerVector);
      ManMessageSendPosition msg = new ManMessageSendPosition(playerVector);
      TeddyClient.getInstance().send(msg);
      // ... and sleep an amount of time.
      try {
        sleep((int) (1 / GameSettings.SENDPOSITION_TIMER_RATE));
      } catch (InterruptedException ex) {
        MegaLogger.debug(new Throwable("Sleep request from timer interrupted!", ex));
      }
    }
  }
}
