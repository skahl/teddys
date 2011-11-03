package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    // init java logging
    private static final Logger logger = Logger.getLogger("MyLogger");
    
    Vector3f location;
    Vector2f camSize;

    public static void main(String[] args) {
        Main app = new Main();
        
        app.start();
    }

    @Override
    public void simpleInitApp() {
        logger.setLevel(Level.ALL);
        
        // Scenery
        // scene size based on the camera's frustum
        camSize = new Vector2f(cam.getFrustumRight()-cam.getFrustumLeft(),
                                cam.getFrustumTop()-cam.getFrustumBottom());
        logger.log(Level.INFO, "Camera's view size: "+String.valueOf(camSize.x)+"/"+String.valueOf(camSize.y));
        
        Quad scene = new Quad(camSize.x, camSize.y);
        Geometry geom = new Geometry("Scenery", scene);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key1 = new TextureKey("Textures/background/sunset.jpg");
        key1.setGenerateMips(true);
        Texture texture1 = assetManager.loadTexture(key1);
        mat1.setTexture("ColorMap", texture1);
        geom.setMaterial(mat1);
        
        rootNode.attachChild(geom);
        
        Box ground = new Box(camSize.x, camSize.y*0.1f, 0.01f);
        Geometry geoGround = new Geometry("Ground", ground);
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key2 = new TextureKey("Textures/ground/woodchips.jpg");
        key2.setGenerateMips(true);
        Texture texture2 = assetManager.loadTexture(key2);
        mat2.setTexture("ColorMap", texture2);
        geoGround.setMaterial(mat2);
        
        rootNode.attachChild(geoGround);
        
        // characters
        
        Quad char1 = new Quad(scene.getWidth()*0.05f, scene.getHeight()*0.05f);
        Geometry geoChar1 = new Geometry("Char1", char1);
        Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key3 = new TextureKey("Textures/char/tweety.jpg");
        key3.setGenerateMips(true);
        Texture texture3 = assetManager.loadTexture(key3);
        mat3.setTexture("ColorMap", texture3);
        geoChar1.setMaterial(mat3);
        
        rootNode.attachChild(geoChar1);
        
        geoChar1.move(scene.getWidth()*0.1f, camSize.y*0.11f, 0.005f);
        
        
        // vars
        location = new Vector3f(scene.getWidth()/2f, scene.getHeight()/2f, cam.getLocation().z);
        
        
        
        
        // A line through the scenery's center
        //Line line = new Line(geom.get)
        
        // Lights
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1,0,-2).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        // directional light not necessary here!
        //rootNode.addLight(sun);        
        
        
        flyCam.setEnabled(false);
        cam.setParallelProjection(true);
        
        // init key mappings
        initInput();

    }
    
    public void initInput() {
        // Name the actions
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("ZoomIn", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("ZoomOut", new KeyTrigger(KeyInput.KEY_O));
        
        
        
        // Add a name to the action listener
        inputManager.addListener(analogListener, new String[]{"Up", "Down", 
                                                            "Left", "Right", 
                                                            "ZoomIn", "ZoomOut"});
    }
    
    private AnalogListener analogListener = new AnalogListener() {
      public void onAnalog(String name, float duration, float tpf) {
          
          // logging example
          logger.log(Level.INFO, "camera location ("+String.valueOf(location.x)+"/"+
                  String.valueOf(location.y)+"/"+
                  String.valueOf(location.z)+")");
          
          if(name.equals("Up")) {
              location.y += 10f*tpf;
          }
          if(name.equals("Down")) {
              location.y -= 10f*tpf;
          }
          if(name.equals("Left")) {
              location.x -= 10f*tpf;
          }
          if(name.equals("Right")) {
              location.x += 10f*tpf;
          }
          if(name.equals("ZoomIn")) {
              location.z += 10f*tpf;
          }
          if(name.equals("ZoomOut")) {
              location.z -= 10f*tpf;
          }
          
      }  
    };

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        
        
        cam.setLocation(location);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
        
    }
}
