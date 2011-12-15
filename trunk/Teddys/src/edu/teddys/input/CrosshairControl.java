package edu.teddys.input;

import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.FastMath;
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

    private float scrollSpeed = 0.7f;
    private float mouseSpeed = 500f;
    
    private final Vector3f left = new Vector3f(1,0,0);
    private final Vector3f right = new Vector3f(-1,0,0);
    private final Vector3f top = new Vector3f(0,-1,0);
    private final Vector3f bottom = new Vector3f(0,1,0);    
    
    private InputManager input;
    private Camera cam;

    private CameraNode camNode;
    private Spatial player;
    private Cursor crosshair;
    private float minCamX, maxCamX, minCamY, maxCamY, minMouseX, minMouseY, maxMouseX, maxMouseY, width, height;

    
    
    public CrosshairControl(CameraNode camNode, Spatial player, Cursor crosshair, int width, int height) {

        this.camNode = camNode;
        cam = camNode.getCamera();
        this.player = player;
        this.crosshair = crosshair;
        this.width = width;
        this.height = height;
        
        minCamX = width * 0.1f;
        maxCamX = width * 0.9f;
        minCamY = height * 0.1f;
        maxCamY = height * 0.9f;
        minMouseX = 0;
        maxMouseX = width - crosshair.getWidth();
        minMouseY = 0;
        maxMouseY = height - crosshair.getHeight();      
        
        crosshair.setPosition(width/2 - crosshair.getWidth()/2, height/2 - crosshair.getHeight()/2);
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
    
    public void setMouseBorders(float border) {
        minMouseX = border * width;
        maxMouseX = (1 - border) * width - crosshair.getWidth();
        minMouseY = border * height;
        maxMouseY = (1 - border) * height - crosshair.getHeight(); 
    }
    
    public void setCamBorders(float border) {
        minCamX = border * width;
        maxCamX = (1 - border) * width;
        minCamY = border * height;
        maxCamY = (1 - border) * height;
    }

    public void setScrollSpeed(float scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }
    
    public void setMouseSpeed(float mouseSpeed) {
        this.mouseSpeed = mouseSpeed;
    }
    
    public void onAnalog(String name, float value, float tpf) {
        
        Vector3f mouseVel = new Vector3f();  
        Vector3f camVel = new Vector3f();   

       
        if (name.equals("scrollLeft")) {
            mouseVel = Vector3f.UNIT_X.mult(-value * mouseSpeed);
            camVel = Vector3f.UNIT_X.mult(-value * scrollSpeed);
        } else if (name.equals("scrollRight")) {
            mouseVel = Vector3f.UNIT_X.mult(value * mouseSpeed);
            camVel = Vector3f.UNIT_X.mult(value * scrollSpeed);
        } else if (name.equals("scrollUp")) {
            mouseVel = Vector3f.UNIT_Y.mult(-value * mouseSpeed);
            camVel = Vector3f.UNIT_Y.mult(-value * scrollSpeed);
        } else if (name.equals("scrollDown")) {
            mouseVel = Vector3f.UNIT_Y.mult(value * mouseSpeed);
            camVel = Vector3f.UNIT_Y.mult(value * scrollSpeed);
        }
        
        Vector2f newPos = crosshair.getPosition().add(new Vector2f(mouseVel.x, mouseVel.y));
        
        newPos.x = FastMath.clamp(newPos.x, minMouseX, maxMouseX);
        newPos.y = FastMath.clamp(newPos.y, minMouseY, maxMouseY);
        
        crosshair.setPosition(newPos.x, newPos.y);
        
        boolean move = true;
        Vector3f playerPos = cam.getScreenCoordinates(player.getWorldTranslation());

        
        if (playerPos.x < minCamX) 
            if (left.dot(camVel) > 0) 
                move = false;            
        
        if (playerPos.x > maxCamX) 
            if (right.dot(camVel) > 0)
                move = false;            
        
        if (playerPos.y < minCamY) 
            if (bottom.dot(camVel) > 0)
                move = false;            
        
        if (playerPos.y > maxCamY) 
            if (top.dot(camVel) > 0)
                move = false;            
        
        
        if (move) 
            camNode.move(camVel); 
        
    }

    
    
}