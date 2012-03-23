/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.callables;

import com.jme3.math.Vector3f;
import edu.teddys.objects.player.Player;
import edu.teddys.states.Game;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author cm
 */
public class TeddyDeadCallable implements Callable {

  private Player player;

  public TeddyDeadCallable(Player playerInstance) {
    this.player = playerInstance;
  }

  public Object call() throws Exception {
    
    // Trigger PlayerVisual.die()
    player.getPlayerVisual().die();
    
    // Rid player of all items and weapons except the StenGun
    Game.getInstance().getApp().enqueue(new SetHealthCallable(player.getData().getId(), 100));
    player.getData().setHealth(100);
    
    // Respawn player at a new position
    Game.getInstance().setRandomPlayerPosition(player);
    
    return null;
  }
}
