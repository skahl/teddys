package edu.teddys.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.shadow.BasicShadowRenderer;
import edu.teddys.BaseGame;
import edu.teddys.GameModeEnum;
import edu.teddys.MegaLogger;
import edu.teddys.hud.HUD;
import edu.teddys.hud.HUDController;
import edu.teddys.input.CrosshairControl;
import edu.teddys.input.Cursor;
import edu.teddys.map.GameLoader;
import edu.teddys.objects.player.Player;
import java.util.Random;

/**
 *
 * @author skahl
 */
public class Game extends AbstractAppState {

    private static Game instance;
    private BaseGame app;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private BulletAppState bulletAppState;
    private Node rootNode;
    private BasicShadowRenderer shadowRenderer; // Shadow rendering
    private GameLoader gameLoader;
    public static HUD hud;
    private Cursor cursor;
    private boolean paused;
    //private boolean enabled;

    protected Game() {
        super();
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }
    // ActionListener
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean keyPressed, float tpf) {
        }
    };

    @Override
    public void update(float tpf) {
    }

//  @Override
//  public boolean isEnabled() {
//    return enabled;
//  }
    @Override
    public void setEnabled(boolean isActive) {
        if (isActive && !this.isEnabled()) {
            // activate

            // here one could attach and detach the whole scene graph
            //this.app.getRootNode().attachChild(rotationNode);

            // attach keys
            initKeys(true);
            hud.show();
            super.setEnabled(true);

        } else if (!isActive && this.isEnabled()) {
            // deactivate
            //this.app.getRootNode().detachChild(rotationNode);

            // TODO: Why nullpointer exception on exit?
            stateManager.cleanup();
            rootNode.detachAllChildren();

            // detach keys
            initKeys(false);
            hud.hide();
            super.setEnabled(false);
        }
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (BaseGame) app;
        this.stateManager = stateManager;
        this.inputManager = this.app.getInputManager();
        this.rootNode = this.app.getRootNode();

        app.getViewPort().setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1f));

        this.paused = false;
        //this.enabled = false;


        // init physics  renderstate
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // load game
        gameLoader = new GameLoader("firstlevel", "maps/firstlevel.zip", this);


        // shed some light
        Vector3f sunDirection = new Vector3f(-1f, -1f, -1.2f);
        sunDirection.normalizeLocal();

        DirectionalLight sunL = new DirectionalLight();
        sunL.setColor(ColorRGBA.White.mult(0.5f));
        sunL.setDirection(sunDirection);
        rootNode.addLight(sunL);

        AmbientLight sunA = new AmbientLight();
        sunA.setColor(ColorRGBA.White.mult(0.3f));
        rootNode.addLight(sunA);

        // init shadow renderstate
        shadowRenderer = new BasicShadowRenderer(this.app.getAssetManager(), 256);
        shadowRenderer.setDirection(sunDirection);
        this.app.getViewPort().addProcessor(shadowRenderer);


        //HUD
        hud = HUD.getInstance(this.app.getGuiNode(),
                this.app.getAssetManager(),
                this.app.getSettings().getWidth(),
                this.app.getSettings().getHeight(), GameModeEnum.DEATHMATCH);

        HUDController hudController = HUDController.getInstance();
        hudController.setHUD(hud);
        hudController.registerWithInput(inputManager);

        // init player
//    player.getPlayerControl().setPhysicsLocation(new Vector3f(1f, 0f, -1.2f));
//    
//    Player player2 = Player.getInstance(1);
//    player2.getPlayerControl().setPhysicsLocation(new Vector3f(0f, 0f, -1.2f));

//    rootNode.attachChild(player.getNode());
//    rootNode.attachChild(player2.getNode());

        // Crosshair
        int crosshairSize = this.app.getSettings().getHeight() / 15;
        this.app.getAssetManager().loadTexture("Interface/HUD/crosshair.png");

        cursor = Cursor.getInstance("Cursor");
        cursor.setImage(this.app.getAssetManager(), "Interface/HUD/crosshair.png", true);
        cursor.getMaterial().getAdditionalRenderState().setAlphaTest(true);
        cursor.setHeight(crosshairSize);
        cursor.setWidth(crosshairSize);
        this.app.getGuiNode().attachChild(cursor);

        // Camera
        CameraNode camNode = new CameraNode("Camera", this.app.getCamera());
        camNode.setControlDir(ControlDirection.SpatialToCamera);

        Player player = Player.getInstance(Player.LOCAL_PLAYER);
        player.getNode().attachChild(camNode);

        // initial distance between camera and player
        camNode.move(0, 1, 8);

        Vector3f dir = player.getNode().getWorldTranslation().add(0, 0.75f, 0);
        camNode.lookAt(dir, new Vector3f(0, 1, 0));

        // Input
        CrosshairControl cameraControl = new CrosshairControl(camNode, player, cursor,
                this.app.getSettings().getWidth(), this.app.getSettings().getHeight());
        cameraControl.registerWithInput(inputManager);


        // physics debug (shows collission meshes):

        //bulletAppState.getPhysicsSpace().enableDebug(this.app.getAssetManager());

        addPlayerToWorld(Player.getInstance(1));

    }

    public void addPlayerToWorld(Player player) {
        getRootNode().attachChild(player.getNode());
        MegaLogger.getLogger().debug("Added player to the world.");
        Random rnd = new Random();
        //TODO set to the maximum dimension of the world
        Vector3f pos = new Vector3f(rnd.nextFloat() * 6, rnd.nextFloat() * 2, -1.2f);
        player.getPlayerControl().setPhysicsLocation(pos);
        MegaLogger.getLogger().debug("Position of the player has been set: " + pos);
    }

    public void removePlayerFromWorld(Player player) {
        if (getRootNode().hasChild(player.getNode())) {
            getRootNode().detachChild(player.getNode());
            MegaLogger.getLogger().debug("Player removed from the world.");
        }
    }

    @Override
    public void cleanup() {
        super.setEnabled(false);
        super.cleanup();


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

            this.app.getGuiNode().detachChildNamed("Cursor");
            hud.hide();

        } else if (!paused && this.paused) {
            this.paused = false;

            hud.show();
            this.app.getGuiNode().attachChild(cursor);
        }
    }

    public BaseGame getApp() {
        return app;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    public AssetManager getAssetManager() {
        return app.getAssetManager();
    }

    public InputManager getInputManager() {
        return inputManager;
    }
}
