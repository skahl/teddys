/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.callables;

import edu.teddys.hud.HUDController;
import edu.teddys.objects.player.Player;
import java.util.concurrent.Callable;

/**
 * 
 * Refreshed the indicator bar regarding the health of the local Teddy.
 *
 * @author cm
 */
public class SetHealthCallable implements Callable {

  private Integer playerID;
  private Integer health;

  public SetHealthCallable(Integer playerID, Integer health) {
    this.playerID = playerID;
    this.health = health;
  }

  public Object call() {
    Player player = Player.getInstance(playerID);
    player.getData().setHealth(health);
    if(player.getHUDController() != null) {
      player.getHUDController().setHealth(health);
    }
    return null;
  }
}
