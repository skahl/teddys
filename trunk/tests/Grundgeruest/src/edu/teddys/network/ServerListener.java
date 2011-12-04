/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import edu.teddys.network.messages.NetworkMessageGameState;
import edu.teddys.network.messages.NetworkMessageInfo;
import edu.teddys.network.messages.NetworkMessageManipulation;
import edu.teddys.network.messages.NetworkMessageResponse;
import edu.teddys.network.messages.client.GSMessageGamePaused;
import edu.teddys.network.messages.client.ResMessageMapLoaded;
import edu.teddys.network.messages.client.GSMessagePlayerReady;
import edu.teddys.network.messages.client.ResMessageSendChecksum;
import edu.teddys.network.messages.client.ManMessageSendPosition;
import edu.teddys.network.messages.client.ManMessageTriggerWeapon;
import edu.teddys.protection.ChecksumManager;

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
    if (message instanceof NetworkMessageInfo) {
      NetworkMessageInfo info = (NetworkMessageInfo) message;
      System.out.println(String.format(
              "Message received at X from client %s: %s",
//              info.getTimestamp(),
              source,
              info.getMessage()));
    } else if(message instanceof NetworkMessageGameState) {
      if(message instanceof GSMessageGamePaused) {
        // distribute messages to the other clients
        GSMessageGamePaused msg = (GSMessageGamePaused)message;
        TeddyServer.getInstance().send(msg);
      } else if(message instanceof GSMessagePlayerReady) {
        //TODO sync with the other clients
        GSMessagePlayerReady msg = (GSMessagePlayerReady)message;
        TeddyServer.getInstance().send(msg);
      }
    } else if(message instanceof NetworkMessageResponse) {
      if(message instanceof ResMessageSendChecksum) {
        ResMessageSendChecksum msg = (ResMessageSendChecksum)message;
        if(ChecksumManager.checkChecksum(msg.getToken(), msg.getChecksum())) {
          System.out.println("Yeah, checksum is 1! Right!");
        } else {
          System.out.println("What the ...? Checksum has to be 1!");
          System.out.println("As soon as the function is implemented, you'll be kicked.");
        }
        //TODO check if all clients responded. If so, call ChecksumManager.ready()!
      } else if(message instanceof ResMessageMapLoaded) {
        //TODO sync with the other clients
        ResMessageMapLoaded msg = (ResMessageMapLoaded)message;
        TeddyServer.getInstance().send(msg);
        //TODO check how many clients are ready yet to start the game occassionally.
        
      }
    } else if(message instanceof NetworkMessageManipulation) {
      if(message instanceof ManMessageSendPosition) {
        //TODO redistibute to the other clients
      } else if(message instanceof ManMessageTriggerWeapon) {
        //TODO calculate the damage and send them to the appropriate clients
        
        NetworkMessageInfo dmgMsg = new NetworkMessageInfo("Teddy [0] draws first blood. :D", null);
        TeddyServer.getInstance().send(dmgMsg);
      }
      //TODO check if trigger effect is also possible for clients
    }
  }
}
