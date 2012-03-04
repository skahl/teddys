package edu.teddys.map;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetLoadException;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import edu.teddys.BaseGame;
import edu.teddys.MegaLogger;
import edu.teddys.states.Game;
import java.util.logging.Level;

/**
 * Represents the model of a game map that is used in a game.
 *
 * @author skahl
 */
public class GameMap {

  private Node mapModel;
  private BaseGame app;
  private RigidBodyControl mapPhysics;
  private CollisionShape sceneShape;

  public GameMap(String mapPath, Game game) {
    mapPath = "Models/firstlevel/firstlevel.j3o";

    try {

      mapModel = (Node) game.getApp().getAssetManager().loadModel(mapPath);

      mapModel.center();
      mapModel.getLocalRotation().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y);

      sceneShape = CollisionShapeFactory.createMeshShape(mapModel);
      mapPhysics = new RigidBodyControl(sceneShape, 0);
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
