package mygame;

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
import java.util.logging.Level;

/**
 * Represents the model of a game map that is used in a game.
 *
 * @author skahl
 */
public class GameMap {
    private Node mapModel;
    private Main app;
    
    private RigidBodyControl mapPhysics;
    private CollisionShape sceneShape;
    
    public GameMap(String mapPath, Main app) {
        //mapPath = "Models/firstlevel/firstlevel.j3o";
        
        try {
        
        mapModel = (Node)app.getAssetManager().loadModel(mapPath);
        mapModel.center();
        mapModel.getLocalRotation().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);
        
        
        
        // enable alpha blending
        // not necessary due to MultiSampling and renderManager.setAlphaToCoverage
        
        for(Spatial geom : mapModel.getChildren()) {
            if(geom instanceof Geometry) {
                Material mat = ((Geometry)geom).getMaterial();
                
                mat.getAdditionalRenderState().setAlphaTest(true);
                mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
                geom.setQueueBucket(Bucket.Transparent);
            }
        }
        
        sceneShape = CollisionShapeFactory.createMeshShape(mapModel);
        mapPhysics = new RigidBodyControl(sceneShape, 0);
        mapModel.addControl(mapPhysics);
        
        app.getRootNode().attachChild(mapModel);
        app.getBulletAppState().getPhysicsSpace().add(mapModel);
        
        //app.getRootNode().getLocalRotation().fromAngleAxis(-FastMath.HALF_PI, new Vector3f(0,1,0));
        
        
        
        } catch (AssetLoadException e) {
            e.printStackTrace();
        }
    }
    
    public Node getMapNode() {
        return mapModel;
    }
    
    public RigidBodyControl getMapPhysicsControl() {
        return mapPhysics;
    }
}
