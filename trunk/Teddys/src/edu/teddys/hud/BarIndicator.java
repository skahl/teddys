/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.hud;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import edu.teddys.states.Game;

/**
 *
 * A Bar indicating the status of health, jetpack etc.
 * 
 * @author besient
 */
public class BarIndicator {

  private Geometry geom;
  private float width, height, geomWidth, geomHeight;
  private int percentage;

  /**
   * Contructor
   * 
   * @param width The bar's widh
   * @param height The bar's height
   * @param x The x coordinate of the bar's position
   * @param y The y coordinate of the bar's position
   * @param assetManager
   * @param color The bar's color
   * @param parent The parent node, the bar will be attached to
   */
  public BarIndicator(float width, float height, float x, float y, AssetManager assetManager, ColorRGBA color, Node parent) {
    Quad bar = new Quad(width, height);
    geom = new Geometry("BarGeom", bar);
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", color);
    geom.setMaterial(mat);
    geom.move(x, y, 0);

    //TODO add node
    Game.getInstance().addSpatial(parent, geom);
    this.width = width;
    this.height = height;
    geomWidth = geom.getLocalScale().x;
    geomHeight = geom.getLocalScale().y;
  }

  /**
   * Set the new size in percent
   * @param percentage 
   */
  public void setValue(int percentage) {
    geom.setLocalScale(geomWidth * percentage / 100, geomHeight, 0);
    this.percentage = percentage;
  }

  /**
   * 
   * @return The current size in percent
   */
  public int getValue() {
    return percentage;
  }

  /**
   * Getter method for the spatial object
   * @return 
   */
  public Spatial getSpatial() {
    return geom;
  }
}
