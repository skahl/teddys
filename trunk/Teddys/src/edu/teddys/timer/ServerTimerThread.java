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
  
  private volatile Long tick = new Long(0);

  @Override
  public void run() {
    //TODO parse game events
    
    // increment the tick by one
    setTick(++tick);
    try {
        sleep(GameSettings.SERVER_TIMESTAMP_INTERVAL);
      } catch (InterruptedException ex) {
        MegaLogger.getLogger().debug(new Throwable("Sleep request from timer interrupted!", ex));
      }
  }

  protected Long getTick() {
    //TODO check if synchronized is necessary
    return tick;
  }

  synchronized void setTick(Long tick) {
    this.tick = tick;
  }
}
