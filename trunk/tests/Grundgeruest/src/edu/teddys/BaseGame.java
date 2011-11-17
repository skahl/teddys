package edu.teddys;

import edu.teddys.states.Pause;
import edu.teddys.states.Menu;
import edu.teddys.states.Game;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.renderer.RenderManager;
import edu.teddys.controls.MappingEnums;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * BaseGame
 * Handles game settings, GameStates, Pausing, HUD, level-loading.
 * @author skahl
 */
public class BaseGame extends SimpleApplication {
    // init java logging
    private static final Logger logger = Logger.getLogger("baseGameLogger");
    
    public ScheduledThreadPoolExecutor threadPool;
    
    
    // ActionListener
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            
            if(name.equals(MappingEnums.MENU.name()) && !keyPressed) {
                if(!stateManager.getState(Menu.class).isEnabled()) {
                    
                    // if a game is running while menu is activated
                    if(stateManager.getState(Game.class).isEnabled()) {
                        // pause the game
                        stateManager.getState(Game.class).setPaused(true);
                    }
                    
                    stateManager.getState(Menu.class).setEnabled(true);
                    
                    logger.log(Level.INFO, "\nMenu State: enabled\n");
                } else {
                    
                    // if a game is running while menu is deactivated
                    if(stateManager.getState(Game.class).isEnabled()) {
                        // unpause the game
                        stateManager.getState(Game.class).setPaused(false);
                    }
                    
                    stateManager.getState(Menu.class).setEnabled(false);
                    
                    logger.log(Level.INFO, "\nMenu State: disabled\n");
                }
            }
        }
    };
    

    
    
    public static void main(String[] args) {
        
        BaseGame app = new BaseGame();
        // TODO: Change the logo of the game
        
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        stateManager.attach(new Menu());
        stateManager.attach(new Game());
        stateManager.attach(new Pause());
        
        // init thread pool with size
        threadPool = new ScheduledThreadPoolExecutor(4);
        
        
        // init keys
        // TODO: InputMapping sollte abhängig vom Gamestate erfolgen!
        initKeys();
        
        // init start state
        stateManager.getState(Menu.class).initialize(stateManager, this);
        stateManager.getState(Menu.class).setEnabled(true);
        logger.log(Level.INFO, "\nMenu State: enabled\n");
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
    }
    
    public void initKeys() {
        // add key mappings
        inputManager.addMapping(MappingEnums.MENU.name(), new KeyTrigger(KeyInput.KEY_M));
        
        // add the action listener
        inputManager.addListener(actionListener, new String[]{MappingEnums.MENU.name()});
        
    }
    
    public ScheduledThreadPoolExecutor getThreadPool() {
        return threadPool;
    }
    
    public static Logger getLogger() {
        return logger;
    }
    
    public void menuFeedback(String feedback) {
        if(feedback.equals("Start Game")) {
            // start game
            if(!stateManager.getState(Game.class).isEnabled()) {
                stateManager.getState(Game.class).initialize(stateManager, this);
                stateManager.getState(Menu.class).setEnabled(false);
                stateManager.getState(Game.class).setEnabled(true);
            }
        }
    }
}
