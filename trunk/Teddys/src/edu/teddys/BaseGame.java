package edu.teddys;

import edu.teddys.states.Pause;
import edu.teddys.states.Menu;
import edu.teddys.states.Game;
import com.jme3.app.SimpleApplication;
import com.jme3.input.controls.ActionListener;
import com.jme3.network.serializing.Serializer;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.system.AppSettings;
import edu.teddys.controls.MappingEnum;
import edu.teddys.network.ClientData;
import edu.teddys.network.DeathTest;
import edu.teddys.network.HealthListener;
import edu.teddys.network.SessionClientData;
import edu.teddys.network.TeddyClient;
import edu.teddys.network.TeddyServer;
import edu.teddys.network.messages.NetworkMessage;
import edu.teddys.network.messages.NetworkMessageInfo;
import edu.teddys.network.messages.client.GSMessageGamePaused;
import edu.teddys.network.messages.client.GSMessagePlayerReady;
import edu.teddys.network.messages.client.ManMessageSendPosition;
import edu.teddys.network.messages.client.ManMessageTriggerWeapon;
import edu.teddys.network.messages.client.ResMessageMapLoaded;
import edu.teddys.network.messages.client.ResMessageSendChecksum;
import edu.teddys.network.messages.client.ResMessageSendClientData;
import edu.teddys.network.messages.server.GSMessageBeginGame;
import edu.teddys.network.messages.server.GSMessageEndGame;
import edu.teddys.network.messages.server.ManMessageActivateItem;
import edu.teddys.network.messages.server.ManMessageSendDamage;
import edu.teddys.network.messages.server.ManMessageSetPosition;
import edu.teddys.network.messages.server.ManMessageTransferServerData;
import edu.teddys.network.messages.server.ManMessageTriggerEffect;
import edu.teddys.network.messages.server.ReqMessageMapRequest;
import edu.teddys.network.messages.server.ReqMessagePauseRequest;
import edu.teddys.network.messages.server.ReqMessageRelocateServer;
import edu.teddys.network.messages.server.ReqMessageSendChecksum;
import edu.teddys.network.messages.server.ReqMessageSendClientData;
import edu.teddys.objects.Jetpack;
import edu.teddys.timer.ChecksumManager;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;

/**
 * BaseGame
 * Handles game settings, GameStates, Pausing, HUD, level-loading.
 * @author skahl
 */
public class BaseGame extends SimpleApplication {
  // init java logging

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

    BasicConfigurator.configure();
    // Set the log level to ALL in order to be informed of all loggable events
    MegaLogger.getLogger().setLevel(Level.ALL);
    PatternLayout layout = new PatternLayout("%d{ISO8601} %-5p [%t] %c: %m%n");
    // Set up a daily log file. If a new day has begun, empty the log file and
    // save it with the specified date format in logs/.
    try {
      DailyRollingFileAppender fileAppender = new DailyRollingFileAppender(layout, "logs/teddys.log", "'.'yyyy-MM-dd_HH");
      MegaLogger.getLogger().addAppender(fileAppender);
    } catch (IOException ex) {
      MegaLogger.getLogger().error(new Throwable("Creation of the log file appender aborted!", ex));
    }
    // add the custom appender to react on some infos
    MegaLogger.getLogger().addAppender(new MegaLoggerListener(layout));

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
    stateManager.attach(Game.getInstance());
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
    
    // Get the handle to the client and try to join the specified server
    TeddyClient client = TeddyClient.getInstance();
    MegaLogger.getLogger().info("Client has " + client.getData().getHealth() + " health points at the beginning.");

    // Trying to connect to the server
    if (client.join()) {
      MegaLogger.getLogger().debug(String.format("Client started a join request to %s ", client.getServerIP()));
    } else {
      MegaLogger.getLogger().error("Error while connecting the server!");
      return;
    }
    
    //TODO send a PlayerReady and MapLoaded message

    // Create the listeners for the client
    client.registerListener(TeddyClient.ListenerFields.health, new HealthListener());
    client.registerListener(TeddyClient.ListenerFields.isDead, new DeathTest());

    // # # # # # # # # # # # # # # GAME # # # # # # # # # # # # # # # #


  }

  private void initSerializer() {
    // General
    Serializer.registerClass(NetworkMessage.class);
    Serializer.registerClass(NetworkMessageInfo.class);
    // Internal data to be serialized
    Serializer.registerClass(SessionClientData.class);
    Serializer.registerClass(Jetpack.class);
    Serializer.registerClass(ClientData.class);
    // Client
    Serializer.registerClass(GSMessageGamePaused.class);
    Serializer.registerClass(GSMessagePlayerReady.class);
    Serializer.registerClass(ManMessageSendPosition.class);
    Serializer.registerClass(ManMessageTriggerWeapon.class);
    Serializer.registerClass(ResMessageMapLoaded.class);
    Serializer.registerClass(ResMessageSendChecksum.class);
    Serializer.registerClass(ResMessageSendClientData.class);
    // Server
    Serializer.registerClass(GSMessageBeginGame.class);
    Serializer.registerClass(GSMessageEndGame.class);
    Serializer.registerClass(ManMessageActivateItem.class);
    Serializer.registerClass(ManMessageSendDamage.class);
    Serializer.registerClass(ManMessageSetPosition.class);
    Serializer.registerClass(ManMessageTransferServerData.class);
    Serializer.registerClass(ManMessageTriggerEffect.class);
    Serializer.registerClass(ReqMessageMapRequest.class);
    Serializer.registerClass(ReqMessagePauseRequest.class);
    Serializer.registerClass(ReqMessageRelocateServer.class);
    Serializer.registerClass(ReqMessageSendChecksum.class);
    Serializer.registerClass(ReqMessageSendClientData.class);
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
