/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys;

import edu.teddys.hud.HUDController;
import org.apache.log4j.Logger;

/**
 *
 * @author cm
 */
public class MegaLogger {

    private static Logger logger = Logger.getLogger(BaseGame.class);

    public static Logger getLogger() {
        return logger;
    }

    // printing methods:
    public static void trace(Object message) {
        logger.trace(message);
    }

    public static void debug(Object message) {
        logger.debug(message);
    }

    /**
     * 
     * Show the specified message as a popup if the client is in a menu or as
     * HUD message if the player is currently in a game.
     * 
     * @param message 
     */
    public static void info(Object message) {
        logger.info(message);
        //TODO DO it!
        HUDController.getInstance().addMessage(getMessageFromObject(message));
    }

    public static void warn(Object message) {
        logger.warn(message);
        // display a message?
        
    }

    public static void error(Object message) {
        logger.error(message);
        // display a message?
        
    }

    public static void fatal(Throwable message) {
        logger.fatal(message);
        // display a message?
        
    }

    private static String getMessageFromObject(Object message) {
        String out = null;
        if (message instanceof Throwable) {
            Throwable temp = (Throwable) message;
            out = temp.getLocalizedMessage();
            if (temp.getCause() != null) {
                out += "\nGrund: " + temp.getCause().getClass().getName();
                out += "\n(" + temp.getCause().getLocalizedMessage() + ")";
            }
        } else if (message instanceof String) {
            out = (String) message;
        }
        return out;
    }
}
