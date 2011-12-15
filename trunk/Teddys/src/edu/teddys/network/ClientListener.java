/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.message.DisconnectMessage;
import edu.teddys.BaseGame;
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
import java.util.logging.Level;

/**
 *
 * The listener for clients of our network implementation.
 * 
 * @author cm
 */
public class ClientListener implements MessageListener<com.jme3.network.Client> {

  public void messageReceived(com.jme3.network.Client source, Message message) {
    System.out.println("New message arrived: " + message.getClass());
    if (message instanceof DisconnectMessage) {
      BaseGame.getLogger().log(Level.WARNING,
              "Client has been disconnected from the " 
              + "server yet. Reason: {0}", 
              ((DisconnectMessage) message).getReason());
      //TODO change game state
    }
    if (message instanceof NetworkMessageInfo) {
      NetworkMessageInfo info = (NetworkMessageInfo) message;
      //TODO use a method to find the ingame name of the teddy
      System.out.println(String.format(
              "Teddy %s says: %s",
              //              info.getTimestamp(),
              source.getId(),
              info.getMessage()));
      //TODO display in the HUD
    } else if (message instanceof NetworkMessageGameState) {
      if (message instanceof GSMessageGamePaused) {
        //TODO Set game state to "Paused"
      } else if (message instanceof GSMessageBeginGame) {
        //TODO Set game state to "Game"
      } else if (message instanceof GSMessageEndGame) {
        //TODO Set game state to "EndGame"
      } else if (message instanceof GSMessagePlayerReady) {
        //TODO Refresh the status of the other clients
      }
    } else if (message instanceof NetworkMessageManipulation) {
      if (message instanceof ManMessageActivateItem) {
        ManMessageActivateItem msg = (ManMessageActivateItem) message;
//        TeddyClient.getInstance().setCurrentItem(msg.getItemName());
      } else if (message instanceof ManMessageSendDamage) {
        ManMessageSendDamage msg = (ManMessageSendDamage) message;
        if (msg.getClient().equals(TeddyClient.getInstance().getId())) {
          TeddyClient.getInstance().addDamage(msg.getDamage());
        }
      } else if (message instanceof ManMessageTransferServerData) {
        //overwrite the current data
        ManMessageTransferServerData msg = (ManMessageTransferServerData) message;
        TeddyServer.getInstance().setData(msg.getData());
      } else if (message instanceof ManMessageTriggerEffect) {
        //TODO call the appropriate effect and game state
      }
    } else if (message instanceof NetworkMessageRequest) {
      if (message instanceof ReqMessageMapRequest) {
        ReqMessageMapRequest msg = (ReqMessageMapRequest) message;
        //TODO call the map loader

      } else if (message instanceof ReqMessagePauseRequest) {
        //TODO call the game state "pause"
      } else if (message instanceof ReqMessageRelocateServer) {
        ReqMessageRelocateServer msg = (ReqMessageRelocateServer) message;
        if (msg.getDestination().equals(TeddyClient.getInstance().getId())) {
          TeddyServer.getInstance().startServer();
          System.out.println("Server is ready to get connections.");
        } else {
          //TODO prepare to (seamlessly?) join the new server.
        }
      } else if (message instanceof ReqMessageSendChecksum) {
        ReqMessageSendChecksum msg = (ReqMessageSendChecksum) message;
        //TODO calculate the checksum (use a dummy value now)
        ResMessageSendChecksum response = new ResMessageSendChecksum(
                msg.getToken(), "1");
        TeddyClient.getInstance().send(response);
      } else if (message instanceof ReqMessageSendClientData) {
        System.out.println("Client should send the data now ...");
        ResMessageSendClientData response = new ResMessageSendClientData(TeddyClient.getInstance().getData());
        TeddyClient.getInstance().send(response);
      }
    }
  }
}
