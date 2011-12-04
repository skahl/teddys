/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.protection;

import edu.teddys.network.TeddyServer;
import edu.teddys.network.messages.server.ReqMessageRequestChecksum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cm
 */
public class ChecksumManager {
  
  /**
   * The intervall for checksum checks
   */
  private static Integer timerIntervall = new Integer(30);
  
  private static Map<String,String> result;
  private static Map<String,List<String>> files = new HashMap<String,List<String>>();
  
  /**
   * Start the checksum manager timer. This sends checksum requests every intervall
   * to the clients of the server. Note: If all clients responded, use ready()
   * to generate a new list of files to be checked.
   */
  public static void startTimer() {
    //TODO dismiss old values
  }
  
  public static void stopTimer() {
    
  }
  
  protected void sendRequest() {
    String currentToken = "312";
    List<String> currentFiles = new ArrayList<String>();
    //TODO add randomly files
    files.put(currentToken, currentFiles);
    result.put(currentToken, "1");
    ReqMessageRequestChecksum message = new ReqMessageRequestChecksum(currentToken, currentFiles);
    TeddyServer.getInstance().send(message);
  }
  
  public static boolean checkChecksum(String token, String input) {
    if(input.equals(result.get(token))) {
      return true;
    }
    return false;
  }
}
