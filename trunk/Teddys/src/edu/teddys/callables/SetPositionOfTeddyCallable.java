/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.callables;

import com.jme3.math.Vector3f;
import edu.teddys.network.TeddyServer;
import edu.teddys.objects.player.Player;
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
  
  public Object call() throws Exception {
    //TODO if the array contains more than entry, use smoothed movements.
    Vector3f lastPosition = positions.get(positions.size()-1);
    player.getPlayerControl().setPhysicsLocation(lastPosition);
    //TODO check!
    TeddyServer.getInstance().getData().getClientPositions().put(player.getData().getId(), positions);
    return null;
  }
  
}
