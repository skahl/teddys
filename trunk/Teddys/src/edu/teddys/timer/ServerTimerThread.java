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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author cm
 */
public class ServerTimerThread extends Thread {

  
  private boolean stop = false;
  private Long tick = new Long(0);
  /**
   * History of the clients' positions for the lag compensation.
   */
  private Map<Integer,LinkedBlockingQueue<Vector3f>> clientPositions = new HashMap<Integer,LinkedBlockingQueue<Vector3f>>();
  
  @Override
  public void run() {
    
    while (!stop) {
      //TODO parse game events

//      ManMessageSetPosition posMsg = new ManMessageSetPosition();
//      // update the players' positions
//      for(Player player : Player.getInstanceList()) {
//        addClientPosition(player.getData().getId(), player.getPlayerControl().getPhysicsLocation());
//        posMsg.setClientID(player.getData().getId());
//        posMsg.setPosition(player.getPlayerControl().getPhysicsLocation());
////        TeddyServer.getInstance().send(posMsg);
//      }
      
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

  public Map<Integer, LinkedBlockingQueue<Vector3f>> getClientPositions() {
    return clientPositions;
  }
  
  /**
   * 
   * Adds an element to the position List. If the capacity has been reached, dismiss the first value.
   * So, every time this is called, add the position to the tail of the List.
   * 
   * If the List does not exist for the user, it is created automatically.
   * 
   * @param clientId  The player ID.
   * @param pos The last "known" position.
   */
  public synchronized void addClientPosition(Integer clientId, Vector3f pos) {
    if(!getClientPositions().containsKey(clientId)) {
      getClientPositions().put(clientId, new LinkedBlockingQueue<Vector3f>(GameSettings.MAX_SERVER_POS_CAPACITY));
    }
    // check if the queue is full
    if(getClientPositions().get(clientId).remainingCapacity() == 0) {
      getClientPositions().get(clientId).poll();
    }
    // now the position is added :)
    getClientPositions().get(clientId).offer(pos);
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
