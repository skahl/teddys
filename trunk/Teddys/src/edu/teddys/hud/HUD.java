/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.hud;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import edu.teddys.GameModeEnum;
import java.util.ArrayList;
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
  private List<BitmapText> messages;
  private Picture item, health, jetpack, weapon;
  private BitmapText playerName, team;
  
  private BarIndicator healthIndicator, jetpackIndicator, weaponIndicator;
  
  private static HUD instance = null;

  private HUD(Node parent, AssetManager assetManager, float width, float height, GameModeEnum mode) {

      
    hudNode = new Node("hudNode");

    //trasparent background boxes
    Picture topLeftBG = new Picture("TopLeftBG");
    topLeftBG.setImage(assetManager, "Interface/grey.png", true);
    topLeftBG.setWidth(width/2);
    topLeftBG.setHeight(height/6);
    topLeftBG.move(0, height - height/6, 0);
    hudNode.attachChild(topLeftBG);
    
    Picture topRightBG = new Picture("TopRightBG");
    topRightBG.setImage(assetManager, "Interface/grey.png", true);
    topRightBG.setWidth(width/4);
    topRightBG.setHeight(height/6);
    topRightBG.move(width - width/4, height - height/6, 0);
    hudNode.attachChild(topRightBG);
    
    Picture bottomBG = new Picture("BottomBG");
    bottomBG.setImage(assetManager, "Interface/grey.png", true);
    bottomBG.setWidth(width);
    bottomBG.setHeight(height/6);
    hudNode.attachChild(bottomBG);
    

    
    imageSize = (int) (height / 12);


    
    //message area
    messages = new ArrayList<BitmapText>();

    BitmapFont messageFont = assetManager.loadFont("Interface/Fonts/Waree.fnt");
    BitmapFont font = assetManager.loadFont("Interface/Fonts/Purisa32.fnt");
    for (int i = 0; i < numMessages; i++) {
      BitmapText t = new BitmapText(messageFont);
      t.setSize(messageFont.getCharSet().getRenderedSize());
      t.setLocalTranslation(0, height - i * t.getLineHeight(), 0);
      t.setColor(ColorRGBA.White);
      hudNode.attachChild(t);
      messages.add(0, t);
    }

    
    //show the player's name and team
    playerName = new BitmapText(messageFont);
    playerName.setLocalTranslation(9 * width / 10, height, 0);
    playerName.setText("Dr. Unnamed");
    hudNode.attachChild(playerName);
    team = new BitmapText(messageFont);
    team.setLocalTranslation(9 * width / 10, height - team.getLineHeight(), 0);
    team.setText("Team Red");

    hudNode.attachChild(team);

    //display a collected item
    item = new Picture("item");
    item.setImage(assetManager, "Interface/i.png", true);
    item.setLocalTranslation(width / 20, 2 * height / 3, 0);
    item.setWidth(imageSize);
    item.setHeight(imageSize);
    //parent.attachChild(item);

    

    //health
    healthIndicator = new BarIndicator(imageSize*2.5f, height/25, 
            width / 3 + imageSize + imageSize, 
            height / 30 + imageSize/2 - height/55, 
            assetManager, 
            ColorRGBA.Red, 
            hudNode);
    
    health = new Picture("health");
    health.setImage(assetManager, "Interface/health_bar_icon_white.png", true);
    health.setLocalTranslation(width / 3 + imageSize, height / 30, 0);
    health.setWidth(imageSize);
    health.setHeight(imageSize);
    hudNode.attachChild(health);
    
    Picture healthBar = new Picture("HealthBar");
    healthBar.setImage(assetManager, "Interface/health_bar_bar_white.png", true);
    healthBar.setLocalTranslation(width / 3 + 2 * imageSize, height / 30-1, 0);
    healthBar.setWidth(imageSize*2.5f);
    healthBar.setHeight(imageSize+2);
    hudNode.attachChild(healthBar);
    
    Picture healthTail = new Picture("HealthTail");
    healthTail.setImage(assetManager, "Interface/health_bar_tail_white2.png", true);
    healthTail.setLocalTranslation(width / 3 + 2 * imageSize + imageSize*2.5f, height / 30-1, 0);
    healthTail.setWidth(imageSize/12);
    healthTail.setHeight(imageSize+2);
    hudNode.attachChild(healthTail);
    

    //jeatpack energy
    jetpackIndicator = new BarIndicator(imageSize*2.5f, height/25,
            2*width/3+2* imageSize, 
            height/30 + imageSize/2 - height/55,
            assetManager, 
            ColorRGBA.Blue, 
            hudNode);
        
    jetpack = new Picture("jetpack");
    jetpack.setImage(assetManager, "Interface/jetpack_bar_icon_white.png", true);
    jetpack.setLocalTranslation(2*width / 3 + imageSize, height / 30, 0);
    jetpack.setWidth(imageSize);
    jetpack.setHeight(imageSize);
    hudNode.attachChild(jetpack);
    
    Picture jetpackBar = new Picture("JetpackBar");
    jetpackBar.setImage(assetManager, "Interface/health_bar_bar_white.png", true);
    jetpackBar.setLocalTranslation(2*width / 3 + 2 * imageSize, height / 30-1, 0);
    jetpackBar.setWidth(imageSize*2.5f);
    jetpackBar.setHeight(imageSize+2);
    hudNode.attachChild(jetpackBar);
    
    Picture jetpackTail = new Picture("JetpackTail");
    jetpackTail.setImage(assetManager, "Interface/health_bar_tail_white2.png", true);
    jetpackTail.setLocalTranslation(2*width / 3 + 2 * imageSize + imageSize*2.5f, height / 30-1, 0);
    jetpackTail.setWidth(imageSize/12);
    jetpackTail.setHeight(imageSize+2);
    hudNode.attachChild(jetpackTail);
    
    
    //ammo
    weaponIndicator = new BarIndicator(imageSize*2.5f, height/25,
            2* imageSize, 
            height/30 + imageSize/2 - height/55,
            assetManager, 
            ColorRGBA.Yellow, 
            hudNode);
        
    weapon = new Picture("weapon");
    weapon.setImage(assetManager, "Interface/weapon_bar_icon_white.png", true);
    weapon.setLocalTranslation(imageSize, height / 30, 0);
    weapon.setWidth(imageSize);
    weapon.setHeight(imageSize);
    hudNode.attachChild(weapon);
    
    Picture weaponBar = new Picture("WeaponBar");
    weaponBar.setImage(assetManager, "Interface/health_bar_bar_white.png", true);
    weaponBar.setLocalTranslation(2 * imageSize, height / 30-1, 0);
    weaponBar.setWidth(imageSize*2.5f);
    weaponBar.setHeight(imageSize+2);
    hudNode.attachChild(weaponBar);
    
    Picture weaponTail = new Picture("WeaponTail");
    weaponTail.setImage(assetManager, "Interface/health_bar_tail_white2.png", true);
    weaponTail.setLocalTranslation(2 * imageSize + imageSize*2.5f, height / 30-1, 0);
    weaponTail.setWidth(imageSize/12);
    weaponTail.setHeight(imageSize+2);
    hudNode.attachChild(weaponTail);
    
    parent.attachChild(hudNode);
    
    //test the iconlist
    IconList icons = new IconList(40, width/50, height/2, assetManager, hudNode);
    icons.addItem("Test", 0, "Interface/placeholder.png", ColorRGBA.Red);
    icons.setValue("Test", 50);
    icons.addItem("Test2", 1, "Interface/placeholder.png", ColorRGBA.Blue);
    icons.addItem("Test3", 2, "Interface/placeholder.png", ColorRGBA.Green);
    icons.removeItem("Test2");
    icons.addItem("Test4", 1, "Interface/placeholder.png", ColorRGBA.Orange);
    
    
    switch(mode) {
        case CAPTURE_THE_HONEY:
            initTeam();
            break;
        case DEATHMATCH:
            initSolo();    
    }
  }
  
  private void initSolo() {
      
  }
  
  private void initTeam() {
      
  }
  
  public static HUD getInstance(Node parent, AssetManager assetManager, float width, float height, GameModeEnum mode) {
    if (instance == null) {
      instance = new HUD(parent, assetManager, width, height, mode);
    }
    return instance;
  }

  public void setMessage(int index, String message) {
    messages.get(index).setText(message);
  }

  public void setAmmo(int num) {
    weaponIndicator.setValue(num);
  }

  public void setHealth(int health) {
    //TODO call a repaint method
    //healthText.setText(Integer.toString(health)+"%");
    
//    for(Spatial node : nodes) {
//      hudNode.attachChild(node);
//    }
    //TODO use a graphical representation of the health?
    
    //healthDisplay.setHealth(health);
      healthIndicator.setValue(health);
  }
  
  public void setJetpackEnergy(int energy) {
      jetpackIndicator.setValue(energy);
  }

  public void setActiveWeapon(String name) {
      
  }
  
  public void setAmmo(String weapon, int ammo) {
      
  }
  
  public void setItem(String item) {
      
  }

  public void setPlayerName(String name) {
    playerName.setText(name);
  }

  public void setTeam(String team) {
    this.team.setText(team);
  }
}