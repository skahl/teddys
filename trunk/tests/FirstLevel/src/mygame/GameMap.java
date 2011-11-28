package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetLoadException;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Represents the model of a game map that is used in a game.
 *
 * @author skahl
 */
public class GameMap {
    Spatial mapModel;
    Main app;
    
    RigidBodyControl mapPhysics;
    CollisionShape sceneShape;
    
    public GameMap(String mapPath, Main app) {
        
        try {
        
        mapModel = app.getAssetManager().loadModel(mapPath);
        
        sceneShape = CollisionShapeFactory.createMeshShape((Node) mapModel);
        mapPhysics = new RigidBodyControl(sceneShape, 0);
        mapModel.addControl(mapPhysics);
        
        app.getRootNode().attachChild(mapModel);
        
        app.getBulletAppState().getPhysicsSpace().add(mapModel);
        
        } catch (AssetLoadException e) {
            e.printStackTrace();
        }
    }
}
