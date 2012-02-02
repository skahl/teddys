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
public class ServerTimerThread extends Thread {

  
  private boolean stop = false;
  private Long tick = new Long(0);

  @Override
  public void run() {
    
    while (!stop) {
      //TODO parse game events

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
   * Set the flag to stop the current thread.
   * 
   */
  void stopThread() {
    stop = true;
  }
}
