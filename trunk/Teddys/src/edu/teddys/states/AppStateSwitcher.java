/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.states;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import edu.teddys.MegaLogger;

/**
 * Used for convenient change of game states
 * @author besient
 */
public class AppStateSwitcher {

  public enum AppStateEnum {

    GAME,
    PAUSE,
    MENU
  };
  private static AppStateSwitcher instance = null;
  private AppStateManager manager;
  private Application app;

  private AppStateSwitcher() {
  }

  /**
   * Activates the given state, deactivates all others.
   * @param state The state to be activated
   */
  public void activateState(AppStateEnum state) {
    switch (state) {
      case GAME:
        manager.getState(Menu.class).setEnabled(false);
        manager.getState(Pause.class).setEnabled(false);
        manager.getState(Game.class).setEnabled(true);
        break;
      case MENU:
        manager.getState(Menu.class).setEnabled(true);
        manager.getState(Pause.class).setEnabled(false);
        manager.getState(Game.class).setEnabled(false);
        break;
      case PAUSE:
        manager.getState(Menu.class).setEnabled(false);
        manager.getState(Pause.class).setEnabled(true);
        manager.getState(Game.class).setEnabled(false);
    }
    MegaLogger.getLogger().debug("App State switched to " + state.name());
  }

  /**
   * Gets the given state from the AppStateManager.
   * @param state
   * @return 
   */
  public AppState getState(AppStateEnum state) {
    AppState returnState = null;
    switch (state) {
      case PAUSE:
        returnState = manager.getState(Pause.class);
        break;
      case MENU:
        returnState = manager.getState(Menu.class);
        break;
      case GAME:
        returnState = manager.getState(Game.class);
    }

    return returnState;
  }

//    public void unpause() {
//        manager.getState(Pause.class).setEnabled(false);
//        manager.getState(Game.class).setPaused(false);
//    }
//  /**
//   * Used to obtain the singleton object.
//   * @param manager
//   * @return 
//   */
//  public static AppStateSwitcher getInstance(AppStateManager manager) {
//    if (instance == null) {
//      instance = new AppStateSwitcher(manager);
//    }
//    return instance;
//  }
  /**
   * Used to obtain the singleton object.
   * @param manager
   * @return 
   */
  public static AppStateSwitcher getInstance() {
    if (instance == null) {
      instance = new AppStateSwitcher();
    }
    return instance;
  }

  public void setManager(AppStateManager manager) {
    this.manager = manager;
  }

  public void setApp(Application app) {
    this.app = app;
    // initialize the GameStates
    if (manager == null) {
      MegaLogger.getLogger().fatal("Could not initialize the GameStates because the AppStateManager is null!");
      return;
    }
    manager.getState(Game.class).initialize(manager, app);
    manager.getState(Pause.class).initialize(manager, app);
    manager.getState(Menu.class).initialize(manager, app);
  }
}
