package edu.teddys;

import edu.teddys.states.Pause;
import edu.teddys.states.Menu;
import edu.teddys.states.Game;
import com.jme3.app.SimpleApplication;
import com.jme3.input.controls.ActionListener;
import com.jme3.network.serializing.Serializer;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.system.AppSettings;
import edu.teddys.controls.MappingEnum;
import edu.teddys.input.Position;
import edu.teddys.network.ClientData;
import edu.teddys.network.DeathTest;
import edu.teddys.network.HealthListenerTest;
import edu.teddys.network.SessionClientData;
import edu.teddys.network.TeddyClient;
import edu.teddys.network.TeddyServer;
import edu.teddys.network.messages.NetworkMessage;
import edu.teddys.network.messages.NetworkMessageInfo;
import edu.teddys.network.messages.client.ManMessageTriggerWeapon;
import edu.teddys.network.messages.client.ResMessageSendChecksum;
import edu.teddys.network.messages.client.ResMessageSendClientData;
import edu.teddys.network.messages.server.ManMessageSendDamage;
import edu.teddys.network.messages.server.ReqMessageSendChecksum;
import edu.teddys.network.messages.server.ReqMessageSendClientData;
import edu.teddys.objects.Jetpack;
import edu.teddys.protection.ChecksumManager;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Logger;

/**
 * BaseGame
 * Handles game settings, GameStates, Pausing, HUD, level-loading.
 * @author skahl
 */
public class BaseGame extends SimpleApplication {
  // init java logging

  private static final Logger logger = Logger.getLogger("baseGameLogger"); // Event loggin
  public ScheduledThreadPoolExecutor threadPool; // Multithreading
  
  // ActionListener
  private ActionListener actionListener = new ActionListener() {

    public void onAction(String name, boolean keyPressed, float tpf) {

      if (name.equals(MappingEnum.MENU.name()) && !keyPressed) {
        if (!stateManager.getState(Menu.class).isEnabled()) {

          // if a game is running while menu is activated
          if (stateManager.getState(Game.class).isEnabled()) {
            // pause the game
            stateManager.getState(Game.class).setPaused(true);
          }

          stateManager.getState(Menu.class).setEnabled(true);

        } else {

          // if a game is running while menu is deactivated
          if (stateManager.getState(Game.class).isEnabled()) {
            // unpause the game
            stateManager.getState(Game.class).setPaused(false);
          }

          stateManager.getState(Menu.class).setEnabled(false);

        }
      }
    }
  };

  public static void main(String[] args) {

    AppSettings settings = new AppSettings(true);

    BaseGame app = new BaseGame();
    app.setSettings(settings);
    settings.setTitle(GameSettings.TITLE);
    settings.setVSync(GameSettings.VSYNC);
    settings.setSamples(GameSettings.MSAA);
    settings.setResolution(GameSettings.WIDTH, GameSettings.HEIGHT);
    
    app.start();
  }

  public AppSettings getSettings() {
    return settings;
  }

  @Override
  public void simpleInitApp() {
    
    flyCam.setEnabled(false);
    setDisplayFps(true);
    setDisplayStatView(false);
    inputManager.setCursorVisible(false);
    rootNode.setShadowMode(ShadowMode.Off);
    renderManager.setAlphaToCoverage(false);

    stateManager.attach(new Menu());
    stateManager.attach(new Game());
    stateManager.attach(new Pause());

    // init thread pool with size
    threadPool = new ScheduledThreadPoolExecutor(4);
    
    // Post Processing Filter initialization
    


    // init start state
    stateManager.getState(Game.class).initialize(stateManager, this);
    stateManager.getState(Game.class).setEnabled(true);
    
    
    // # # # # # # # # # # # # # # NETWORK # # # # # # # # # # # # # # # #
    
    // Register the network messages at the Serializer
    initSerializer();
    
    // Create the server
    TeddyServer server = TeddyServer.getInstance();
    server.startServer();
    
    // Start the protection mechanisms
    ChecksumManager.startTimer();
    System.out.println("Checksum timer started.");
    
    // Get the handle to the client and try to join the specified server
    TeddyClient client = TeddyClient.getInstance();
    System.out.println("Client has "+client.getHealth()+" health points at the beginning.");
    
    // Trying to connect to the server
    if(client.join()) {
      System.out.println("Client had tried to join the server ("+client.getServerIP()+")");
      System.out.println("Client ID is "+client.getId());
    } else {
      System.err.println("Error while connecting the server!");
      return;
    }
    
    // Create the listeners for the client
    client.registerListener(TeddyClient.ListenerFields.health, new HealthListenerTest());
    client.registerListener(TeddyClient.ListenerFields.isDead, new DeathTest());
    
    // # # # # # # # # # # # # # # GAME # # # # # # # # # # # # # # # #
    
    
  }
  
  private void initSerializer() {
    Serializer.registerClass(NetworkMessage.class);
    Serializer.registerClass(NetworkMessageInfo.class);
    Serializer.registerClass(ManMessageTriggerWeapon.class);
    Serializer.registerClass(ManMessageSendDamage.class);
    Serializer.registerClass(ReqMessageSendClientData.class);
    Serializer.registerClass(ResMessageSendClientData.class);
    Serializer.registerClass(ReqMessageSendChecksum.class);
    Serializer.registerClass(ResMessageSendChecksum.class);
    Serializer.registerClass(SessionClientData.class);
    Serializer.registerClass(Position.class);
    Serializer.registerClass(Jetpack.class);
    Serializer.registerClass(ClientData.class);
    //TODO add the other ones
  }

  @Override
  public void simpleUpdate(float tpf) {
  }

  @Override
  public void simpleRender(RenderManager rm) {
  }

  @Override
  public void stop() {
    super.stop();

    stateManager.cleanup();

    threadPool.shutdown();
    
    TeddyClient.getInstance().disconnect();
    TeddyServer.getInstance().stopServer();
  }

  public ScheduledThreadPoolExecutor getThreadPool() {
    return threadPool;
  }

  public static Logger getLogger() {
    return logger;
  }

  public void menuFeedback(String feedback) {
    if (feedback.equals("Start Game")) {
      // start game
      if (!stateManager.getState(Game.class).isEnabled()) {
        stateManager.getState(Game.class).initialize(stateManager, this);
        stateManager.getState(Menu.class).setEnabled(false);
        stateManager.getState(Game.class).setEnabled(true);
      }
    }
  }
}
