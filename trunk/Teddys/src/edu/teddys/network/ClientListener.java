/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.math.Vector3f;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.message.DisconnectMessage;
import edu.teddys.MegaLogger;
import edu.teddys.callables.SetPositionOfTeddyCallable;
import edu.teddys.input.ControllerInputListener;
import edu.teddys.network.messages.NetworkMessage;
import edu.teddys.network.messages.NetworkMessageGameState;
import edu.teddys.network.messages.NetworkMessageInfo;
import edu.teddys.network.messages.NetworkMessageManipulation;
import edu.teddys.network.messages.NetworkMessageRequest;
import edu.teddys.network.messages.NetworkMessageResponse;
import edu.teddys.network.messages.client.GSMessageGamePaused;
import edu.teddys.network.messages.client.GSMessagePlayerReady;
import edu.teddys.network.messages.client.ManControllerInput;
import edu.teddys.network.messages.client.ResMessageMapLoaded;
import edu.teddys.network.messages.client.ResMessageSendChecksum;
import edu.teddys.network.messages.client.ResMessageSendClientData;
import edu.teddys.network.messages.server.GSMessageBeginGame;
import edu.teddys.network.messages.server.GSMessageEndGame;
import edu.teddys.network.messages.server.ManMessageActivateItem;
import edu.teddys.network.messages.server.ManMessageSendDamage;
import edu.teddys.network.messages.server.ManMessageSetPosition;
import edu.teddys.network.messages.server.ManMessageTransferPlayerData;
import edu.teddys.network.messages.server.ManMessageTransferServerData;
import edu.teddys.network.messages.server.ManMessageTriggerEffect;
import edu.teddys.network.messages.server.ReqMessageMapRequest;
import edu.teddys.network.messages.server.ReqMessagePauseRequest;
import edu.teddys.network.messages.server.ReqMessagePlayerDisconnect;
import edu.teddys.network.messages.server.ReqMessageRelocateServer;
import edu.teddys.network.messages.server.ReqMessageSendChecksum;
import edu.teddys.network.messages.server.ReqMessageSendClientData;
import edu.teddys.objects.player.Player;
import edu.teddys.states.Game;
import edu.teddys.timer.ChecksumManager;
import edu.teddys.timer.ClientTimer;
import edu.teddys.timer.ServerTimer;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * The listener for clients of our network implementation.
 * 
 * @author cm
 */
public class ClientListener implements MessageListener<com.jme3.network.Client> {

