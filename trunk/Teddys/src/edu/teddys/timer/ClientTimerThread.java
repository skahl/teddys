/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import edu.teddys.network.TeddyClient;
import edu.teddys.network.messages.client.ManControllerInput;
import edu.teddys.input.InputTuple;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author cm
 */
public class ClientTimerThread extends Thread {
  
  private boolean stop = false;

  @Override
  public void run() {

    while(!stop) {

      List<InputTuple> inputList = ClientTimer.getInput();
      if (!inputList.isEmpty()) {
        ManControllerInput input = new ManControllerInput(new LinkedList<InputTuple>(inputList));
        // The timestamp has been already set in the empty constructor of
        // NetworkMessage
        TeddyClient.getInstance().send(input);
        ClientTimer.input.clear();
      }

      try {
        sleep((int) (1f / GameSettings.NETWORK_CLIENT_TIMER_RATE * 1000));
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
