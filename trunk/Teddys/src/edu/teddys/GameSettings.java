/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys;

import edu.teddys.network.TeddyServerData;
import edu.teddys.timer.ChecksumManager;
import edu.teddys.timer.ClientTimer;
import edu.teddys.timer.ServerDataSync;
import edu.teddys.timer.ServerTimer;

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
  public static final String TITLE = "Teddys (pre-beta)";
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
   * Client update rate per second (Default: 20 -> interval ^= 50 ms)
   * @see ClientTimer
   */
  public static final Integer CLIENT_TIMER_RATE = 20;
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
  public static final Integer CLIENT_INTERPOL_DELAY = (int) (2 * (1f / CLIENT_TIMER_RATE));
  /**
   * The maximum number of client positions for the interpolation of movements.
   * Default: 5 (that means there is a history of 5*(1/CLIENT_TIMER_RATE) ms)
   * (-> 250 ms)
   */
  public static final Integer MAX_CLIENT_INTERPOL_CAPACITY = 5;
  /**
   * Checksum interval in milliseconds (Default: 3000 ms)
   * @see ChecksumManager
   */
  public static final Integer CHECKSUM_INTERVAL = 3000;
  /**
   * Server synchronization interval in milliseconds (Default: 2000 ms)
   * @see ServerDataSync
   */
  public static final Integer SERVER_SYNC_INTERVAL = 2000;
  /**
   * The interval for event calculations in ms.
   * @see ServerTimer
   */
  public static final Integer SERVER_TIMESTAMP_INTERVAL = 15;
  /**
   * The maximum number of client positions on the server.
   * @see TeddyServerData
   */
  public static final Integer MAX_SERVER_POS_CAPACITY = 10;
}
