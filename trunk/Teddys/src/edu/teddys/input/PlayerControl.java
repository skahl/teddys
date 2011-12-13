package edu.teddys.input;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author besient
 */
public class PlayerControl extends CharacterControl implements AnalogListener {

    private float moveSpeed = 0.1f;
    
    private InputManager input;
    Vector3f vel;
    
    
    public PlayerControl(Spatial player, CollisionShape collisionShape, float stepHeight) {
        super(collisionShape, stepHeight);
        setPhysicsLocation(player.getWorldTranslation());
        setFallSpeed(30);
        setJumpSpeed(30);
        setGravity(30);
        player.addControl(this);        
    }
        
    public void registerWithInput(InputManager input) {
        this.input = input;
        
        input.addMapping("moveLeft", new KeyTrigger(KeyInput.KEY_A));
        input.addMapping("moveRight", new KeyTrigger(KeyInput.KEY_D));
        input.addMapping("jump", new KeyTrigger(KeyInput.KEY_SPACE));
        
        input.addListener(this, new String[]{"moveLeft", 
                                            "moveRight",
                                            "jump"});
    }
    
    public void onAnalog(String name, float value, float tpf) {
        
        if (name.equals("moveLeft")) {
            vel = new Vector3f(-1, 0, 0);
            vel.multLocal(moveSpeed);
            warp(getPhysicsLocation().add(vel));
        } else if (name.equals("moveRight")) {
            vel = new Vector3f(1, 0, 0);
            vel.multLocal(moveSpeed);
            warp(getPhysicsLocation().add(vel));
        } else if (name.equals("jump")) {
            jump();
        }

    }
    
}