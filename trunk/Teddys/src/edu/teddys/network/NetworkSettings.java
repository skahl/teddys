/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

/**
 *
 * Provides network settings for the server and the client.
 * 
 * @author cm
 */
public class NetworkSettings {
  /**
   * The default value for server connection. Used for development issues yet.
   */
  public static final String DEFAULT_SERVER = "127.0.0.1";
  /**
   * The default IP for the local server.
   */
  public static final String LOCAL_IP = "127.0.0.1";
  /**
   * The port on which the socket will be opened.
   */
  public static final Integer SERVER_PORT = 6100;
  /**
   * The port on which the socket will be opened.
   */
  public static final Integer LOCAL_SERVER_PORT = 6101;
  /**
   * Use encryption for the whole communication.
   */
  public static final boolean USE_ENCRYPTION_IF_AVAIL = false;
  /**
   * Use the UDP communication protocol for messages.
   */
  public static final boolean USE_UDP = true;
  /**
   * The ID of the server regarding network communication. Default: 0, clients
   * have to be given IDs > 0.
   */
  public static final Integer SERVER_ID = 0;
}
