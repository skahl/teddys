/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.callables;

import com.jme3.math.Vector3f;
import edu.teddys.MegaLogger;
import edu.teddys.objects.player.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author cm
 */
public class SetPositionOfTeddyCallable implements Callable {

  private Player player;
  List<Vector3f> positions = null;
  Boolean fixed = false;

  public SetPositionOfTeddyCallable(Player playerInstance, List<Vector3f> positions, Boolean fixed) {
    this.player = playerInstance;
    this.positions = positions;
    this.fixed = fixed;
  }

  public SetPositionOfTeddyCallable(Player playerInstance, Vector3f position, Boolean fixed) {
    this.player = playerInstance;
    this.positions = new ArrayList<Vector3f>();
    this.positions.add(position);
    this.fixed = fixed;
  }

  public Object call() throws Exception {
    if(positions.isEmpty()) {
      return null;
    }
    Vector3f lastPosition = positions.get(positions.size() - 1);
    if(fixed) {
      player.getPlayerControl().setPhysicsLocation(lastPosition);
      MegaLogger.getLogger().debug("Player set to a FIXED position.");
    } else {
      //TODO integrate smooth movement
      MegaLogger.getLogger().debug("SMOOOOOOOOTH");
      Vector3f curPos = player.getPlayerControl().getPhysicsLocation();
//      player.getPlayerControl().setWalkDirection(lastPosition.subtract(curPos));
    }
    MegaLogger.getLogger().debug(String.format("Position of player %d set to %s",
            player.getData().getId(), lastPosition));
    return null;
  }
}
