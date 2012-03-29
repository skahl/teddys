/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import com.jme3.math.Vector3f;
import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import edu.teddys.network.TeddyServer;
import edu.teddys.network.messages.server.ManMessageSetPosition;
import edu.teddys.objects.player.Player;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author cm
 */
public class ServerTimerThread extends Thread {

  private boolean stop = false;
  private Long tick = new Long(0);

  @Override
  public void run() {

    while (!stop) {

      // The following steps are only necessary for the server because it collects the
      // players' positions.
      if (TeddyServer.getInstance().isRunning()) {
        if (tick % GameSettings.TRANSMIT_POSITION_MOD == 0) {
          ManMessageSetPosition posMsg = new ManMessageSetPosition();
          // update the players' positions
          for (Player player : Player.getInstanceList()) {
            // Local server player?
            if(player.getData().getId() == -1) {
              continue;
            }
            posMsg.getPositions().put(player.getData().getId(), player.getPlayerControl().getPhysicsLocation());
          }
          addClientPosition(posMsg.getPositions());
          // use smooth movements
          posMsg.setFixed(false);
          TeddyServer.getInstance().send(posMsg);
        }
      }

      // increment the tick by one
      ++tick;
      try {
        sleep(GameSettings.SERVER_TIMESTAMP_INTERVAL);
      } catch (InterruptedException ex) {
        MegaLogger.getLogger().debug(new Throwable("Sleep request from timer interrupted!", ex));
      }
    }
  }

  protected Long getTick() {
    return tick;
  }

  protected synchronized void setTick(Long ts) {
    tick = ts;
  }

  /**
   * 
   * Adds an element to the position List in TeddyServerData.
   * If the capacity (@see GameSettings.MAX_SERVER_POS_CAPACITY) has been reached, dismiss the first value.
   * 
   * So, every time this is called, add the positions to the tail of the List.
   * 
   * @param posClients Positions of the clients
   */
  public synchronized void addClientPosition(Map<Integer, Vector3f> posClients) {
    TreeMap<Long, Map<Integer, Vector3f>> posMap = TeddyServer.getInstance().getData().getClientPositions();
    if (posMap.size() > GameSettings.MAX_SERVER_POS_CAPACITY) {
      // Remove the first entry
      posMap.pollFirstEntry();
    }
    posMap.put(tick, posClients);
    TeddyServer.getInstance().getData().setClientPositions(posMap);
  }

  /**
   * 
   * Set the flag to stop the current thread.
   * 
   */
  void stopThread() {
    stop = true;
  }
}
