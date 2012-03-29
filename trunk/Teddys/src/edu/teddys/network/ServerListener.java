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
import edu.teddys.timer.ServerDataSync;
import edu.teddys.timer.ServerTimer;
import java.awt.Color;
import java.util.ArrayList;
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

  /**
   * This is just for alternating team allocation.
   */
  private boolean firstTeamAssignment = true;

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
        ResMessageMapLoaded msg = (ResMessageMapLoaded) message;
        // Distribute to all clients
        TeddyServer.getInstance().send(msg);

        Player newPlayer = Player.getInstance(source.getId());
        newPlayer.getData().setMapLoaded(true);

        List<Integer> playerWithActiveMap = new ArrayList<Integer>();
        for (Player curPlayer : Player.getInstanceList()) {
          if (curPlayer.getData().isMapLoaded()) {
            playerWithActiveMap.add(curPlayer.getData().getId());
          }
        }
        // Tell the client that the other players have loaded the map
        ResMessageMapLoaded mapLoaded = new ResMessageMapLoaded(source.getId(), playerWithActiveMap.toArray(new Integer[playerWithActiveMap.size()]));
        TeddyServer.getInstance().send(mapLoaded);

        // add the player to the game world (if not already joined?)
        Game.getInstance().setRandomPlayerPosition(newPlayer);
        Game.getInstance().addPlayerToWorld(newPlayer);
        // refresh the clients' positions
        List<ClientData> clientDataList = new ArrayList<ClientData>();
        for (Player curPlayer : Player.getInstanceList()) {
          clientDataList.add(curPlayer.getData());
        }
        ManMessageTransferPlayerData playerData = new ManMessageTransferPlayerData(clientDataList);
        TeddyServer.getInstance().send(playerData);

        ServerDataSync.startTimer();
        ServerTimer.startTimer();

        // Now start a game
        GSMessageBeginGame beginGame = new GSMessageBeginGame(source.getId());
        TeddyServer.getInstance().send(beginGame);
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
        player.getData().setName("Ted " + clientID);

        if (TeddyServer.getInstance().getData().getTeams().isEmpty()) {
          // Create a new team
          TeddyServer.getInstance().getData().getTeams().add(new Team(Color.RED, "1337e Molche"));
          // Create a second team
          TeddyServer.getInstance().getData().getTeams().add(new Team(Color.BLUE, "Stramme Quaster"));
        }

        Team assignedTeam = null;
        // Now read alternatingly the team
        if (firstTeamAssignment) {
          assignedTeam = TeddyServer.getInstance().getData().getTeams().get(0);
        } else {
          assignedTeam = TeddyServer.getInstance().getData().getTeams().get(1);
        }
        firstTeamAssignment = !firstTeamAssignment;
        Integer teamID = assignedTeam.getTeamID();

        // Add the player to the team
        TeddyServer.getInstance().getData().getTeams().get(teamID).addPlayer(clientID);

        // Since a new team has been created, update the TeddyServerData on the clients
        ManMessageTransferServerData serverDataMsg = new ManMessageTransferServerData(TeddyServer.getInstance().getData());
        TeddyServer.getInstance().send(serverDataMsg);

        // Refresh the teamID
        player.getData().setTeam(teamID);
        msg.getClientData().setTeam(teamID);
        String infoString = String.format("Teddy %s has joined the team %s!", data.getName(), assignedTeam.getName());
        MegaLogger.getLogger().info(infoString);
        NetworkMessageInfo teamInfoMsg = new NetworkMessageInfo(infoString);
        teamInfoMsg.setServerMessage(true);
        TeddyServer.getInstance().send(teamInfoMsg);

//        // Send this message to all clients (the current one has a new team assignment)
//        List<Integer> rec = new ArrayList<Integer>(Player.getInstances().keySet());
//        // Refresh the recipient list
//        msg.setRecipients(rec.toArray(new Integer[rec.size()]));
//        // And send the data to the clients
//        TeddyServer.getInstance().send(msg);

        // Send the data of the "old" clients to the new member
//        for (Player playerInstance : Player.getInstanceList()) {
//          if (playerInstance.getData().getId() == clientID) {
//            continue;
//          }
//          ResMessageSendClientData tmp = new ResMessageSendClientData(clientID, playerInstance.getData());
//          TeddyServer.getInstance().send(tmp);
//          //TODO not all players may have loaded the map ...
//          // Since the active players have loaded the map, send an info to the new one
//          ResMessageMapLoaded tmpMapLoaded = new ResMessageMapLoaded(new Integer[]{clientID}, playerInstance.getData().getId());
//          TeddyServer.getInstance().send(tmpMapLoaded);
//        }

        // Now update the player data on the clients
        List<ClientData> playerData = new ArrayList<ClientData>();
        List<Integer> playerWithActiveMap = new ArrayList<Integer>();
        for (Player curPlayer : Player.getInstanceList()) {
          playerData.add(curPlayer.getData());
          if (curPlayer.getData().isMapLoaded()) {
            playerWithActiveMap.add(curPlayer.getData().getId());
          }
        }
        // Refresh the player data for the client
        ManMessageTransferPlayerData playerDataMsg = new ManMessageTransferPlayerData(playerData);
        TeddyServer.getInstance().send(playerDataMsg);

        // Send a request to the new client that the specified map has to be loaded
        Entry<String, String> levelData = Game.getInstance().getLevelData();
        ReqMessageMapRequest mapRequest = new ReqMessageMapRequest(clientID, levelData.getKey(), levelData.getValue());
        TeddyServer.getInstance().send(mapRequest);
      }
    } else if (message instanceof NetworkMessageManipulation) {
      if (message instanceof ManControllerInput) {
        ManControllerInput input = (ManControllerInput) message;

        // refresh the player's action
        Player.getInstance(input.getSource()).getPlayerControl().newInput(input.getInput());
        // send to the other players
        List<Integer> clients = TeddyServer.getInstance().getClientIDs();
        // redistribute
        input.setRecipients(clients.toArray(new Integer[clients.size()]));
        TeddyServer.getInstance().send(input);
      } else if (message instanceof ManCursorPosition) {
        ManCursorPosition cursorPos = (ManCursorPosition) message;
        Vector3f cursor = cursorPos.getCursor();
        Player.getInstance(source.getId()).getPlayerControl().setScreenPositions(new Vector2f(cursor.x, cursor.y));
      }
    }
  }
}
