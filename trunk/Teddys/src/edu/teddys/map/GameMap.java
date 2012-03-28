package edu.teddys.map;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import edu.teddys.MegaLogger;
import edu.teddys.states.Game;

/**
 * Represents the model of a game map that is used in a game.
 *
 * @author skahl
 */
public class GameMap {

  private Node mapModel;
  private RigidBodyControl mapPhysics;
  private CollisionShape sceneShape;

  public GameMap(String mapPath, Game game) {
    mapPath = "Models/firstlevel/firstlevel.j3o";
//    mapPath = "Models/darkness/darkness.j3o";

    try {

      mapModel = (Node) game.getApp().getAssetManager().loadModel(mapPath);

      mapModel.center();
      mapModel.getLocalRotation().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y);

      sceneShape = CollisionShapeFactory.createMeshShape(mapModel);
      mapPhysics = new RigidBodyControl(sceneShape, 0);
//      mapPhysics.setKinematic(true);
      mapModel.addControl(mapPhysics);

    } catch (Exception e) {
      MegaLogger.getLogger().warn(e);
    }
  }

  public Node getMapModel() {
    return mapModel;
  }

  public RigidBodyControl getMapPhysicsControl() {
    return mapPhysics;
  }
}
