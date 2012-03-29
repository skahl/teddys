/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import edu.teddys.network.ClientData;
import edu.teddys.network.TeddyServer;
import edu.teddys.network.messages.server.ManMessageTransferPlayerData;
import edu.teddys.objects.player.Player;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cm
 */
public class ServerDataSyncThread extends Thread {

  private boolean stop = false;

  @Override
  public void run() {
    
    while (!stop) {
      
//      TeddyServerData data = TeddyServer.getInstance().getData();
//      Map<Integer,List<Vector3f>> pos = new TreeMap<Integer,List<Vector3f>>();
//      // add the positions to the clientPositions Map
//      for(Entry<Integer,LinkedBlockingQueue<Vector3f>> entry : ServerTimer.getClientPositions().entrySet()) {
//        pos.put(entry.getKey(), new ArrayList<Vector3f>(entry.getValue()));
//      }
//      data.setClientPositions(pos);
//      ManMessageTransferServerData msg = new ManMessageTransferServerData(data);
//      TeddyServer.getInstance().send(msg);
      
//      ManMessageSetPosition posMsg = new ManMessageSetPosition();
//      // update the players' positions
//      for(Player player : Player.getInstanceList()) {
//        posMsg.getPositions().put(player.getData().getId(), player.getPlayerControl().getPhysicsLocation());
//      }
//      TeddyServer.getInstance().send(posMsg);
      
      List<ClientData> clientData = new ArrayList<ClientData>();
      for(Player player : Player.getInstanceList()) {
        // Local server player
        if(player.getData().getId() == -1) {
          continue;
        }
        clientData.add(player.getData());
      }
      ManMessageTransferPlayerData transferPlayerData = new ManMessageTransferPlayerData(clientData);
      TeddyServer.getInstance().send(transferPlayerData);
      
      // ... and sleep an amount of time.
      try {
        sleep(GameSettings.NETWORK_SERVER_SYNC_INTERVAL);
      } catch (InterruptedException ex) {
        MegaLogger.getLogger().debug(new Throwable("Sleep request from timer interrupted!", ex));
      }
    }
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
