package edu.teddys.input;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import edu.teddys.hud.HUDController;

/**
 *
 * @author besient
 */
public class PlayerControl extends CharacterControl implements AnalogListener, ActionListener {

    private float moveSpeed = 0.02f;
    private float totalEnergy, currentEnergy;
    private boolean jetpackActive;
    
    private InputManager input;
    private  Vector3f vel;
    
    
    public PlayerControl(Spatial player, CollisionShape collisionShape, float stepHeight) {
        super(collisionShape, stepHeight);
        setPhysicsLocation(player.getWorldTranslation());
//        setFallSpeed(1f);
//        setJumpSpeed(1f);
//        setGravity(1f);
        player.addControl(this);   
        
        
        totalEnergy = 100;
        currentEnergy = totalEnergy;

    }
        
    public void registerWithInput(InputManager input) {
        this.input = input;
        
        input.addMapping("moveLeft", new KeyTrigger(KeyInput.KEY_A));
        input.addMapping("moveRight", new KeyTrigger(KeyInput.KEY_D));
        input.addMapping("jump", new KeyTrigger(KeyInput.KEY_SPACE));
        input.addMapping("jetpack", new KeyTrigger(KeyInput.KEY_LCONTROL));
        
        input.addListener(this, new String[]{"moveLeft", 
                                            "moveRight",
                                            "jump",
                                            "jetpack"});
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
    
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("jetpack")) {
            if (!jetpackActive && isPressed) {
                startJetpack();                
            } else {
                stopJetpack();
            }    
        }
    }
    
    private void startJetpack() {
        if (currentEnergy > 0f) {
            setGravity(-4f);
            jetpackActive = true;
        } else {
            stopJetpack();
        }
    }
    
    private void stopJetpack() {
        setGravity(4f);
        jetpackActive = false;
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (jetpackActive) {
            if (currentEnergy > 0) {
                currentEnergy -= 75f * tpf;
            } else {
                stopJetpack();
            }    
        } else {
            if (currentEnergy < totalEnergy) {
                currentEnergy += 25f * tpf;
            }
        }
        
        HUDController.getInstance().setJetpackEnergy((int)currentEnergy);
    }

    

    
}