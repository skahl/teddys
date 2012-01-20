/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.timer;

import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;

/**
 *
 * @author cm
 */
public class ClientTimerThread extends Thread {

  @Override
  public void run() {
    //TODO send a snapshot of the keyboard and mouse state
    
    try {
        sleep(GameSettings.SERVER_TIMESTAMP_INTERVAL);
      } catch (InterruptedException ex) {
        MegaLogger.debug(new Throwable("Sleep request from timer interrupted!", ex));
      }
  }
}
