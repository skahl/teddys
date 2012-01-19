/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.message.DisconnectMessage;
import edu.teddys.BaseGame;
import edu.teddys.MegaLogger;
import edu.teddys.hud.HUDController;
import edu.teddys.network.messages.NetworkMessageGameState;
import edu.teddys.network.messages.NetworkMessageInfo;
import edu.teddys.network.messages.NetworkMessageManipulation;
import edu.teddys.network.messages.NetworkMessageRequest;
import edu.teddys.network.messages.client.GSMessageGamePaused;
import edu.teddys.network.messages.client.GSMessagePlayerReady;
import edu.teddys.network.messages.client.ResMessageSendChecksum;
import edu.teddys.network.messages.client.ResMessageSendClientData;
import edu.teddys.network.messages.server.GSMessageBeginGame;
import edu.teddys.network.messages.server.GSMessageEndGame;
import edu.teddys.network.messages.server.ManMessageActivateItem;
import edu.teddys.network.messages.server.ManMessageSendDamage;
import edu.teddys.network.messages.server.ManMessageTransferServerData;
import edu.teddys.network.messages.server.ManMessageTriggerEffect;
import edu.teddys.network.messages.server.ReqMessageMapRequest;
import edu.teddys.network.messages.server.ReqMessagePauseRequest;
import edu.teddys.network.messages.server.ReqMessageRelocateServer;
import edu.teddys.network.messages.server.ReqMessageSendChecksum;
import edu.teddys.network.messages.server.ReqMessageSendClientData;
import edu.teddys.protection.ChecksumManager;
import java.util.Arrays;

/**
 *
 * The listener for clients of our network implementation.
 * 
 * @author cm
 */
public class ClientListener implements MessageListener<com.jme3.network.Client> {

