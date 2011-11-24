/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys;

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
  public static final String TITLE = "Teddys (pre-alpha)";
  /**
    * Set multisampling to 0 to switch antialiasing off (harder edges, faster.) 
    * Set multisampling to 2 or 4 to activate antialising (softer edges, may be slower.) 
    * Depending on your graphic card, you may be able to set multisampling to higher values such as 8, 16, or 32 samples.
   */
  public static final Integer MSAA = 2;
  /**
   * Set vertical syncing to true to time the frame buffer to coincide with the refresh frequency of the screen. VSync prevents ugly page tearing artefacts, but is a bit slower; recommened for release build. 
   * Set VSync to false to deactivate vertical syncing (faster, but possible page tearing artifacts); can remain deactivated during development or for slower PCs.
   */
  public static final boolean VSYNC = false;
  /**
   * Activate optional joystick support
   */
  public static final boolean USE_JOYSTICKS = false;
}
