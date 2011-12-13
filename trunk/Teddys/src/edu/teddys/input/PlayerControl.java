package edu.teddys.input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author besient
 */
public class PlayerControl implements AnalogListener {

    private float moveSpeed = 0.01f;
    
    private InputManager input;
    private Node player;
    Vector3f vel;
    
    public PlayerControl(Node player) {
        this.player = player;
    }
    
    public void registerWithInput(InputManager input) {
        this.input = input;
        
        input.addMapping("moveLeft", new KeyTrigger(KeyInput.KEY_A));
        input.addMapping("moveRight", new KeyTrigger(KeyInput.KEY_D));
        input.addMapping("moveUp", new KeyTrigger(KeyInput.KEY_W));
        input.addMapping("moveDown", new KeyTrigger(KeyInput.KEY_S));
        
        input.addListener(this, new String[]{"moveLeft", 
                                            "moveRight", 
                                            "moveUp", 
                                            "moveDown"});
    }
    
    public void onAnalog(String name, float value, float tpf) {
        
        if (name.equals("moveLeft")) {
            vel = new Vector3f(-1, 0, 0);
            vel.multLocal(moveSpeed);
            player.move(vel);
        } else if (name.equals("moveRight")) {
            vel = new Vector3f(1, 0, 0);
            vel.multLocal(moveSpeed);
            player.move(vel);
        } else if (name.equals("moveUp")) {
            vel = new Vector3f(0, 1, 0);
            vel.multLocal(moveSpeed);
            player.move(vel);
        } else if (name.equals("moveDown")) {
            vel = new Vector3f(0, -1, 0);
            vel.multLocal(moveSpeed);
            player.move(vel);
        }

    }
    
}