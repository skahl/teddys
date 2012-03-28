/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import edu.teddys.MegaLogger;
import edu.teddys.network.messages.NetworkMessageGameState;
import edu.teddys.network.messages.NetworkMessageInfo;
import edu.teddys.network.messages.NetworkMessageManipulation;
import edu.teddys.network.messages.NetworkMessageResponse;
import edu.teddys.network.messages.client.GSMessageGamePaused;
import edu.teddys.network.messages.client.ResMessageMapLoaded;
import edu.teddys.network.messages.client.GSMessagePlayerReady;
import edu.teddys.network.messages.client.ManControllerInput;
import edu.teddys.network.messages.client.ManCursorPosition;
import edu.teddys.network.messages.client.ResMessageSendChecksum;
import edu.teddys.network.messages.client.ResMessageSendClientData;
import edu.teddys.network.messages.server.GSMessageBeginGame;
import edu.teddys.network.messages.server.ManMessageTransferPlayerData;
import edu.teddys.network.messages.server.ManMessageTransferServerData;
import edu.teddys.network.messages.server.ReqMessageMapRequest;
import edu.teddys.objects.player.Player;
import edu.teddys.states.Game;
import edu.teddys.timer.ChecksumManager;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

/**
 * 
 * Since clients are able to submit messages to the server, this
 * class is used to fetch these ones.
 *
 * @author cm
 */
public class ServerListener implements MessageListener<HostedConnection> {

