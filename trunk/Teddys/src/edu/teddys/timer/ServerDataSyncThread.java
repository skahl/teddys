/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import com.jme3.math.Vector3f;
import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import edu.teddys.network.TeddyClient;
import edu.teddys.network.TeddyServer;
import edu.teddys.network.TeddyServerData;
import edu.teddys.network.messages.server.ManMessageTransferServerData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author cm
 */
public class ServerDataSyncThread extends Thread {

  private boolean stop = false;

  @Override
  public void run() {
    //TODO dismiss old values
    while (!stop) {
      //TODO(nice to have) delta compression?
      
      TeddyServerData data = TeddyServer.getInstance().getData();
      Map<Integer,List<Vector3f>> pos = new TreeMap<Integer,List<Vector3f>>();
      // add the positions to the clientPositions Map
      for(Entry<Integer,LinkedBlockingQueue<Vector3f>> entry : ServerTimer.getClientPositions().entrySet()) {
        pos.put(entry.getKey(), new ArrayList<Vector3f>(entry.getValue()));
      }
      data.setClientPositions(pos);
      ManMessageTransferServerData msg = new ManMessageTransferServerData(data);
      TeddyServer.getInstance().send(msg);
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
