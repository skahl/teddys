/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.callables;

import edu.teddys.hud.HUDController;
import edu.teddys.objects.player.Player;
import java.util.concurrent.Callable;

/**
 *
 * @author cm
 */
public class SetHealthCallable implements Callable {

  private Integer health;

  public SetHealthCallable(Integer health) {
    this.health = health;
  }

  public Object call() {
    HUDController.getInstance().setHealth(health);
    return null;
  }
}
