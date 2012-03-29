package edu.teddys;

import edu.teddys.states.Pause;
import edu.teddys.states.Menu;
import edu.teddys.states.Game;
import com.jme3.app.SimpleApplication;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.network.serializing.Serializer;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import edu.teddys.controls.MappingEnum;
import edu.teddys.input.ControllerEvents;
import edu.teddys.input.ControllerInputListener;
import edu.teddys.network.ClientData;
import edu.teddys.network.SessionClientData;
import edu.teddys.network.Team;
import edu.teddys.network.TeddyClient;
import edu.teddys.network.TeddyServer;
import edu.teddys.network.TeddyServerData;
import edu.teddys.network.messages.NetworkMessage;
import edu.teddys.network.messages.NetworkMessageGameState;
import edu.teddys.network.messages.NetworkMessageInfo;
import edu.teddys.network.messages.NetworkMessageManipulation;
import edu.teddys.network.messages.NetworkMessageRequest;
import edu.teddys.network.messages.NetworkMessageResponse;
import edu.teddys.network.messages.client.GSMessageGamePaused;
import edu.teddys.network.messages.client.ManControllerInput;
import edu.teddys.network.messages.client.ResMessageMapLoaded;
import edu.teddys.network.messages.client.ResMessageSendChecksum;
import edu.teddys.network.messages.client.ResMessageSendClientData;
import edu.teddys.input.InputTuple;
import edu.teddys.menu.MenuTypes;
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
import edu.teddys.menu.MainMenu;
import edu.teddys.menu.OptionsMenu;
import edu.teddys.menu.PauseMenu;
import edu.teddys.network.NetworkSettings;
import edu.teddys.network.messages.client.ManCursorPosition;
import edu.teddys.network.messages.server.ReqMessagePlayerDisconnect;
import edu.teddys.network.messages.server.ManMessageTransferPlayerData;
import edu.teddys.objects.player.Player;
import edu.teddys.states.AppStateSwitcher;
import edu.teddys.timer.ServerTimer;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;

/**
 * BaseGame
 * Handles game settings, GameStates, Pausing, HUD, level-loading.
 * @author skahl
 */
public class BaseGame extends SimpleApplication {
  // init java logging

  private NiftyJmeDisplay niftyDisplay;
  private Nifty nifty;
  private boolean createdLockfile = false;
  public ScheduledThreadPoolExecutor threadPool; // Multithreading
  // ActionListener
  private ActionListener actionListener = new ActionListener() {

    public void onAction(String name, boolean keyPressed, float tpf) {

      if (name.equals(MappingEnum.MENU.name()) && !keyPressed) {
        if(AppStateSwitcher.getInstance().isPaused()) {
          AppStateSwitcher.getInstance().unpause(true);
        } else {
          AppStateSwitcher.getInstance().pause(true);
        }
      }
    }
  };

  public static void main(String[] args) {

    BasicConfigurator.configure();
    // get a clean logger
    MegaLogger.getLogger().removeAllAppenders();
    // Set the log level to ALL in order to be informed of all loggable events
    MegaLogger.getLogger().setLevel(Level.INFO);
    PatternLayout basicLayout = new PatternLayout("%d{ISO8601} %-5p (%F:%L): %m%n");
    // Set up a daily log file. If a new day has begun, empty the log file and
    // save it with the specified date format in logs/.
    try {
      DailyRollingFileAppender fileAppender = new DailyRollingFileAppender(basicLayout, "logs/teddys.log", "'.'yyyy-MM-dd_HH");
      fileAppender.setThreshold(Priority.INFO);
      MegaLogger.getLogger().addAppender(fileAppender);
    } catch (IOException ex) {
      MegaLogger.getLogger().error(new Throwable("Creation of the log file appender aborted!", ex));
    }
    // add the console logger with the specified layout
    MegaLogger.getLogger().addAppender(new ConsoleAppender(basicLayout));

    AppSettings settings = new AppSettings(true);

    BaseGame app = new BaseGame();
    app.setPauseOnLostFocus(false);
    settings.setTitle(GameSettings.TITLE);
    settings.setVSync(GameSettings.VSYNC);
    settings.setSamples(GameSettings.MSAA);
    settings.setResolution(GameSettings.WIDTH, GameSettings.HEIGHT);

    app.setSettings(settings);
//    app.setPauseOnLostFocus(false);

    File lockfile = new File(".serverlock");
    if (lockfile.exists()) {
      app.start();
    } else {
//      app.start(JmeContext.Type.Headless);
      app.setShowSettings(false);
      app.start();
    }
  }

