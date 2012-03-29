/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys;

import edu.teddys.controls.PlayerControl;
import edu.teddys.network.NetworkCommunicatorSpidermonkeyClient;
import edu.teddys.network.NetworkCommunicatorSpidermonkeyServer;
import edu.teddys.timer.ChecksumManager;
import edu.teddys.timer.ClientTimer;
import edu.teddys.timer.ServerDataSync;
import edu.teddys.timer.ServerTimer;
import edu.teddys.timer.ServerTimerThread;

/**
 *
 * Provides default settings of the application.
 * 
 * @author cm
 */
public class GameSettings {

  /**
   * The title displayed on the window
   */
  public static final String TITLE = "Teddys";
  /**
   * Set multisampling to 0 to switch antialiasing off (harder edges, faster.) 
   * Set multisampling to 2 or 4 to activate antialising (softer edges, may be slower.) 
   * Depending on your graphic card, you may be able to set multisampling to higher values such as 8, 16, or 32 samples.
   */
  public static final Integer MSAA = 0;
  /**
   * Set vertical syncing to true to time the frame buffer to coincide with the refresh frequency of the screen. VSync prevents ugly page tearing artefacts, but is a bit slower; recommened for release build. 
   * Set VSync to false to deactivate vertical syncing (faster, but possible page tearing artifacts); can remain deactivated during development or for slower PCs.
   */
  public static final boolean VSYNC = true;
  /**
   * Activate optional joystick support
   */
  public static final boolean USE_JOYSTICKS = false;
  /**
   * Resolution Width
   */
  public static final Integer WIDTH = 800;
  /**
   * Resolution Height
   */
  public static final Integer HEIGHT = 600;
  /**
   * Default game mode.
   */
  public static final Class<? extends GameMode> DEFAULT_GAME_MODE = Deathmatch.class;
  /**
   * Shows the meshed of the bounding boxes and increases the verbosity of logs.
   */
  public static final boolean DEBUG = false;
  /**
   * Defined the value regarding the z axis in the 3D world.
   */
  public static final float WORLD_Z_INDEX = -1.2f;
  /**
   * Enable checks of randomly chosen game files.
   */
  public static final boolean ENABLE_CHECKSUM_CHECK = false;
  /**
   * Client update rate per second (Default: 40 -> interval ^= 25 ms)
   * @see ClientTimer
   */
  public static final Integer NETWORK_CLIENT_TIMER_RATE = 40;
  /**
   * The number of rates to shift the user back in time.
   * @see CLIENT_INTERPO_DELAY
   */
  public static final Integer CLIENT_POS_QUEUE_SIZE = 2;
  /**
   * The interpolation delay (in ms) is necessary for the lag compensation, 
   * such as interpolating position changes or time-related calculation 
   * of triggered events etc.
   * 
   * Always go back CLIENT_POS_QUEUE_SIZE steps in the current scene.
   * This allows n-1 packets to get lost.
   * 
   * Default: 2*(1/CLIENT_TIMER_RATE) (-> 100ms)
   */
  public static final Integer CLIENT_INTERPOL_DELAY = (int) (2 * (1f / NETWORK_CLIENT_TIMER_RATE));
  /**
   * The maximum number of client positions for the interpolation of movements.
   * Default: 5 (that means there is a history of 5*(1/CLIENT_TIMER_RATE) ms)
   * (-> 250 ms)
   */
  public static final Integer MAX_CLIENT_INTERPOL_CAPACITY = 5;
  /**
   * Transmit the position of all Teddys every n-th tick. Default: 3
   */
  public static final Integer TRANSMIT_POSITION_MOD = 10;
  /**
   * Low-pass filtered interpolation of the Teddy position in case of deviance.
   */
  public static final Float CLIENT_INTERPOL_SMOOTHING = 0.3f;
  /**
   * Checksum interval in milliseconds (Default: 3000 ms)
   * @see ChecksumManager
   */
  public static final Integer CHECKSUM_INTERVAL = 3000;
  /**
   * Server synchronization interval in milliseconds (Default: 1000 ms)
   * @see ServerDataSync
   */
  public static final Integer NETWORK_SERVER_SYNC_INTERVAL = 1000;
  /**
   * The interval for event calculations in ms.
   * @see ServerTimer
   */
  public static final Integer SERVER_TIMESTAMP_INTERVAL = 15;
  /**
   * The maximum number of positions of the clients (i.e. the #ticks) on the server.
   * 
   * @see ServerTimerThread
   */
  public static final Integer MAX_SERVER_POS_CAPACITY = 20;
  /**
   * Set a delay for testing purposes. This holds back a message for the
   * specified time in ms on the server.
   * @see NetworkCommunicatorSpidermonkeyServer
   */
  public static final Integer NETWORK_SERVER_LAG_DELAY = 0;
  /**
   * Set a delay for testing purposes. This holds back a message for the
   * specified time in ms on the client.
   * @see NetworkCommunicatorSpidermonkeyClient
   */
  public static final Integer NETWORK_CLIENT_LAG_DELAY = 0;
  /**
   * Specify how often isConnected() on a network client should be called.
   * Default: 1000, that means every fifth time isValidConnection() is called,
   * isConnected() is called as well.
   * @see NetworkCommunicatorSpidermonkeyClient
   */
  public static final Integer NETWORK_CLIENT_CON_CHECK = 1000;
  /**
   * The maximum damage which is taken as the base value for the calculation of
   * the damage. Default: 80f
   * @see PlayerControl
   */
  public static final Float DAMAGE_MAX = 50f;
  /**
   * Defined the sigma value for the damage calculation.
   * @see PlayerControl
   */
  public static final Double DAMAGE_SIGMA = 0.05;
}
