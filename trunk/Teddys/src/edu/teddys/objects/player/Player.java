
package edu.teddys.objects.player;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.scene.Node;
import edu.teddys.input.PlayerControl;
import edu.teddys.states.Game;

/**
 *  Singleton Player
 * @author skahl
 */
public class Player {
    
    private static Player INSTANCE = null;
    
    String name;
    Node node;
    TeddyVisual visual;
    Game game;
    
    PlayerControl control;
    CapsuleCollisionShape collisionShape;
    
    
    private Player(String name, Game game) {
        this.game = game;
        node = new Node(name);
        visual = new TeddyVisual(node, game.getAssetManager());
        
        // physics
        collisionShape = new CapsuleCollisionShape(visual.getWidth()*0.6f, visual.getHeight()*0.8f, 1);
        
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
    
    public PlayerControl getPlayerControl() {
        return control;
    }
    
    public Node getNode() {
        return node;
    }
    
    public static Player getInstance(String name, Game game) {
        if(INSTANCE == null) {
            INSTANCE = new Player(name, game);
        }
        
        return INSTANCE;
    }
}