  public AppSettings getSettings() {
    return settings;
  }

  @Override
  public void simpleInitApp() {

    // Change the level of some jME log messages to WARNING
    Logger.getLogger("").setLevel(java.util.logging.Level.WARNING);


    setDisplayFps(true);
    setDisplayStatView(true);
    inputManager.setCursorVisible(false);
    rootNode.setShadowMode(ShadowMode.Off);
    renderManager.setAlphaToCoverage(false);

    inputManager.addMapping(MappingEnum.MENU.name(), new KeyTrigger(keyInput.KEY_P));
    inputManager.addListener(actionListener, new String[]{MappingEnum.MENU.name()});
    initGUI();

    stateManager.attach(new Menu());
    stateManager.attach(Game.getInstance());
    stateManager.attach(new Pause());
    
    AppStateSwitcher.getInstance().setManager(stateManager);

    // init thread pool with size
    threadPool = new ScheduledThreadPoolExecutor(6);

    // Post Processing Filter initialization


    Map<String, List<Trigger>> map = ControllerEvents.getAllEvents();
    // add the mapping
    for (Entry<String, List<Trigger>> entry : map.entrySet()) {
      for (Trigger trigger : entry.getValue()) {
        getInputManager().addMapping(entry.getKey(), trigger);
      }
    }

    // init start state
    AppStateSwitcher.getInstance().setApp(this);

    // set the ControllerInputListener as input listener
    getInputManager().addListener(
            ControllerInputListener.getInstance(),
            map.keySet().toArray(new String[map.keySet().size()]));


    // # # # # # # # # # # # # # # NETWORK # # # # # # # # # # # # # # # #

    // Register the network messages at the Serializer
    initSerializer();

    // Create the server "on demand"
    File lockfile = new File(".serverlock");
    if (!lockfile.exists()) {
      flyCam.setEnabled(true);

      AppStateSwitcher.getInstance().activateState(AppStateSwitcher.AppStateEnum.GAME); 
      
      TeddyServer server = TeddyServer.getInstance();
      server.startServer(NetworkSettings.SERVER_PORT);
      server.getData().setDiscoverable(true);
      try {
        lockfile.createNewFile();
        createdLockfile = true;
        MegaLogger.getLogger().debug("Created the lock file for the server instance.");
      } catch (IOException ex) {
        MegaLogger.getLogger().warn("Could not create the lock file for the server instance!");
      }
      // Now start the timer
      ServerTimer.startTimer();
      // Load the map

//      Game.getInstance().loadGameMap("firstlevel", "Models/firstlevel/");
      Game.getInstance().loadGameMap("darkness", "Models/darkness/");

    } else {
      flyCam.setEnabled(false);

      
      AppStateSwitcher.getInstance().activateState(AppStateSwitcher.AppStateEnum.MENU); 
      
      // Get the handle to the client and try to join the specified server
      TeddyClient client = TeddyClient.getInstance();
      MegaLogger.getLogger().info("Client has " + Player.getInstance(Player.LOCAL_PLAYER).getData().getHealth() + " health points at the beginning.");

      // Try to connect to the server
      if (client.join(TeddyClient.getInstance().getServerIP(), NetworkSettings.SERVER_PORT)) {
        MegaLogger.getLogger().debug(String.format("Client started a join request to %s ", TeddyClient.getInstance().getServerIP()));
      } else {
        MegaLogger.getLogger().error("Error while connecting the server!");
        return;
      }
      // Create the listeners for the client
//      client.registerListener(TeddyClient.ListenerFields.health, new HealthListener());
//      client.registerListener(TeddyClient.ListenerFields.isDead, new DeathTest());
    }

    // # # # # # # # # # # # # # # GAME # # # # # # # # # # # # # # # #


  }

