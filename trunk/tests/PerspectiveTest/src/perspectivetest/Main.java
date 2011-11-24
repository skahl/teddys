package perspectivetest;

import perspectivetest.input.CrosshairControl;
import perspectivetest.input.PlayerControl;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import perspectivetest.input.Cursor;

/**
 * 
 * @author besient
 */
public class Main extends SimpleApplication {


    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        //Background        
        Box background = new Box(new Vector3f(0, 0, 0), 5, 5, 0.1f);
        Geometry backgroundGeom = new Geometry("Background", background);

        Material backgroundMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        backgroundMat.setTexture("ColorMap", assetManager.loadTexture("Textures/bricks.jpg"));
        backgroundGeom.setMaterial(backgroundMat);

        rootNode.attachChild(backgroundGeom);
        
        //Player        
        Node playerNode = new Node("Player");
        
        Box player = new Box(new Vector3f(0,0,0), 0.05f, 0.05f, 0.01f);
        Geometry playerGeom = new Geometry("Player", player);
        
        Material playerMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        playerMat.setColor("Color", ColorRGBA.Blue);
        playerGeom.setMaterial(playerMat);
        
        playerNode.attachChild(playerGeom);
        playerNode.move(0,0,2);
        rootNode.attachChild(playerNode);
        
        //Crosshair
        assetManager.loadTexture("Textures/fadenkreuz.png");
        
        Cursor cursor = new Cursor("Cursor");
        cursor.setImage(assetManager, "Textures/fadenkreuz.png", true);
        cursor.setHeight(64);
        cursor.setWidth(64);
        playerNode.attachChild(cursor);
        
        
        //Camera
        cam.setLocation(Vector3f.ZERO);
                
        CameraNode camNode = new CameraNode("Camera", cam);
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        
        playerNode.attachChild(camNode);
        camNode.move(0,0,2);
        camNode.lookAt(playerNode.getWorldTranslation(), new Vector3f(0,1,0));
        
        flyCam.setEnabled(false);
        
        
        //Input
        CrosshairControl cameraControl = new CrosshairControl(camNode, playerNode, cursor, 
                settings.getWidth(), settings.getHeight());
        cameraControl.registerWithInput(inputManager);
        
        mouseInput.setCursorVisible(false);
        
        PlayerControl playerInput = new PlayerControl(playerNode);
        playerInput.registerWithInput(inputManager);
        
        
        //HUD
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/screen.xml", "hud");
        
        guiViewPort.addProcessor(niftyDisplay);
        setDisplayFps(false);
        setDisplayStatView(false);
        
        Element niftyElement = nifty.getCurrentScreen().findElementByName("itemLabel");
        niftyElement.getRenderer(TextRenderer.class).setText("");
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}