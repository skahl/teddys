/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.states;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import edu.teddys.MegaLogger;
import edu.teddys.network.TeddyClient;
import edu.teddys.network.messages.client.GSMessageGamePaused;

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
  private AppStateEnum activeState = null;

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
        activeState = AppStateEnum.GAME;
        break;
      case MENU:
        manager.getState(Menu.class).setEnabled(true);
        manager.getState(Pause.class).setEnabled(false);
        manager.getState(Game.class).setEnabled(false);
        activeState = AppStateEnum.MENU;
        break;
      case PAUSE:
        manager.getState(Menu.class).setEnabled(false);
        manager.getState(Pause.class).setEnabled(true);
        manager.getState(Game.class).setEnabled(false);
        activeState = AppStateEnum.PAUSE;
    }
    MegaLogger.getLogger().debug("App State switched to " + state.name());
  }

  /**
   * 
   * Returns the currently active AppState as Enum value specified above.
   * 
   * @return The AppState that is active
   */
  public AppStateEnum getActiveState() {
    return activeState;
  }

  public void pause(boolean notify) {
    if(getActiveState().equals(AppStateEnum.PAUSE)) {
      return;
    }
    activateState(AppStateEnum.PAUSE);
    if (notify) {
      GSMessageGamePaused paused = new GSMessageGamePaused(true);
      TeddyClient.getInstance().send(paused);
    }
  }

  public void unpause(boolean notify) {
    if(getActiveState().equals(AppStateEnum.GAME)) {
      return;
    }
    activateState(AppStateEnum.GAME);
    if (notify) {
      GSMessageGamePaused paused = new GSMessageGamePaused(false);
      TeddyClient.getInstance().send(paused);
    }
  }
  
  public boolean isPaused() {
    return activeState.equals(AppStateEnum.PAUSE);
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
