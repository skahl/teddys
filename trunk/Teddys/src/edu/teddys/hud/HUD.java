/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.hud;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.ui.Picture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author besient
 */
public class HUD {

  public Node hudNode;
  private final int numMessages = 5;
  private int imageSize;
  private int healthSize;
  private List<BitmapText> messages;
  private Picture item, health, jetpack, weapon;
  private BitmapText healthText, playerName, team, ammoText, jetpackText;
  private Map<String, Picture> weapons;
  
  private static HUD instance = null;

  private HUD(Node parent, AssetManager assetManager, float width, float height) {

      
    hudNode = new Node("hudNode");

    //temporary background boxes
    Quad bottomBG_quad = new Quad(width, height / 6);
    Geometry bottomBG_geo = new Geometry("BackgroundG", bottomBG_quad);
    Material bottomBG_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    bottomBG_mat.setTexture("ColorMap", assetManager.loadTexture("Textures/HUD/hudbg.png"));
    bottomBG_mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    bottomBG_geo.setMaterial(bottomBG_mat);
    bottomBG_geo.move(0,0,-1);
    hudNode.attachChild(bottomBG_geo);
    
    Quad topBG_quad = new Quad(width, height / 6);
    Geometry topBG_geo = new Geometry("BackgroundG", bottomBG_quad);
    topBG_geo.setMaterial(bottomBG_mat);
    topBG_geo.move(0,height - height/6,-1);
    hudNode.attachChild(topBG_geo);
    
    
    
    
    imageSize = (int) (height / 14);
    healthSize = (int) (height / 12);


    
    messages = new ArrayList<BitmapText>();

    BitmapFont messageFont = assetManager.loadFont("Interface/Fonts/Waree.fnt");
    BitmapFont font = assetManager.loadFont("Interface/Fonts/Purisa32.fnt");
    for (int i = 0; i < numMessages; i++) {
      BitmapText t = new BitmapText(messageFont);
      t.setSize(messageFont.getCharSet().getRenderedSize());
      t.setLocalTranslation(0, height - i * t.getLineHeight(), 0);
      hudNode.attachChild(t);
      messages.add(0, t);
    }

    weapons = new HashMap<String, Picture>();

    playerName = new BitmapText(messageFont);
    playerName.setLocalTranslation(9 * width / 10, height, 0);
    playerName.setText("Dr. Unnamed");
    hudNode.attachChild(playerName);
    team = new BitmapText(messageFont);
    team.setLocalTranslation(9 * width / 10, height - team.getLineHeight(), 0);
    team.setText("Team Red");

    hudNode.attachChild(team);

    item = new Picture("item");
    item.setImage(assetManager, "Interface/i.png", true);
    item.setLocalTranslation(width / 20, height / 3, 0);
    item.setWidth(imageSize);
    item.setHeight(imageSize);
    //parent.attachChild(item);

    //Weapon
    weapon = new Picture("weapon");
    weapon.setImage(assetManager, "Interface/weapon1_3.png", true);
    weapon.setLocalTranslation(width / 5 - imageSize, height / 30, 0);
    weapon.setWidth(imageSize);
    weapon.setHeight(imageSize);
    hudNode.attachChild(weapon);

    ammoText = new BitmapText(font);
    ammoText.setSize(font.getCharSet().getRenderedSize());
    ammoText.setLocalTranslation(width / 5 + ammoText.getLineWidth() + 20,
            weapon.getLocalTranslation().y + imageSize / 2 + ammoText.getLineHeight() / 2,
            0);
    //ammoText.setText("100");
    hudNode.attachChild(ammoText);

    //Health
    health = new Picture("health");
    health.setImage(assetManager, "Interface/health1_3.png", true);
    health.setLocalTranslation(width / 2 - healthSize, height / 30, 0);
    health.setWidth(healthSize);
    health.setHeight(healthSize);
    hudNode.attachChild(health);

    healthText = new BitmapText(font);
    healthText.setName("healthText");
    healthText.setSize(font.getCharSet().getRenderedSize());
    healthText.setLocalTranslation(width / 2 + healthText.getLineWidth() + 20,
            health.getLocalTranslation().y + healthSize / 2 + healthText.getLineHeight() / 2,
            0);
    //healthText.setText("");
    hudNode.attachChild(healthText);

    //Jetpack
    jetpack = new Picture("jetpack");
    jetpack.setImage(assetManager, "Interface/jetpack1_3.png", true);
    jetpack.setLocalTranslation(4 * width / 5 - imageSize, height / 30, 0);
    jetpack.setWidth(imageSize);
    jetpack.setHeight(imageSize);
    hudNode.attachChild(jetpack);

    jetpackText = new BitmapText(font);
    jetpackText.setName("jetpackText");
    jetpackText.setSize(font.getCharSet().getRenderedSize());
    jetpackText.setLocalTranslation(4 * width / 5 + jetpackText.getLineWidth() + 20,
            jetpack.getLocalTranslation().y + imageSize / 2 + jetpackText.getLineHeight() / 2,
            0);
    //jetpackText.setText("100");
    hudNode.attachChild(jetpackText);

//    parent.attachChild(g);
    
    parent.attachChild(hudNode);
  }
  
  public static HUD getInstance(Node parent, AssetManager assetManager, float width, float height) {
    if (instance == null) {
      instance = new HUD(parent, assetManager, width, height);
    }
    return instance;
  }

  public void setMessage(int index, String message) {
    messages.get(index).setText(message);
  }

  public void setAmmo(int num) {
    ammoText.setText(Integer.toString(num));
  }

  public void setHealth(int health) {
    //TODO call a repaint method
    healthText.setText(Integer.toString(health)+"%");
    
//    for(Spatial node : nodes) {
//      hudNode.attachChild(node);
//    }
    //TODO use a graphical representation of the health?
  }

  public void setActiveWeapon(String name) {
    weapon = weapons.get(name);
  }

  public void setPlayerName(String name) {
    playerName.setText(name);
  }

  public void setTeam(String team) {
    this.team.setText(team);
  }
}