/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import edu.teddys.network.TeddyClient;
import edu.teddys.network.messages.client.ManControllerInput;
import edu.teddys.input.SimpleTriple;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author cm
 */
public class ClientTimerThread extends Thread {

  @Override
  public void run() {

    for (;;) {

      List<SimpleTriple> inputList = ClientTimer.getInput();
      if (!inputList.isEmpty()) {
        ManControllerInput input = new ManControllerInput(new LinkedList<SimpleTriple>(inputList));
        TeddyClient.getInstance().send(input);
        ClientTimer.input.clear();
      }

      try {
        sleep((int) (1f / GameSettings.CLIENT_TIMER_RATE));
      } catch (InterruptedException ex) {
        MegaLogger.getLogger().debug(new Throwable("Sleep request from timer interrupted!", ex));
      }
    }
  }
}
