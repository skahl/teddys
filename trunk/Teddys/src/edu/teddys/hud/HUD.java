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
  private List<BitmapText> messages;
  
  private final int imageSize;
  private final float iconOffset, hudHeight, weaponXLocation, healthXLocation, jetpackXLocation, 
          barYOffset, barWidth, barHeight, tailWidth;
  
  private Picture health, jetpack, weapon;
  private BarIndicator healthIndicator, jetpackIndicator, weaponIndicator;
  
  private Map<String, Picture> items;
  
  private String activeWeapon;
  private Map<String, Picture> weaponImages;
  private Map<String, BarIndicator> weaponBars;
  
  private IconList weaponList;
  
  private BitmapText playerName, team;
  
  private final String bar = "Interface/HUD/bar_bar_white.png";
  private final String bar_tail = "Interface/HUD/bar_tail_white.png";
  private final String bar_icon_health = "Interface/HUD/bar_icon_health_white.png";
  private final String bar_icon_jetpack = "Interface/HUD/bar_icon_jetpack_white.png";
  private final String bar_icon_weapon = "Interface/HUD/bar_icon_weapon_white.png";  
  
  
  private static HUD instance = null;

  private HUD(Node parent, AssetManager assetManager, float width, float height, GameModeEnum mode) {

      
    hudNode = new Node("hudNode");

    imageSize = (int) (height / 20);
    iconOffset = 3 * imageSize;
    hudHeight = height / 30;
    
    weaponXLocation = 0 + iconOffset;
    healthXLocation = width / 3 + iconOffset;
    jetpackXLocation = 2 * width / 3 + iconOffset;
    
    barWidth = imageSize * 2.5f;
    barHeight = imageSize / 2;
    tailWidth = imageSize / 12;
    barYOffset = imageSize / 4;
    
    BitmapFont messageFont = assetManager.loadFont("Interface/Fonts/Waree.fnt");
    BitmapFont font = assetManager.loadFont("Interface/Fonts/Purisa32.fnt");

    
    Picture bottomBG = new Picture("BottomBG");
    bottomBG.setImage(assetManager, "Interface/HUD/grey.png", true);
    bottomBG.setWidth(2*width/3 + 67/12*imageSize);
    bottomBG.setHeight(1.5f*imageSize);
    bottomBG.move(2.5f*imageSize, height/30f - imageSize/4.5f, 0);
    hudNode.attachChild(bottomBG);
    
    
    //message area
    messages = new ArrayList<BitmapText>();
    for (int i = 0; i < numMessages; i++) {
      BitmapText t = new BitmapText(messageFont);
      t.setSize(messageFont.getCharSet().getRenderedSize());
      t.setLocalTranslation(0, height - i * t.getLineHeight(), 0);
      t.setColor(ColorRGBA.Black);
      hudNode.attachChild(t);
      messages.add(0, t);
    }

    
    //show the player's name and team
    playerName = new BitmapText(messageFont);
    playerName.setLocalTranslation(9 * width / 10, height, 0);
    playerName.setText("Dr. Unnamed");
    playerName.setColor(ColorRGBA.Black);
    hudNode.attachChild(playerName);
    team = new BitmapText(messageFont);
    team.setLocalTranslation(9 * width / 10, height - team.getLineHeight(), 0);
    team.setText("Team Red");
    team.setColor(ColorRGBA.Black);
    hudNode.attachChild(team);

    //display a collected item
//    Picture item = new Picture("item");
//    item.setImage(assetManager, "Interface/HUD/placeholder.png", true);
//    item.setLocalTranslation(width / 20, 2 * height / 3, 0);
//    item.setWidth(imageSize);
//    item.setHeight(imageSize);
    //parent.attachChild(item);

//    items = new HashMap<String, Picture>();
//    items.put("default", item);
    

    //health
    healthIndicator = new BarIndicator(barWidth, barHeight, 
            healthXLocation + imageSize, 
            hudHeight + barYOffset, 
            assetManager, 
            ColorRGBA.Red, 
            hudNode);
    
    health = new Picture("health");
    health.setImage(assetManager, bar_icon_health, true);
    health.setLocalTranslation(healthXLocation, hudHeight, 0);
    health.setWidth(imageSize);
    health.setHeight(imageSize);
    hudNode.attachChild(health);
    
    Picture healthBar = new Picture("HealthBar");
    healthBar.setImage(assetManager, bar, true);
    healthBar.setLocalTranslation(healthXLocation + imageSize, hudHeight, 0);
    healthBar.setWidth(barWidth);
    healthBar.setHeight(imageSize);
    hudNode.attachChild(healthBar);
    
    Picture healthTail = new Picture("HealthTail");
    healthTail.setImage(assetManager, bar_tail, true);
    healthTail.setLocalTranslation(healthXLocation + imageSize + barWidth, hudHeight, 0);
    healthTail.setWidth(tailWidth);
    healthTail.setHeight(imageSize);
    hudNode.attachChild(healthTail);
    

    //jeatpack energy
    jetpackIndicator = new BarIndicator(barWidth, barHeight,
            jetpackXLocation + imageSize, 
            hudHeight + barYOffset,
            assetManager, 
            ColorRGBA.Blue, 
            hudNode);
        
    jetpack = new Picture("jetpack");
    jetpack.setImage(assetManager, bar_icon_jetpack, true);
    jetpack.setLocalTranslation(jetpackXLocation, hudHeight, 0);
    jetpack.setWidth(imageSize);
    jetpack.setHeight(imageSize);
    hudNode.attachChild(jetpack);
    
    Picture jetpackBar = new Picture("JetpackBar");
    jetpackBar.setImage(assetManager, bar, true);
    jetpackBar.setLocalTranslation(jetpackXLocation + imageSize, hudHeight, 0);
    jetpackBar.setWidth(barWidth);
    jetpackBar.setHeight(imageSize);
    hudNode.attachChild(jetpackBar);
    
    Picture jetpackTail = new Picture("JetpackTail");
    jetpackTail.setImage(assetManager, bar_tail, true);
    jetpackTail.setLocalTranslation(jetpackXLocation + imageSize + barWidth, hudHeight, 0);
    jetpackTail.setWidth(tailWidth);
    jetpackTail.setHeight(imageSize);
    hudNode.attachChild(jetpackTail);
    
    
    //ammo
    weaponIndicator = new BarIndicator(barWidth, barHeight,
            weaponXLocation + imageSize, 
            hudHeight + barYOffset,
            assetManager, 
            ColorRGBA.Yellow, 
            hudNode);
        
    weapon = new Picture("weapon");
    weapon.setImage(assetManager, bar_icon_weapon, true);
    weapon.setLocalTranslation(iconOffset, hudHeight, 0);
    weapon.setWidth(imageSize);
    weapon.setHeight(imageSize);
    hudNode.attachChild(weapon);
    
    Picture weaponBar = new Picture("WeaponBar");
    weaponBar.setImage(assetManager, bar, true);
    weaponBar.setLocalTranslation(weaponXLocation + imageSize, hudHeight, 0);
    weaponBar.setWidth(barWidth);
    weaponBar.setHeight(imageSize);
    hudNode.attachChild(weaponBar);
    
    Picture weaponTail = new Picture("WeaponTail");
    weaponTail.setImage(assetManager, bar_tail, true);
    weaponTail.setLocalTranslation(weaponXLocation + imageSize + barWidth, hudHeight, 0);
    weaponTail.setWidth(tailWidth);
    weaponTail.setHeight(imageSize);
    hudNode.attachChild(weaponTail);
    
    
    weaponList = new HorizontalIconList(imageSize, iconOffset, 2 * imageSize, assetManager, hudNode);
    
    //test weapon list
    weaponList.addItem("Test1", 0, "Interface/HUD/placeholder.png", ColorRGBA.Blue);
    weaponList.addItem("Test2", 1, "Interface/HUD/placeholder.png", ColorRGBA.Red);
    weaponList.addItem("Test3", 2, "Interface/HUD/placeholder.png", ColorRGBA.Yellow);
    weaponList.addItem("Test4", 3, "Interface/HUD/placeholder.png", ColorRGBA.Orange);
    weaponList.addItem("Test5", 2, "Interface/HUD/placeholder.png", ColorRGBA.Green);
    weaponList.show();
    
    parent.attachChild(hudNode);

    
    
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
      if (!name.equals(activeWeapon)) {
        activeWeapon = name;
        weaponIndicator.setValue(weaponBars.get(name).getValue());
      }
  }
  
  public void setAmmo(String weapon, int ammo) {
      weaponBars.get(weapon).setValue(ammo);
      if (weapon.equals(activeWeapon))
              setAmmo(ammo);
  }
  
  public void setItem(String item) {
      if (hudNode.getChild("item") != null)
        hudNode.detachChildNamed("item");
      if (items.containsKey(item))
        hudNode.attachChild(items.get(item));
  }
  
  public void removeItem(String item) {
      if (hudNode.getChild("item") != null) 
        hudNode.detachChild(items.get(item));
  }

  public void setPlayerName(String name) {
    playerName.setText(name);
  }

  public void setTeam(String team) {
    this.team.setText(team);
  }
  
  public IconList getWeaponList() {
      return weaponList;
  }
}