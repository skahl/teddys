/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys;

import org.apache.log4j.Logger;

/**
 *
 * @author cm
 */
public class MegaLogger {

    private static Logger logger = Logger.getLogger(BaseGame.class);
    public static MegaLoggerListener listener;

    public static Logger getLogger() {
        return logger;
    }
}
