/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.callables;

import com.jme3.math.Vector3f;
import edu.teddys.GameSettings;
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
    Vector3f serverPosition = positions.get(positions.size() - 1);
    if(fixed) {
      player.getPlayerControl().setPhysicsLocation(serverPosition);
      MegaLogger.getLogger().debug("Player set to a FIXED position.");
    } else {
      //TODO integrate smooth movement
      Vector3f curPos = player.getPlayerControl().getPhysicsLocation();
      player.getPlayerControl().setPhysicsLocation(serverPosition.interpolate(curPos,
              GameSettings.CLIENT_INTERPOL_SMOOTHING));
      if(curPos.distance(serverPosition) >= 1 && player.collidedWithLevel(serverPosition)) {
        // Set the Teddy directly to the position
        player.getPlayerControl().setPhysicsLocation(serverPosition);
        MegaLogger.getLogger().debug("User set to the desired position directly because there is a collision with the level");
      }
    }
    return null;
  }
}
