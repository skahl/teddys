package mygame;

import com.jme3.app.SimpleApplication;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

import com.jme3.material.Material;
import com.jme3.light.DirectionalLight;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import com.jme3.renderer.RenderManager;

import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Line;
import com.jme3.scene.Node;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    protected Node pivot;
    protected float acceleration;
    protected BitmapText text;
    protected String str;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Nodes
        Node node1 = new Node("Node1");
        Node node2 = new Node("Node2");
        
        // Fonts and Text
        BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        text = new BitmapText(font, false);
        str = new String("Try to keep the speed constant by repeatedly pressing SPACE!\nSpeed (rad per frame): ");
        //text.setBox(new Rectangle(0, 0, settings.getWidth(), settings.getHeight()));
        text.setSize(font.getPreferredSize());
        text.setColor(ColorRGBA.White);
        text.setText(str+String.valueOf(0.0f));
        text.setLocalTranslation(0, settings.getHeight(), 0);
        
        guiNode.attachChild(text);
        
        // Materials
        Material mat_lit = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat_lit.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));
        mat_lit.setTexture("NormalMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond_normal.png"));
        mat_lit.setFloat("Shininess", 5f);
        mat_lit.setBoolean("UseMaterialColors", true);
        mat_lit.setColor("Ambient", ColorRGBA.White);
        mat_lit.setColor("Diffuse", ColorRGBA.White);
        mat_lit.setColor("Specular", ColorRGBA.White);
        
        Material red = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        red.setColor("Color", ColorRGBA.Red);
        
        Material green = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        green.setColor("Color", ColorRGBA.Green);
        
        Material blue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blue.setColor("Color", ColorRGBA.Blue);
        
        // Boxes with Materials
        Sphere sphere1 = new Sphere(32,32,1f);
        sphere1.setTextureMode(Sphere.TextureMode.Projected);
        Geometry geo1 = new Geometry("Sphere1", sphere1);
        geo1.setLocalTranslation(-2f, 0f, 0f);
        geo1.rotate(0f, -FastMath.HALF_PI, 0f);
        geo1.setMaterial(mat_lit);
        geo1.setShadowMode(ShadowMode.CastAndReceive);
        TangentBinormalGenerator.generate(sphere1); // for lighting effect
        node1.attachChild(geo1);
        
        Sphere sphere2 = new Sphere(32,32,1f);
        sphere2.setTextureMode(Sphere.TextureMode.Projected);
        Geometry geo2 = new Geometry("Sphere2", sphere2);
        geo2.setLocalTranslation(2f, 0f, 0f);
        geo2.rotate(0f, FastMath.HALF_PI, 0f);
        geo2.setMaterial(mat_lit);
        geo2.setShadowMode(ShadowMode.CastAndReceive);
        TangentBinormalGenerator.generate(sphere2);
        node2.attachChild(geo2);
        
        
        // Lines (X, Y and Z axis markers) with Materials
        Line x = new Line(new Vector3f(0,0,0), new Vector3f(1,0,0));
        Geometry xLine = new Geometry("xLine", x);
        xLine.setMaterial(red);
        
        Line y = new Line(new Vector3f(0,0,0), new Vector3f(0,1,0));
        Geometry yLine = new Geometry("yLine", y);
        yLine.setMaterial(green);
        
        Line z = new Line(new Vector3f(0,0,0), new Vector3f(0,0,1));
        Geometry zLine = new Geometry("zLine", z);
        zLine.setMaterial(blue);
        
        // Lights
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1,0,-2).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        // Create a pivot node at (0,0,0) and attach to root node
        pivot = new Node("pivot");
        rootNode.attachChild(pivot);
        
        
        // attach geometry boxes and lines to pivot node
        pivot.attachChild(node1);
        pivot.attachChild(node2);
        pivot.attachChild(xLine);
        pivot.attachChild(yLine);
        pivot.attachChild(zLine);
        
        // set initial rotation
        pivot.rotate((float)(Math.PI*0.12f),0.0f,0.0f);
        
        // init acceleration
        acceleration = 1.0f;
        
        
        // init key mappings
        initKeys();
    }
    
    private void initKeys() {
        // Name the actions
        inputManager.addMapping("Accelerate", new KeyTrigger(KeyInput.KEY_SPACE));
        
        // Add a name to the action listener
        inputManager.addListener(actionListener, new String[]{"Accelerate"});
    }
    
    private ActionListener actionListener = new ActionListener() {
      public void onAction(String name, boolean keyPressed, float tpf) {
          if(name.equals("Accelerate")) {
              acceleration += acceleration*.1f;
          }
      }  
    };

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        float speed = acceleration*.5f*tpf;
        
        pivot.rotate(0f, speed, 0f);
        
        Spatial shpere1 = pivot.getChild("Sphere1");
        shpere1.rotate(0f, 0f, -FastMath.PI*speed);
        Spatial shpere2 = pivot.getChild("Sphere2");
        shpere2.rotate(0f, 0f, -FastMath.PI*speed);
        
        acceleration -= acceleration*.5f*tpf;
        
        // update text
        text.setText(str+String.valueOf(speed));
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
