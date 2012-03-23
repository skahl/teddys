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

  public SetPositionOfTeddyCallable(Player playerInstance, List<Vector3f> positions) {
    this.player = playerInstance;
    this.positions = positions;
  }

  public SetPositionOfTeddyCallable(Player playerInstance, Vector3f position) {
    this.player = playerInstance;
    this.positions = new ArrayList<Vector3f>();
    this.positions.add(position);
  }

  public Object call() throws Exception {
    //TODO if the array contains more than entry, use smoothed movements.
    Vector3f lastPosition = positions.get(positions.size() - 1);
    player.getPlayerControl().setPhysicsLocation(lastPosition);
    MegaLogger.getLogger().debug(String.format("Position of player %d set to %s",
            player.getData().getId(), lastPosition));
    return null;
  }
}
