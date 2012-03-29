/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.callables;

import com.jme3.light.Light;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import edu.teddys.MegaLogger;
import edu.teddys.states.Game;
import java.util.concurrent.Callable;

/**
 *
 * @author cm
 */
public class AttachLightCallable implements Callable {
  Light light;

  public AttachLightCallable(Light light) {
    if(light == null) {
      throw new ExceptionInInitializerError("Light is null!");
    }
    this.light = light;
  }

  public Object call() {
    Game.getInstance().getRootNode().addLight(light);
    MegaLogger.getLogger().debug(String.format("Light attached to root."));
    return null;
  }
}
