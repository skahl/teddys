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
import edu.teddys.network.messages.server.ManMessageSendDamage;
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
    //TODO get the name of the client
    if (message instanceof NetworkMessageInfo) {
      NetworkMessageInfo info = (NetworkMessageInfo) message;
      System.out.println(String.format(
              "Message received at X from client %s: %s",
//              info.getTimestamp(),
              source,
              info.getMessage()));
      // Distribute to the other clients
      TeddyServer.getInstance().send(info);
    } else if(message instanceof NetworkMessageGameState) {
      if(message instanceof GSMessageGamePaused) {
        // Just distribute the message to the other clients
        GSMessageGamePaused msg = (GSMessageGamePaused)message;
        TeddyServer.getInstance().send(msg);
      } else if(message instanceof GSMessagePlayerReady) {
        //TODO Refresh client info in TeddyServerData
        
        // Distribute info to the other clients
        GSMessagePlayerReady msg = (GSMessagePlayerReady)message;
        TeddyServer.getInstance().send(msg);
      }
    } else if(message instanceof NetworkMessageResponse) {
      if(message instanceof ResMessageSendChecksum) {
        ResMessageSendChecksum msg = (ResMessageSendChecksum)message;
        if(ChecksumManager.checkChecksum(msg.getToken(), msg.getChecksum())) {
          System.out.println("Yeah, checksum is right!");
        } else {
          System.out.println("What the ...? Checksum has to be 1!");
          System.out.println("As soon as the function is implemented, you'll be kicked.");
          //TODO Close active connection for the client
        }
      } else if(message instanceof ResMessageMapLoaded) {
        //TODO Sync with the other clients
        ResMessageMapLoaded msg = (ResMessageMapLoaded)message;
        TeddyServer.getInstance().send(msg);
        //TODO Check how many clients are ready yet to start the game occassionally.
        // (use TeddyServerData)
        
      }
    } else if(message instanceof NetworkMessageManipulation) {
      if(message instanceof ManMessageSendPosition) {
        //TODO redistibute to the other clients
        //TODO also transfer jumps
      } else if(message instanceof ManMessageTriggerWeapon) {
        //TODO read the target list
        
        //TODO calculate the damage and send them to the appropriate clients
        
        //TEST
        ManMessageSendDamage damMsg = new ManMessageSendDamage(0, 40);
        TeddyServer.getInstance().send(damMsg);
        NetworkMessageInfo dmgMsg = new NetworkMessageInfo("Teddy [0] draws first blood. :D", null);
        TeddyServer.getInstance().send(dmgMsg);
      }
      //TODO check if trigger effect is also possible for clients
    }
  }
}