  public void messageReceived(com.jme3.network.Client source, Message message) {
    String inputMessage = String.format(
            "Client received a message (%s): %s",
            message.getClass().getSimpleName(), message);
    MegaLogger.getLogger().debug(inputMessage);

    if (message instanceof DisconnectMessage) {
      //
      // USER HAS BEEN DISCONNECTED/KICKED FROM THE SERVER
      //
      String reason = ((DisconnectMessage) message).getReason();
      String logMsg = "Client has been disconnected from the server!";
      logMsg += (!reason.isEmpty()) ? " Reason: " + reason : "";
      MegaLogger.getLogger().info(new Throwable(logMsg));
      //TODO change game state
    } else if (message instanceof NetworkMessage) {

      // CHECK CLIENT ID
      Integer[] rec = ((NetworkMessage) message).getRecipients();
      if (rec.length != 0 && !(Arrays.asList(rec)).contains(
              TeddyClient.getInstance().getData().getId())) {
        MegaLogger.getLogger().debug("Not a message for me ...");
        return;
      }

      // get the server timestamp
      //TODO check if it is a message from the server!!
      NetworkMessage tempMsg = (NetworkMessage) message;
      if (tempMsg.getServerTimestamp() != null) {
        // set the new timestamp
        ClientTimer.lastServerTimestamp = tempMsg.getServerTimestamp();
      }
      if (message instanceof NetworkMessageInfo) {
        //
        // RECEIVED A SIMPLE MESSAGE
        //
        NetworkMessageInfo info = (NetworkMessageInfo) message;
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
        MegaLogger.getLogger().info(infoString);
      } else if (message instanceof NetworkMessageResponse) {
        if (message instanceof ResMessageSendClientData) {
          //
          // A NEW TEAM MEMBER JOINED THE SERVER
          //
          ResMessageSendClientData msg = (ResMessageSendClientData) message;

        } else if (message instanceof ResMessageMapLoaded) {
          //
          // A USER HAS LOADED THE MAP.
          // ADD THE CLIENT TO THE LOCAL WORLD
          //
          ResMessageMapLoaded msg = (ResMessageMapLoaded) message;
          Player newPlayer = Player.getInstance(msg.getAffected());
          //TODO this should have been done already when the player data was received
          newPlayer.getData().setMapLoaded(true);
          MegaLogger.getLogger().debug("Client " + msg.getAffected() + " has loaded the map.");
          // The position has been set by the server already
          Game.getInstance().addPlayerToWorld(newPlayer);
        }
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
          GSMessageBeginGame msg = (GSMessageBeginGame) message;
          //TODO check if this is the place to add the players ...
          // start the server timer to get the tick
          // if the server is local, don't change the value.
//          if (source.getId() != Player.LOCAL_PLAYER) {
            ServerTimer.setServerTimestamp(msg.getServerTimestamp());
            ServerTimer.startTimer();
//          }
          // start sending input data
          ClientTimer.startTimer();
          // activate the keyboard and mouse listeners
          Game.getInstance().getInputManager().addListener(ControllerInputListener.getInstance());
        } else if (message instanceof GSMessageEndGame) {
          //
          // END OF THE GAME. DISPLAY STATISTICS ...
          //
          //TODO Set game state to "EndGame"
//          ServerTimer.stopTimer();

          Player.getInstance(Player.LOCAL_PLAYER).getData().setReady(false);
          Player.getInstance(Player.LOCAL_PLAYER).getData().setMapLoaded(false);
          // Stop sending the actions to the server
          ClientTimer.stopTimer();
          Game.getInstance().getInputManager().removeListener(ControllerInputListener.getInstance());
          //TODO what about the other players?
        } else if (message instanceof GSMessagePlayerReady) {
          //
          // A PLAYER IS READY TO START THE GAME
          //
          //TODO the status could be updated, but is synced as well. So this can be deleted
//          String teddyName = TeddyServer.getInstance().getClientData(source.getId()).getName();
//          String infoString = String.format("Player %s is ready yet!", teddyName);
//          MegaLogger.getLogger().info(infoString);
//          // Refresh the server data
//          TeddyServer.getInstance().getClientData(source.getId()).setReady(true);
        }
      } else if (message instanceof NetworkMessageManipulation) {
        if (message instanceof ManControllerInput) {
        ManControllerInput input = (ManControllerInput) message;
        // refresh the player's action
        Player.getInstance(input.getSource()).getPlayerControl().newInput(input.getInput());
      } else if (message instanceof ManMessageSetPosition) {
          ManMessageSetPosition pos = (ManMessageSetPosition) message;
          MegaLogger.getLogger().debug("Received a request to change or set the position");
          for(Entry<Integer, Vector3f> entry : pos.getPositions().entrySet()) {
            SetPositionOfTeddyCallable setPos = new SetPositionOfTeddyCallable(
                    Player.getInstance(entry.getKey()),
                    entry.getValue()
                    );
            Game.getInstance().getApp().enqueue(setPos);
          }
//          curPlayer.getPlayerControl().setWalkDirection(pos.getPosition().subtract(curPlayer.getPlayerControl().getPhysicsLocation()).normalize());
        } else if (message instanceof ManMessageActivateItem) {
          //
          // THE USER HAS TO ACTIVATE THE SPECIFIED ITEM YET
          //
          ManMessageActivateItem msg = (ManMessageActivateItem) message;
//        TeddyClient.getInstance().setCurrentItem(msg.getItemName());
          //TODO client has to load and activate the item
        } else if (message instanceof ManMessageSendDamage) {
          //
          // A DAMAGE REQUEST TO BE APPLIED
          //
          ManMessageSendDamage msg = (ManMessageSendDamage) message;
          
          //TODO care about the local damage which has been already calculated!
          
          // Add a painful scar
          Player.getInstance(msg.getDamagedTeddy()).addDamage(msg.getDamage());
          try {
            String goodTeddy = Player.getInstance(Player.LOCAL_PLAYER).getData().getName();
            String badTeddy = Player.getInstance(msg.getSource()).getData().getName();
            String infoString = String.format("Mad Teddy %s attacked 'Good Old %s'! %s: %d",
                    badTeddy, goodTeddy, getDamageMessage(msg.getDamage()), msg.getDamage());
            MegaLogger.getLogger().info(infoString);
          } catch (Exception ex) {
            MegaLogger.getLogger().warn(ex);
          }
        } else if (message instanceof ManMessageTransferPlayerData) {
          //
          // NEW PLAYER DATA AVAILABLE. SYNC
          //
          // (NOTE: CURRENTLY NOT USED)
          //
          ManMessageTransferPlayerData msg = (ManMessageTransferPlayerData) message;
          //TODO overwrite the current player data
//          Player.setInstanceList(msg.getData());
        } else if (message instanceof ManMessageTransferServerData) {
          //
          // NEW SERVER DATA AVAILABLE. SYNC
          //
          ManMessageTransferServerData msg = (ManMessageTransferServerData) message;
          //overwrite the current server data
          TeddyServer.getInstance().setData(msg.getData());
          // update the positions of the clients
          for (Entry<Integer, List<Vector3f>> posPerTeddy : msg.getData().getClientPositions().entrySet()) {
            if (posPerTeddy.getValue().isEmpty()) {
              continue;
            }
            Player curPlayer = Player.getInstance(posPerTeddy.getKey());
            // ... and set the position of the player
            SetPositionOfTeddyCallable setPos = new SetPositionOfTeddyCallable(curPlayer, posPerTeddy.getValue());
            //TODO think about it
//            Game.getInstance().getApp().enqueue(setPos);
          }
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
          // Load the game map
          Game.getInstance().loadGameMap(msg.getLevelName(), msg.getLevelPath());
          // Now that the map is loaded, send the confirmation
          ResMessageMapLoaded mapLoaded = new ResMessageMapLoaded(TeddyClient.getInstance().getData().getId());
          TeddyClient.getInstance().send(mapLoaded);
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
          //TODO Stop the local server
          TeddyServer.getInstance().stopServer();
          // Start the server on the default server port
          TeddyServer.getInstance().startServer(NetworkSettings.SERVER_PORT);
          //TODO Force the clients to join the new server ^^
          MegaLogger.getLogger().debug("Relocated server.");
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
          ResMessageSendClientData response = new ResMessageSendClientData(
                  Player.getInstance(Player.LOCAL_PLAYER).getData());
          TeddyClient.getInstance().send(response);

          //TEMPORARY

          //TODO adapt to the game flow
          GSMessagePlayerReady playerReady = new GSMessagePlayerReady();
          TeddyClient.getInstance().send(playerReady);
        } else if (message instanceof ReqMessagePlayerDisconnect) {
          ReqMessagePlayerDisconnect disconnectMsg = (ReqMessagePlayerDisconnect) message;
          Game.getInstance().removePlayerFromWorld(Player.getInstance(disconnectMsg.getClientID()));
          MegaLogger.getLogger().debug(String.format(
                  "Player %d has been disconnected from the server.", disconnectMsg.getClientID()));
        }
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
      out = "No";
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
      strings = new String[]{"Absurd", "Hysterical", "Bizarre", "Perfect", "Freaky", "Crazy", "Glamorous", "Gorgeous", "Scattering"};
      out = strings[getRandomInt(strings.length)];
    }
    out += " ";
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