  public void messageReceived(com.jme3.network.Client source, Message message) {
    MegaLogger.debug("Received a NetworkMessage: " + message.getClass().getName());
    if (message instanceof DisconnectMessage) {
      //
      // USER HAS BEEN DISCONNECTED/KICKED FROM THE SERVER
      //
      String reason = ((DisconnectMessage) message).getReason();
      String logMsg = "Client has been disconnected from the server!";
      logMsg += (!reason.isEmpty()) ? " Reason: " + reason : "";
      MegaLogger.info(new Throwable(logMsg));
      //TODO change game state
    } else if (message instanceof NetworkMessageInfo) {
      //
      // RECEIVED A SIMPLE MESSAGE
      //
      NetworkMessageInfo info = (NetworkMessageInfo) message;
      //TODO check if the client name is displayed as it should be
      //TODO check if the server sent the message
      String teddyName = "";
      try {
        teddyName = TeddyServer.getInstance().getClientData(source.getId()).getName();
      } catch (NullPointerException ex) {
        teddyName = String.valueOf(source.getId());
      }
      String infoString = String.format(
              "Teddy %s says: %s",
              //              info.getTimestamp(),
              teddyName,
              info.getMessage());
      MegaLogger.info(infoString);
    } else if (message instanceof NetworkMessageGameState) {
      if (message instanceof GSMessageGamePaused) {
        //
        // RECEIVED A PAUSE STATUS CHANGE REQUEST
        //
        GSMessageGamePaused msg = (GSMessageGamePaused) message;
        if (msg.isPaused()) {
          //TODO Set game state to "Paused"
        } else {
          //TODO Set game state to "Game"
        }
      } else if (message instanceof GSMessageBeginGame) {
        //
        // START THE ACCEPTED GAME
        //
        //TODO Set game state to "Game"
        //TODO Spawn the position controller
//        SendPositionController.startTimer();
      } else if (message instanceof GSMessageEndGame) {
        //
        // END OF THE GAME. DISPLAY STATISTICS ...
        //
        //TODO Set game state to "EndGame"
      } else if (message instanceof GSMessagePlayerReady) {
        //
        // A PLAYER IS READY TO START THE GAME
        //
        String teddyName = TeddyServer.getInstance().getClientData(source.getId()).getName();
        String infoString = String.format("Player %s is ready yet!", teddyName);
        MegaLogger.info(infoString);
      }
    } else if (message instanceof NetworkMessageManipulation) {
      if (message instanceof ManMessageActivateItem) {
        //
        // THE USER HAS TO ACTIVATE THE SPECIFIED ITEM YET
        //
        ManMessageActivateItem msg = (ManMessageActivateItem) message;
//        TeddyClient.getInstance().setCurrentItem(msg.getItemName());
      } else if (message instanceof ManMessageSendDamage) {
        //
        // A DAMAGE REQUEST TO BE APPLIED
        //
        ManMessageSendDamage msg = (ManMessageSendDamage) message;
        if (msg.getClient().equals(TeddyClient.getInstance().getId())) {
          TeddyClient.getInstance().addDamage(msg.getDamage());
        }
        try {
          String goodTeddy = TeddyServer.getInstance().getClientData(msg.getClient()).getName();
          String badTeddy = TeddyServer.getInstance().getClientData(source.getId()).getName();
          String infoString = String.format("Mad Teddy %s attacked 'Good Old %s'! %s: %s",
                  badTeddy, goodTeddy, getDamageMessage(msg.getDamage()), msg.getDamage());
          MegaLogger.info(infoString);
        } catch (Exception ex) {
        }
      } else if (message instanceof ManMessageTransferServerData) {
        //
        // NEW SERVER DATA AVAILABLE. SYNC
        //
        ManMessageTransferServerData msg = (ManMessageTransferServerData) message;
        //overwrite the current server data
        TeddyServer.getInstance().setData(msg.getData());
      } else if (message instanceof ManMessageTriggerEffect) {
        //
        // CALL THE GIVEN EFFECT
        //
        //TODO call the appropriate effect and game state
      }
    } else if (message instanceof NetworkMessageRequest) {
      if (message instanceof ReqMessageMapRequest) {
        //
        // LOAD THE SPECIFIED MAP
        //
        ReqMessageMapRequest msg = (ReqMessageMapRequest) message;
        //TODO call the map loader
//        GameLoader mapLoader = new GameLoader(null, null, null);
      } else if (message instanceof ReqMessagePauseRequest) {
        //
        //TODO check if GSMessageGamePaused is better ...
        //
        //TODO call the game state "pause"
      } else if (message instanceof ReqMessageRelocateServer) {
        //
        // THE ACTIVE NETWORK SERVER SHOULD BE CHANGED
        //
        ReqMessageRelocateServer msg = (ReqMessageRelocateServer) message;
        if (msg.getDestination().equals(TeddyClient.getInstance().getId())) {
          TeddyServer.getInstance().startServer();
          //TODO Force the clients to join the new server ^^
          MegaLogger.debug("Relocated server.");
        } else {
          //TODO prepare to (seamlessly?) join the new server.
        }
      } else if (message instanceof ReqMessageSendChecksum) {
        //
        // PROTECTION: CHECKSUM REQUEST RECEIVED
        //
        ReqMessageSendChecksum msg = (ReqMessageSendChecksum) message;
        //TODO calculate the checksum (use a dummy value now)
        ResMessageSendChecksum response = new ResMessageSendChecksum(
                msg.getToken(), ChecksumManager.calculateChecksum(msg.getFiles()));
        TeddyClient.getInstance().send(response);
      } else if (message instanceof ReqMessageSendClientData) {
        //
        // CLIENT SYNCHRONISATION REQUEST
        //
        ResMessageSendClientData response = new ResMessageSendClientData(TeddyClient.getInstance().getData());
        TeddyClient.getInstance().send(response);
      }
    }
  }

  /**
   * 
   * Generate a string in the format "(Attribute) Damage" where Attribute is
   * dynamically defined, for example "Enchanting Damage".
   * 
   * @param value The damage of the teddy.
   * @return A string specified above.
   */
  private static String getDamageMessage(Integer value) {
    String suffix = "Damage";
    String out = "";
    String[] strings;
    if (value == 0) {
      out = "No ";
    } else if (value < 20) {
      strings = new String[]{"Laughable", "Ridiculous", "Cheery", "Gay", "Funny", "Mini", "Atomic", "Pintsize", "Tiny little", "Miniscule"};
      out = strings[getRandomInt(strings.length)];
    } else if (value < 40) {
      strings = new String[]{"Amusing", "Comical", "Jokey", "Enjoyable", "Cheery", "Entertaining", "Charming", "Lovely", "Minor", "Negligible"};
      out = strings[getRandomInt(strings.length)];
    } else if (value < 60) {
      strings = new String[]{"Honorable", "Playful", "Strange", "Soulless", "Enchanting", "Magical", "Delightful", "Ravishing"};
      out = strings[getRandomInt(strings.length)];
    } else if (value < 80) {
      strings = new String[]{"Absurd", "Hysterical", "Bizarre", "Freaky", "Crazy", "Glamorous", "Gorgeous", "Scattering"};
      out = strings[getRandomInt(strings.length)];
    }
    return out + suffix;
  }

  /**
   * 
   * Generate a random int value where 0 <= result <= max.
   * 
   * @param max The maximum int value.
   * @return An int value described above.
   */
  private static int getRandomInt(int max) {
    return (int) (Math.random() * max);
  }
}
