package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.renderer.RenderManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.shadow.PssmShadowRenderer;
import java.util.logging.Logger;

public class Main extends SimpleApplication {
    
    private static final Logger logger = Logger.getLogger("baseGameLogger");
    private BulletAppState bulletAppState;
    private PssmShadowRenderer pssmRenderer;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        renderManager.setAlphaToCoverage(true);
        pssmRenderer = new PssmShadowRenderer(assetManager, 1024, 3);
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        GameLoader mapLoader = new GameLoader("firstlevel", "maps/firstlevel.zip", this);
        
        // set camera
        this.getCamera().setLocation(new Vector3f(0f, 0f, -3f));
        this.getCamera().lookAtDirection(Vector3f.UNIT_Z, Vector3f.UNIT_Y);
        
        // init lights
        Vector3f sunDirection = new Vector3f(1f, -1f, 2f);
        sunDirection.normalizeLocal();
        
        DirectionalLight sunL = new DirectionalLight();
        sunL.setColor(ColorRGBA.White.mult(2f));
        sunL.setDirection(sunDirection);
        rootNode.addLight(sunL);
        
        pssmRenderer.setDirection(sunDirection);
        
        //viewPort.addProcessor(pssmRenderer);
    }
    
    
    public static Logger getLogger() {
        return logger;
    }

    @Override
    public void simpleUpdate(float tpf) {
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        
    }
    
    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }
}
