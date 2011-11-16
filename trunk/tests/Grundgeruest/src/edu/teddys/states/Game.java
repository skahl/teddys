
package edu.teddys.states;

import edu.teddys.controls.RotationControl;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import edu.teddys.BaseGame;
import edu.teddys.objects.Jetpack;
import java.util.logging.Level;

/**
 *
 * @author skahl
 */
public class Game extends AbstractAppState {
    
    private boolean enabled;
    private BaseGame app;
    private InputManager inputManager;
    
    private boolean paused;
    
    private Node rotationNode;
    private RotationControl rotationControl;
    
    private Jetpack redJetpack;
    private Jetpack blueJetpack;
    
    
    
    // ActionListener
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if(name.equals("ParticleTrigger") && !keyPressed) {
                if(!redJetpack.isEnabled() || !blueJetpack.isEnabled()) {
                    redJetpack.setEnabled(true);
                    blueJetpack.setEnabled(true);
                } else {
                    redJetpack.setEnabled(false);
                    blueJetpack.setEnabled(false);
                }
            }
        }
    };
    
    
    // AnalogListener
    /*
    public AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float pressDuration, float tpf) {
            
            if(name.equals("ParticleTrigger")) {
                // TODO: Trigger particle emitter activation.
                jetpack1.setEnabled(true);
                jetpack2.setEnabled(true);
            } else {
                if(jetpack1.isEnabled()) {
                    jetpack1.setEnabled(false);
                }
                if(jetpack2.isEnabled()) {
                    jetpack2.setEnabled(false);
                }
            }
        }
    };
    
    */
    
    
    @Override
    public void update(float tpf) {
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public void setEnabled(boolean isActive) {
        if(isActive && !this.isEnabled()) {
            // activate
            
            // here one could attach and detach the whole scene graph
            this.app.getRootNode().attachChild(rotationNode);
            
            // attach keys
            initKeys(true);
            
            this.enabled = true;
            
        } else if(!isActive && this.isEnabled()) {
            // deactivate
            this.app.getRootNode().detachChild(rotationNode);
            
            // detach keys
            initKeys(false);
            
            this.enabled = false;
        }
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (BaseGame)app;
        this.inputManager = this.app.getInputManager();
        rotationControl = new RotationControl(this.app.getThreadPool());
        paused = false;
        enabled = false;
        
        // create the node
        rotationNode = new Node("rotationNode");
        
        // add blue jetpack
        blueJetpack = new Jetpack("blueJetpack", ColorRGBA.Blue, this.app.getAssetManager());
        blueJetpack.getNode().setLocalTranslation(new Vector3f(2f, 0f, -2f));
        blueJetpack.getNode().setLocalScale(0.5f);
        
        // add a red jetpack
        redJetpack = new Jetpack("blueJetpack", ColorRGBA.Red, this.app.getAssetManager());
        redJetpack.getNode().setLocalTranslation(new Vector3f(-2f, 0f, -2f));
        redJetpack.getNode().setLocalScale(0.5f);
        
        
        // attach boxes to rotationNode
        rotationNode.attachChild(blueJetpack.getNode());
        rotationNode.attachChild(redJetpack.getNode());
        
        // add rotationControl to rotationNode
        rotationNode.addControl(rotationControl);
        
        // TODO: HUD Anzeige aktivieren.
        
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        
        this.setEnabled(false);
    }
    
    public void initKeys(boolean attach) {
        
        if(attach) {
            // add key mappings
            //inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
            inputManager.addMapping("ParticleTrigger", new KeyTrigger(KeyInput.KEY_SPACE));

            // add the action listener
            inputManager.addListener(actionListener, new String[]{"ParticleTrigger"});

            // add the analog listener
            //inputManager.addListener(analogListener, new String[]{"ParticleTrigger"});
           
        } else {
            //inputManager.deleteMapping("Pause");
            inputManager.deleteMapping("ParticleTrigger");
        }
        
    }
    
    public void setPaused(boolean paused) {
        if(paused && !this.paused) {
            this.paused = true;
            
            redJetpack.pause();
            blueJetpack.pause();
                    
            rotationControl.setPaused(paused);
        } else if(!paused && this.paused) {
            this.paused = false;
            
            redJetpack.unpause();
            blueJetpack.unpause();
            
            rotationControl.setPaused(paused);
        }
    }
}
