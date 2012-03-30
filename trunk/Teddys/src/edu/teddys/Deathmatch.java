/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.SessionDataFieldsEnum;
import edu.teddys.network.TeddyServer;
import edu.teddys.network.messages.NetworkMessageInfo;
import edu.teddys.objects.player.Player;

/**
 * 
 * TODO There should be a maximum number of kills/deaths ...
 *
 * @author cm
 */
@Serializable
public class Deathmatch extends GameMode {

  public Deathmatch() {
    minutesToPlay = 30;
    modeEnum = GameModeEnum.DEATHMATCH;
  }

  public void valueChanged(Integer playerId, SessionDataFieldsEnum fieldName, Integer oldValue, Integer newValue) {
    if (fieldName.equals(SessionDataFieldsEnum.kills) && newValue > 3) {
      String msg = String.format(
              "I can't believe it! %s got %d kills! HE! IS! AWESOME!",
              Player.getInstance(playerId).getData().getName(),
              newValue);
      NetworkMessageInfo info = new NetworkMessageInfo(msg);
      TeddyServer.getInstance().send(info);
      MegaLogger.getLogger().debug(msg);
    } else if(fieldName.equals(SessionDataFieldsEnum.deaths) && newValue > 5) {
      String msg = String.format(
              "D'ou! It's the %d-th death of %s.",
              newValue,
              Player.getInstance(playerId).getData().getName());
      NetworkMessageInfo info = new NetworkMessageInfo(msg);
      TeddyServer.getInstance().send(info);
      MegaLogger.getLogger().debug(msg);
    }
  }
}
