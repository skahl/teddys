/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

/**
 * 
 * Indicates that an entity supports encryption. The entity must possess a
 * public key.
 *
 * @author cm
 */
public interface EncryptionAvailableInterface {
  public String pubKey = null;
}
