/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys;

import de.lessvoid.nifty.Nifty;
import edu.teddys.hud.HUDController;
import edu.teddys.objects.player.Player;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 * @author cm
 */
public class MegaLoggerListener extends AppenderSkeleton {
  
  private Nifty nifty;

  public MegaLoggerListener(Layout layout, Nifty nifty) {
    this.layout = layout;
    this.nifty = nifty;
    this.name = getClass().getSimpleName();
  }
  
  /*
   * 
   *TODO Show the specified message as a popup if the client is in a menu or as
   * HUD message if the player is currently in a game.
   */
  @Override
  protected void append(LoggingEvent le) {
    if (this.layout == null) {
      errorHandler.error("No layout for appender " + name,
              null,
              ErrorCode.MISSING_LAYOUT);
      return;
    }
    if(le.getLevel().isGreaterOrEqual(Level.INFO)) {
      String message = this.layout.format(le);
      //TODO this is dependent from the game state!
      if(Player.LOCAL_PLAYER != -1 && Player.getInstance(Player.LOCAL_PLAYER).getHUDController() != null) {
        Player.getInstance(Player.LOCAL_PLAYER).getHUDController().addMessage(message);
      }
    }
  }

  public void close() {
    // tudo bem ^^
  }

  public boolean requiresLayout() {
    return true;
  }
}
