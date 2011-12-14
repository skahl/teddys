package edu.teddys.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.PssmShadowRenderer;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import edu.teddys.BaseGame;
import edu.teddys.controls.MappingEnum;
import edu.teddys.input.CrosshairControl;
import edu.teddys.input.Cursor;
import edu.teddys.input.PlayerControl;
import edu.teddys.map.GameLoader;
import java.util.logging.Logger;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import edu.teddys.hud.HUDController;

/**
 *
 * @author skahl
 */
public class Game extends AbstractAppState {

  private BaseGame app;
  private AppStateManager stateManager;
  private Logger logger;
  private InputManager inputManager;
  private BulletAppState bulletAppState;
  private Node rootNode;
  private PssmShadowRenderer pssmRenderer; // Shadow rendering
  private GameLoader gameLoader;
  
  private boolean paused;
  private boolean enabled;
  
  
  
  // ActionListener
  private ActionListener actionListener = new ActionListener() {

    public void onAction(String name, boolean keyPressed, float tpf) {
      
    }
  };

  @Override
  public void update(float tpf) {
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(boolean isActive) {
    if (isActive && !this.isEnabled()) {
      // activate

      // here one could attach and detach the whole scene graph
      //this.app.getRootNode().attachChild(rotationNode);

      // attach keys
      initKeys(true);

      this.enabled = true;

    } else if (!isActive && this.isEnabled()) {
      // deactivate
      //this.app.getRootNode().detachChild(rotationNode);
        
      // TODO: Why nullpointer exception on exit?
      stateManager.cleanup();
      rootNode.detachAllChildren();

      // detach keys
      initKeys(false);

      this.enabled = false;
    }
  }

  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app = (BaseGame) app;
    this.stateManager = stateManager;
    this.logger = this.app.getLogger();
    this.inputManager = this.app.getInputManager();
    this.rootNode = this.app.getRootNode();
    
    paused = false;
    enabled = false;

    
    // init physics  renderstate
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    
    // load game
    gameLoader = new GameLoader("firstlevel", "maps/firstlevel.zip", this);
    
    // shed some light
    Vector3f sunDirection = new Vector3f(-1f, -1f, -1.2f);
    sunDirection.normalizeLocal();

    DirectionalLight sunL = new DirectionalLight();
    sunL.setColor(ColorRGBA.White.mult(0.6f));
    sunL.setDirection(sunDirection);
    rootNode.addLight(sunL);

    AmbientLight sunA = new AmbientLight();
    sunA.setColor(ColorRGBA.White.mult(0.3f));
    rootNode.addLight(sunA);
    
    // init shadow renderstate
    pssmRenderer = new PssmShadowRenderer(this.app.getAssetManager(), 512, 2);
    pssmRenderer.setDirection(sunDirection);
    this.app.getViewPort().addProcessor(pssmRenderer);
    
    
    // TODO: Player inklusive Animation und sonstige Texturen der Spielfigur auslagern.
    // dummy player       
    Node playerNode = new Node("Player");

    Box player = new Box(new Vector3f(0,0,0), 0.1f, 0.1f, 0.05f);
    Geometry playerGeom = new Geometry("Player", player);

    Material playerMat = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    playerMat.setColor("Color", ColorRGBA.Blue);
    playerGeom.setMaterial(playerMat);
    
    playerGeom.setShadowMode(ShadowMode.CastAndReceive);

    
    //Player        
        /*Node playerNode = new Node("Player");
        Box player = new Box(Vector3f.ZERO, 0.1f, 0.15f, 0.01f);
        Geometry playerGeom = new Geometry("PlayerGeometry", player);
        
        Material playerMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        playerMat.setColor("Color", ColorRGBA.Blue);
        playerGeom.setMaterial(playerMat);
        
        playerNode.attachChild(playerGeom);
        playerNode.move(0,4,2);
        rootNode.attachChild(playerNode);
    */    
    
    
    CapsuleCollisionShape playerCollisionShape = new CapsuleCollisionShape(0.15f, 0.2f, 1);
    PlayerControl playerControl = new PlayerControl(playerNode, playerCollisionShape, 0.01f);
    playerControl.registerWithInput(inputManager);
    bulletAppState.getPhysicsSpace().add(playerControl);
    
    
    playerControl.setPhysicsLocation(new Vector3f(0f, 0f, -.75f));
    
    // skahl: toying around here...
    playerControl.setJumpSpeed(4f);
    playerControl.setGravity(3f);
    playerControl.setFallSpeed(5f);
    
    
    playerNode.attachChild(playerGeom);
    rootNode.attachChild(playerNode);
    
    //Crosshair
    this.app.getAssetManager().loadTexture("Textures/fadenkreuz.png");

    Cursor cursor = new Cursor("Cursor");
    cursor.setImage(this.app.getAssetManager(), "Textures/fadenkreuz.png", true);
    cursor.setHeight(64);
    cursor.setWidth(64);
    playerNode.attachChild(cursor);
        
        
    //Camera

    CameraNode camNode = new CameraNode("Camera", this.app.getCamera());
    camNode.setControlDir(ControlDirection.SpatialToCamera);

    playerNode.attachChild(camNode);
    
    // initial distance between camera and player
    camNode.move(0, 0, 4);
    
    camNode.lookAt(playerNode.getWorldTranslation(), new Vector3f(0,1,0));
    
    //Input
    CrosshairControl cameraControl = new CrosshairControl(camNode, playerNode, cursor, 
            this.app.getSettings().getWidth(), this.app.getSettings().getHeight());
    cameraControl.registerWithInput(inputManager);


    //PlayerControl playerInput = new PlayerControl(playerNode);
    //playerInput.registerWithInput(inputManager);
    
    
    // TODO: Sollte auch noch schoener irgendwo verpackt werden.
    // load hud
    /*
    NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(this.app.getAssetManager(), 
            inputManager, this.app.getAudioRenderer(), this.app.getGuiViewPort());
    Nifty nifty = niftyDisplay.getNifty();
    nifty.fromXml("Interface/screen.xml", "hud");

    this.app.getGuiViewPort().addProcessor(niftyDisplay);
    

    Element niftyElement = nifty.getCurrentScreen().findElementByName("itemLabel");
    niftyElement.getRenderer(TextRenderer.class).setText("");
    */
    
    /*setDisplayFps(false);
        setDisplayStatView(false);
        
        
        ViewPort niftyView = renderManager.createPreView("NiftyView", cam);
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
        assetManager,  inputManager,  audioRenderer,  niftyView);
        Nifty nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/screen.xml", "hud");
        
        niftyView.addProcessor(niftyDisplay);
        FrameBuffer fb = new FrameBuffer(settings.getWidth(), settings.getHeight(), 0);
        fb.setDepthBuffer(Format.Depth);
        Texture2D niftytex = new Texture2D(settings.getWidth(), settings.getHeight(), Format.RGBA8);
        fb.setColorTexture(niftytex);
        niftyView.setClearFlags(true, false, false);
        niftyView.setOutputFrameBuffer(fb);
        
        Box b = new Box(Vector3f.ZERO, 1f, 0.75f, 0.01f);
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("m_ColorMap", niftytex); 
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        geom.setMaterial(mat);
        camNode.attachChild(geom);
        geom.move(0,0,2);*/
        
        //Test HUD
        /*HUDController hud = HUDController.getInstance();
        hud.init(nifty);
        hud.addMessage("1");
        hud.addMessage("2");
        hud.addMessage("3");
        hud.addMessage("4");
        hud.addMessage("5");
        hud.addMessage("6");*/
    
    
    // physics debug (shows collission meshes):
    //bulletAppState.getPhysicsSpace().enableDebug(this.app.getAssetManager());
  }

  @Override
  public void cleanup() {
    super.cleanup();

    this.setEnabled(false);
  }

  public void initKeys(boolean attach) {

    if (attach) {
      // add key mappings
      //inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
      //inputManager.addMapping(MappingEnum.PARTICLE_TRIGGER.name(), new KeyTrigger(KeyInput.KEY_SPACE));

      // add the action listener
      //inputManager.addListener(actionListener, new String[]{MappingEnum.PARTICLE_TRIGGER.name()});

      // add the analog listener
      //inputManager.addListener(analogListener, new String[]{"ParticleTrigger"});

    } else {
      //inputManager.deleteMapping("Pause");
      //inputManager.deleteMapping(MappingEnum.PARTICLE_TRIGGER.name());
    }

  }

  public void setPaused(boolean paused) {
    if (paused && !this.paused) {
      this.paused = true;

    } else if (!paused && this.paused) {
      this.paused = false;

    }
  }
  
  public BaseGame getApp() {
      return app;
  }
  
  public Logger getLogger() {
      return logger;
  }
  
  public Node getRootNode() {
      return rootNode;
  }
  
  public BulletAppState getBulletAppState() {
      return bulletAppState;
  }
}
