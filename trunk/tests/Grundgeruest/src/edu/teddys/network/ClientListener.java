/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import edu.teddys.network.messages.NetworkMessageGameState;
import edu.teddys.network.messages.NetworkMessageInfo;
import edu.teddys.network.messages.NetworkMessageManipulation;
import edu.teddys.network.messages.NetworkMessageRequest;
import edu.teddys.network.messages.server.GSMessageBeginGame;
import edu.teddys.network.messages.server.GSMessageEndGame;
import edu.teddys.network.messages.server.ManMessageActivateItem;
import edu.teddys.network.messages.server.ManMessageSendDamage;
import edu.teddys.network.messages.server.ManMessageTransferServerData;
import edu.teddys.network.messages.server.ManMessageTriggerEffect;
import edu.teddys.network.messages.server.ReqMessageMapRequest;
import edu.teddys.network.messages.server.ReqMessagePauseRequest;
import edu.teddys.network.messages.server.ReqMessageRelocateServer;
import edu.teddys.network.messages.server.ReqMessageRequestChecksum;

/**
 *
 * The listener for clients of our network implementation.
 * 
 * @author cm
 */
public class ClientListener implements MessageListener<com.jme3.network.Client> {

  public void messageReceived(com.jme3.network.Client source, Message message) {
    if (message instanceof NetworkMessageInfo) {
      NetworkMessageInfo info = (NetworkMessageInfo) message;
      System.out.println(String.format(
              "Message received at X from client %s: %s",
//              info.getTimestamp(),
              source,
              info.getMessage()));
    } else if(message instanceof NetworkMessageGameState) {
      if(message instanceof GSMessageBeginGame) {
        
      } else if(message instanceof GSMessageEndGame) {
        
      }
    } else if(message instanceof NetworkMessageManipulation) {
      if(message instanceof ManMessageActivateItem) {
        ManMessageActivateItem msg = (ManMessageActivateItem)message;
//        TeddyClient.getInstance().setCurrentItem(msg.getItemName());
      } else if(message instanceof ManMessageSendDamage) {
        ManMessageSendDamage msg = (ManMessageSendDamage)message;
        TeddyClient.getInstance().setHealth(msg.getDamage());
      } else if(message instanceof ManMessageTransferServerData) {
        
      } else if(message instanceof ManMessageTriggerEffect) {
        
      }
    } else if(message instanceof NetworkMessageRequest) {
      if(message instanceof ReqMessageMapRequest) {
        
      } else if(message instanceof ReqMessagePauseRequest) {
        
      } else if(message instanceof ReqMessageRelocateServer) {
        
      } else if(message instanceof ReqMessageRequestChecksum) {
        
      }
    }
  }
}