  public void messageReceived(HostedConnection source, Message message) {

    if (!(message instanceof ManControllerInput)) {
      String inputMessage = String.format(
              "Server received a message (%s): %s",
              message.getClass().getSimpleName(), message);
      MegaLogger.getLogger().debug(inputMessage);
    }

    if (message instanceof NetworkMessageInfo) {
      //
      // RECEIVED A SIMPLE MESSAGE
      //
      NetworkMessageInfo info = (NetworkMessageInfo) message;
      // Distribute to the other clients
      TeddyServer.getInstance().send(info);
    } else if (message instanceof NetworkMessageGameState) {
      if (message instanceof GSMessageGamePaused) {
        //
        // USER HAS CHANGED HIS PAUSE STATUS
        //
        // Just distribute the message to the other clients
        GSMessageGamePaused msg = (GSMessageGamePaused) message;
        TeddyServer.getInstance().send(msg);
      } else if (message instanceof GSMessagePlayerReady) {
        //
        // USER ACCEPTED THE GAME START REQUEST
        //
        // Re-distribute info to the other clients
        GSMessagePlayerReady msg = (GSMessagePlayerReady) message;
        TeddyServer.getInstance().send(msg);
//        TeddyServer.getInstance().getClientData(source.getId()).setReady(true);
        Player.getInstance(source.getId()).getData().setReady(true);

        //TODO change to the chosen map name
        Entry<String, String> levelData = Game.getInstance().getLevelData();
        ReqMessageMapRequest mapRequest = new ReqMessageMapRequest(levelData.getKey(), levelData.getValue());
        TeddyServer.getInstance().send(mapRequest);
      }
    } else if (message instanceof NetworkMessageResponse) {
      if (message instanceof ResMessageSendChecksum) {
        //
        // PROTECTION: USER SENT HIS CHECKSUM
        //
        ResMessageSendChecksum msg = (ResMessageSendChecksum) message;
        // Check the transmitted checksum for some files ...
        MegaLogger.getLogger().debug("User submitted " + msg.getChecksum() + " for token " + msg.getToken());
        try {
          ChecksumManager.checkChecksum(msg.getToken(), msg.getChecksum());
        } catch (VerifyError error) {
          TeddyServer.getInstance().disconnect(source.getId(), error.getLocalizedMessage());
        }
      } else if (message instanceof ResMessageMapLoaded) {
        //
        // CLIENT HAS JUST LOADED A MAP
        //
        // Distribute to all clients
        ResMessageMapLoaded msg = (ResMessageMapLoaded) message;
        TeddyServer.getInstance().send(msg);
        Player newPlayer = Player.getInstance(source.getId());
        MegaLogger.getLogger().debug("Trying to add Player to the world, having the ID: "+String.valueOf(source.getId()));
        newPlayer.getData().setMapLoaded(true);
        // add the player to the game world (if not already joined?)
        Game.getInstance().addPlayerToWorld(newPlayer);
        Game.getInstance().setRandomPlayerPosition(newPlayer);
        // refresh the clients' positions
        ManMessageTransferPlayerData playerData = new ManMessageTransferPlayerData(Player.getInstances());
        TeddyServer.getInstance().send(playerData);
        // If all players have loaded the map, "I wanna play a game with you".
        int numPlayers = 0;
        for (Player player : Player.getInstanceList()) {
          if (player.getData().isReady() && player.getData().isMapLoaded()) {
            numPlayers++;
          }
        }

        //TODO now that the player has been added to the world, send him a message
        // that the game has begun yet

//        ServerDataSync.startTimer();

//        if(numPlayers == TeddyServer.getInstance().getConnections().size()) {
//          ServerTimer.startTimer();
//          //TODO check when the game has ended! the timer must be stopped!
//          // Now start a game
        GSMessageBeginGame beginGame = new GSMessageBeginGame(source.getId());
        TeddyServer.getInstance().send(beginGame);

//        }
      } else if (message instanceof ResMessageSendClientData) {
        //
        // RECEIVED USER DATA
        //
        ResMessageSendClientData msg = (ResMessageSendClientData) message;
        // Tell the server to update the data
        ClientData data = msg.getClientData();
        Integer clientID = source.getId();
        // Get the player object
        Player player = Player.getInstance(clientID);
        // Update the data
        player.setData(data);
        //TODO Add member to a team

        //TEST
        Team newTeam = new Team(Color.BLUE, "Molche");
        if (TeddyServer.getInstance().getData().getTeams().isEmpty()) {
          // Create a new team
          TeddyServer.getInstance().getData().getTeams().add(newTeam);
        }

        Integer teamId = 0;
        // Add the player to the team
        TeddyServer.getInstance().getData().getTeams().get(teamId).addPlayer(clientID);
        // Refresh the teamID
        player.getData().setTeam(teamId);
        msg.getClientData().setTeam(teamId);
        NetworkMessageInfo teamInfoMsg = new NetworkMessageInfo(
                "Teddy has sent his client data. " + data.getName()
                + " belongs to the team " + newTeam.getName() + "!");
        TeddyServer.getInstance().send(teamInfoMsg);

        // Get the other clients
        List<Integer> rec = new ArrayList<Integer>(Player.getInstances().keySet());
        // Remove the current clientID
        rec.remove(clientID);
        // Refresh the recipient list
        msg.setRecipients(rec.toArray(new Integer[rec.size()]));
        // And send the data to the clients
        TeddyServer.getInstance().send(msg);

        // Send the data of the "old" clients to the new member
        for (Player playerInstance : Player.getInstanceList()) {
          if (playerInstance.getData().getId() == clientID) {
            continue;
          }
          ResMessageSendClientData tmp = new ResMessageSendClientData(clientID, playerInstance.getData());
          TeddyServer.getInstance().send(tmp);
          // Since the active players have loaded the map, send an info to the new one
          ResMessageMapLoaded tmpMapLoaded = new ResMessageMapLoaded(new Integer[]{clientID}, playerInstance.getData().getId());
          TeddyServer.getInstance().send(tmpMapLoaded);
        }

        // Now update the player data on the clients
//        ManMessageTransferPlayerData playerDataMsg = new ManMessageTransferPlayerData(Player.getInstances());
//        TeddyServer.getInstance().send(playerDataMsg);
        ManMessageTransferServerData serverDataMsg = new ManMessageTransferServerData(TeddyServer.getInstance().getData());
        TeddyServer.getInstance().send(serverDataMsg);
      }
    } else if (message instanceof NetworkMessageManipulation) {
      if (message instanceof ManControllerInput) {
        ManControllerInput input = (ManControllerInput) message;

        // refresh the player's action
        Player.getInstance(input.getSource()).getPlayerControl().newInput(input.getInput());
        // send to the other players
        List<Integer> clients = TeddyServer.getInstance().getClientIDs();
//        clients.remove(input.getSource());
        // redistribute
        input.setRecipients(clients.toArray(new Integer[clients.size()]));
        TeddyServer.getInstance().send(input);
      } else if (message instanceof ManCursorPosition) {
        ManCursorPosition cursorPos = (ManCursorPosition) message;
        Vector3f cursor = cursorPos.getCursor();
        Player.getInstance(source.getId()).getPlayerControl().setScreenPositions(new Vector2f(cursor.x, cursor.y));
      }
      //TODO check if trigger effect is also possible for clients
    }
  }
}
