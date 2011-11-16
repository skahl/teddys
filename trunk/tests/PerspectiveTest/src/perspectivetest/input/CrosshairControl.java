package perspectivetest.input;

import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Spatial;

/**
 *
 * @author besient
 */
public class CrosshairControl implements AnalogListener {

    private float scrollSpeed = 0.3f;
    private float mouseSpeed = 0.3f;
    private float minDistance = 0.2f;
    private float maxDistance = 0.35f;
    
    private InputManager input;
    Camera cam;

    CameraNode camNode;
    Spatial player;
    Spatial crosshair;
    
    Vector3f vel;
    Vector2f mousePos, screenPos;
    float dist;
    
    
    public CrosshairControl(CameraNode camNode, Spatial player, Spatial crosshair) {

        this.camNode = camNode;
        cam = camNode.getCamera();
        this.player = player;
        this.crosshair = crosshair;
    }
    
    public void registerWithInput(InputManager input) {
        this.input = input;
        
        input.addMapping("scrollLeft", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        input.addMapping("scrollRight", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        input.addMapping("scrollUp", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        input.addMapping("scrollDown", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        
        input.addListener(this, new String[]{"scrollLeft", 
                                            "scrollRight", 
                                            "scrollUp", 
                                            "scrollDown"});
    }
    
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }

    public void setMinDistance(float minDistance) {
        this.minDistance = minDistance;
    }

    public void setScrollSpeed(float scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }
    
    public void setMouseSpeed(float mouseSpeed) {
        this.mouseSpeed = mouseSpeed;
    }
    
    public void onAnalog(String name, float value, float tpf) {
        
        vel = new Vector3f();        
        
        if (name.equals("scrollLeft")) {
            vel = Vector3f.UNIT_X.mult(-value * scrollSpeed);
        } else if (name.equals("scrollRight")) {
            vel = Vector3f.UNIT_X.mult(value * scrollSpeed);
        } else if (name.equals("scrollUp")) {
            vel = Vector3f.UNIT_Y.mult(-value * scrollSpeed);
        } else if (name.equals("scrollDown")) {
            vel = Vector3f.UNIT_Y.mult(value * scrollSpeed);
        }

        camNode.move(vel);
        crosshair.move(vel);
    }
    
}