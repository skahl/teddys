/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

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
import edu.teddys.network.messages.client.ResMessageSendChecksum;
import edu.teddys.network.messages.client.ManMessageSendPosition;
import edu.teddys.network.messages.client.ManMessageTriggerWeapon;
import edu.teddys.network.messages.client.ResMessageSendClientData;
import edu.teddys.network.messages.server.GSMessageBeginGame;
import edu.teddys.network.messages.server.ManMessageSendDamage;
import edu.teddys.objects.player.Player;
import edu.teddys.states.Game;
import edu.teddys.timer.ChecksumManager;
import java.awt.Color;

/**
 * 
 * Since clients are able to submit messages to the server, this
 * class is used to fetch these ones.
 *
 * @author cm
 */
public class ServerListener implements MessageListener<HostedConnection> {

  public void messageReceived(HostedConnection source, Message message) {
//    if (message instanceof NetworkMessage) {
//      NetworkMessage msg = (NetworkMessage) message;
//      if (msg instanceof NetworkMessageInfo) {
    //TODO get the name of the client
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
        //TODO link to the map load status?
        // Distribute info to the other clients
        GSMessagePlayerReady msg = (GSMessagePlayerReady) message;
        TeddyServer.getInstance().send(msg);
        // Refresh client info in TeddyServerData
        if(TeddyServer.getInstance().getClientData(source.getId()) == null) {
          MegaLogger.getLogger().error("User has not yet specified his client data!");
          return;
        }
        TeddyServer.getInstance().getClientData(source.getId()).setReady(true);
        // Refresh server data of the clients
        //TODO
//        ManMessageTransferServerData sync = new ManMessageTransferServerData(TeddyServer.getInstance().getData());
//        TeddyServer.getInstance().send(sync);
      }
    } else if (message instanceof NetworkMessageResponse) {
      if (message instanceof ResMessageSendChecksum) {
        //
        // PROTECTION: USER SENT HIS CHECKSUM
        //
        ResMessageSendChecksum msg = (ResMessageSendChecksum) message;
        // Check the transmitted checksum for some files ...
        System.out.println("User submitted " + msg.getChecksum() + " for token " + msg.getToken());
        try {
          ChecksumManager.checkChecksum(msg.getToken(), msg.getChecksum());
        } catch (VerifyError error) {
          TeddyServer.getInstance().disconnect(source.getId(), error.getLocalizedMessage());
        }
      } else if (message instanceof ResMessageMapLoaded) {
        //
        // CLIENT HAS JUST LOADED A MAP
        //
        //TODO Distribute to the other clients?
        ResMessageMapLoaded msg = (ResMessageMapLoaded) message;
        TeddyServer.getInstance().send(msg);
        //TODO Check how many clients are ready yet to start the game occassionally.
        // (use TeddyServerData)
        Player newPlayer = Player.getInstance(source.getId());
        // set the client data for local access on demand
        //TODO always sync the data! use a thread for that
        newPlayer.setData(TeddyServer.getInstance().getClientData(source.getId()));
        //TODO add the player to the game if not already joined
        Game.getInstance().addPlayerToWorld(newPlayer);
        // Now start a game
        GSMessageBeginGame beginGame = new GSMessageBeginGame();
        TeddyServer.getInstance().send(beginGame);
      } else if (message instanceof ResMessageSendClientData) {
        //
        // RECEIVED USER DATA
        //
        ResMessageSendClientData msg = (ResMessageSendClientData) message;
        // Tell the server to update the data
        ClientData data = msg.getClientData();
        Integer clientID = data.getId();
        TeddyServer.getInstance().setClientData(clientID, data);
        //TODO Add member to a team

        //TEST
        Team newTeam = new Team(Color.BLUE, "Grampen");
        if (TeddyServer.getInstance().getData().getTeams().isEmpty()) {
          // Create a new team
          TeddyServer.getInstance().getData().getTeams().add(newTeam);
        }
        Integer teamId = 0;
        TeddyServer.getInstance().getData().getTeams().get(teamId).addPlayer(clientID);
        data.setTeam(teamId);
        NetworkMessageInfo teamInfoMsg = new NetworkMessageInfo(
                "Teddy has sent his client data. " + data.getName()
                + " belongs to the team " + newTeam.getName() + "!");
        TeddyServer.getInstance().send(teamInfoMsg);

        // send a neat "gift" to the new client
        ManMessageSendDamage dmg = new ManMessageSendDamage(clientID, (int) (Math.random() * 20f + 1));
        TeddyServer.getInstance().send(dmg);

        NetworkMessageInfo dmgInfo = new NetworkMessageInfo("Come on, "
                + data.getName() + ". Don't worry about the damage I gave to you! "
                + "I like you, really! :P", clientID);
        TeddyServer.getInstance().send(dmgInfo);
      }
    } else if (message instanceof NetworkMessageManipulation) {
      if (message instanceof ManControllerInput) {
        ManControllerInput input = (ManControllerInput) message;
        
//        if(source.getId() == Player.LOCAL_PLAYER) {
//          // ignore it, it is handled by the input manager attached to the local
//          // player
//          //TODO sync with the input data if the server has been created locally
//          // (local join)
//          return;
//        }
        // refresh the player
        Player.getInstance(source.getId()).newInput(input.getInput());
      } else if (message instanceof ManMessageSendPosition) {
        //
        // USER POSITION RECEIVED
        //
        ManMessageSendPosition msg = (ManMessageSendPosition) message;
        //TODO redistibute to the other clients
        TeddyServer.getInstance().send(msg);

        //TODO calculate the position vector

        //TODO in case of a larger time frame, reset the position of the client to the last
        // known one
      } else if (message instanceof ManMessageTriggerWeapon) {
        //
        // USER WANTS TO GET NASTY (-> WEAPONS)
        //

        //TODO read the target list

        //TODO calculate the damage and send them to the appropriate clients

        //TEST
        Integer[] affected = (Integer[]) TeddyServer.getInstance().getClientIDs().toArray();
        Integer[] damage = new Integer[]{40};
        for (int i = 0; i < affected.length; i++) {
          ManMessageSendDamage damMsg = new ManMessageSendDamage(affected[i], damage[i]);
          TeddyServer.getInstance().send(damMsg);
        }
      }
      //TODO check if trigger effect is also possible for clients
    }
  }
}