  private void initSerializer() {
    // General
    Serializer.registerClass(NetworkMessage.class);
    Serializer.registerClass(NetworkMessageGameState.class);
    Serializer.registerClass(NetworkMessageInfo.class);
    Serializer.registerClass(NetworkMessageManipulation.class);
    Serializer.registerClass(NetworkMessageRequest.class);
    Serializer.registerClass(NetworkMessageResponse.class);
    // Internal data to be serialized
    Serializer.registerClass(SessionClientData.class);
    Serializer.registerClass(ClientData.class);
    Serializer.registerClass(Team.class);
    Serializer.registerClass(Player.class);
    Serializer.registerClass(TeddyServerData.class);
    Serializer.registerClass(InputTuple.class);
    // Client
    Serializer.registerClass(GSMessageGamePaused.class);
    Serializer.registerClass(ManControllerInput.class);
    Serializer.registerClass(ManCursorPosition.class);
    Serializer.registerClass(ResMessageMapLoaded.class);
    Serializer.registerClass(ResMessageSendChecksum.class);
    Serializer.registerClass(ResMessageSendClientData.class);
    // Server
    Serializer.registerClass(GSMessageBeginGame.class);
    Serializer.registerClass(GSMessageEndGame.class);
    Serializer.registerClass(ManMessageActivateItem.class);
    Serializer.registerClass(ManMessageSendDamage.class);
    Serializer.registerClass(ManMessageSetPosition.class);
    Serializer.registerClass(ManMessageTransferPlayerData.class);
    Serializer.registerClass(ManMessageTransferServerData.class);
    Serializer.registerClass(ManMessageTriggerEffect.class);
    Serializer.registerClass(ReqMessageMapRequest.class);
    Serializer.registerClass(ReqMessagePauseRequest.class);
    Serializer.registerClass(ReqMessagePlayerDisconnect.class);
    Serializer.registerClass(ReqMessageRelocateServer.class);
    Serializer.registerClass(ReqMessageSendChecksum.class);
    Serializer.registerClass(ReqMessageSendClientData.class);
  }

  private void initGUI() {
    niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
    nifty = niftyDisplay.getNifty();
    // add the custom appender to react to some infos
    PatternLayout guiLayout = new PatternLayout("%d{ISO8601} %-3p %m%n");
    MegaLogger.getLogger().addAppender(new MegaLoggerListener(guiLayout, nifty));
    guiViewPort.addProcessor(niftyDisplay);

    nifty.addXml("Interface/GUI/MainMenuScreen.xml");
    nifty.addXml("Interface/GUI/PauseScreen.xml");
    nifty.addXml("Interface/GUI/BlankScreen.xml");
    nifty.addXml("Interface/GUI/CreateScreen.xml");
    nifty.addXml("Interface/GUI/CreditsScreen.xml");
    nifty.addXml("Interface/GUI/JoinScreen.xml");
    nifty.addXml("Interface/GUI/OptionsScreen.xml");
    nifty.addXml("Interface/GUI/Popups.xml");
    if (TeddyServer.getInstance().isRunning()) {
      nifty.gotoScreen(MenuTypes.BLANK.name());
//    } else {
//      nifty.gotoScreen(MenuTypes.MAIN_MENU.name());
    }

    ((PauseMenu) nifty.getScreen(MenuTypes.PAUSE_MENU.name()).getScreenController()).setApplication(this);
    ((MainMenu) nifty.getScreen(MenuTypes.MAIN_MENU.name()).getScreenController()).setApplication(this);
    ((OptionsMenu) nifty.getScreen(MenuTypes.OPTIONS_MENU.name()).getScreenController()).setApplication(this);
  }

  public Nifty getNifty() {
    return nifty;
  }

  @Override
  public void simpleUpdate(float tpf) {
  }

  @Override
  public void update() {
    super.update();
  }

  @Override
  public void simpleRender(RenderManager rm) {
  }

  @Override
  public void stop() {
    super.stop();

    if (createdLockfile) {
      TeddyServer.getInstance().stopServer();
      File lockfile = new File(".serverlock");
      lockfile.delete();
      ServerTimer.stopTimer();
    } else {
      if(TeddyClient.getInstance().isCurrentConnection()) {
        TeddyClient.getInstance().disconnect();
      }
    }

    threadPool.shutdown();
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
