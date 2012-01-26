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

/**
 *
 * @author cm
 */
public class ClientTimerThread extends Thread {

  @Override
  public void run() {

    for (;;) {

      LinkedList<SimpleTriple> inputList = (LinkedList<SimpleTriple>) ClientTimer.getInput().clone();
      ClientTimer.input.clear();
      if (!inputList.isEmpty()) {
        ManControllerInput input = new ManControllerInput(inputList);
        TeddyClient.getInstance().send(input);
      }

      try {
        sleep((int) ((float) GameSettings.CLIENT_TIMER_RATE / 10));
      } catch (InterruptedException ex) {
        MegaLogger.getLogger().debug(new Throwable("Sleep request from timer interrupted!", ex));
      }
    }
  }
}
