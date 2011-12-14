
package edu.teddys.objects.player;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.shape.Box;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import edu.teddys.input.PlayerControl;
import edu.teddys.states.Game;

/**
 *
 * @author skahl
 */
public class Player {
    
    String name;
    Node node;
    Box box;
    Geometry geo;
    Material mat;
    Game game;
    
    PlayerControl control;
    CapsuleCollisionShape collisionShape;
    
    
    public Player(String name, Game game) {
        this.game = game;
        
        node = new Node(name);

        box = new Box(0.3f, 0.3f, 0.01f);
        geo = new Geometry(name, box);

        mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", game.getAssetManager().loadTexture("Textures/teddy.png"));
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        
        geo.setMaterial(mat);
        geo.setShadowMode(ShadowMode.CastAndReceive);
        geo.setQueueBucket(Bucket.Transparent);
        
        node.attachChild(geo);
        
        // physics
        collisionShape = new CapsuleCollisionShape(getHeight()*0.5f, getWidth(), 1);
        
        control = new PlayerControl(node, collisionShape, 0.01f);
        control.registerWithInput(game.getInputManager());
        game.getBulletAppState().getPhysicsSpace().add(control);
    
        
        control.setJumpSpeed(4f);
        control.setGravity(4);
        control.setFallSpeed(4);
    }

    public String getName() {
        return name;
    }

    public Geometry getGeo() {
        return geo;
    }

    public Material getMat() {
        return mat;
    }

    public Node getNode() {
        return node;
    }
    
    public float getWidth() {
        return box.getXExtent();
    }
    
    public float getHeight() {
        return box.getYExtent();
    }

    public Box getBox() {
        return box;
    }
    
    public PlayerControl getPlayerControl() {
        return control;
    }
}
