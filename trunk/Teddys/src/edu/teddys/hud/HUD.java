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
import edu.teddys.objects.weapons.DeafNut;
import edu.teddys.objects.weapons.Florets;
import edu.teddys.objects.weapons.HolyWater;
import edu.teddys.objects.weapons.HoneyBrew;
import edu.teddys.objects.weapons.Rocket;
import edu.teddys.objects.weapons.SniperRifle;
import edu.teddys.objects.weapons.StenGun;
import edu.teddys.states.Game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The HUD containing indicator bars for health, ammo and jetpack energy, a message area, the player's name and team, 
 * the weapon list and the current item.
 * 
 * @author besient
 */
public class HUD {

    private Node hudNode, parent;
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
    private Map<String, Integer> weaponIndices;
    private IconList weaponList;
    private BitmapText playerName, team;
    private final String bar = "Interface/HUD/bar_bar_white.png";
    private final String bar_tail = "Interface/HUD/bar_tail_white.png";
    private final String bar_icon_health = "Interface/HUD/bar_icon_health_white.png";
    private final String bar_icon_jetpack = "Interface/HUD/bar_icon_jetpack_white.png";
    private final String bar_icon_weapon = "Interface/HUD/bar_icon_weapon_white.png";
    private static HUD instance = null;
    private final AssetManager assetManager;
    private final float width;
    private final float height;
    private final float itemXLocation;
    private final int itemYLocation;
    private final float weaponListXLocation;
    private final int weaponListYLocation;
    private final float teamListXLocation;
    private final float teamListYLocation;
    private VerticalIconList teamList;

    private String deafNutIcon = "Textures/Effects/deafnutParticle.png";
    private String flowerIcon = "Textures/Effects/flowerParticle.png";
    private String holyWaterIcon = "Textures/Effects/holywaterParticle.png";
    private String honeyBrewIcon = "Textures/Effects/HoneyBrew.png";
    private String pistoleIcon = "Textures/Effects/kugel_pistole.png";
    private String rocketIcon = "Textures/Effects/rocketParticle.png";
    
    private HUD(Node parent, AssetManager assetManager, float width, float height, GameModeEnum mode) {

        hudNode = new Node("hudNode");
        this.parent = parent;

        this.assetManager = assetManager;

        this.width = width;
        this.height = height;

        imageSize = (int) (height / 20);
        iconOffset = 3 * imageSize;
        hudHeight = height / 30;

        weaponXLocation = 0 + iconOffset;
        healthXLocation = width / 3 + iconOffset;
        jetpackXLocation = 2 * width / 3 + iconOffset;

        itemXLocation = width / 20;
        itemYLocation = 5 * imageSize;

        barWidth = imageSize * 2.5f;
        barHeight = imageSize / 2;
        tailWidth = imageSize / 12;
        barYOffset = imageSize / 4;

        weaponListXLocation = iconOffset;
        weaponListYLocation = 2 * imageSize;
        teamListXLocation = width - 4 * imageSize;
        teamListYLocation = 2 * height / 3;

        BitmapFont messageFont = assetManager.loadFont("Interface/Fonts/Waree.fnt");
        BitmapFont font = assetManager.loadFont("Interface/Fonts/Purisa32.fnt");


        Picture bottomBG = new Picture("BottomBG");
        bottomBG.setImage(assetManager, "Interface/HUD/grey.png", true);
        bottomBG.setWidth(2 * width / 3 + 67 / 12 * imageSize);
        bottomBG.setHeight(1.5f * imageSize);
        bottomBG.move(2.5f * imageSize, height / 30f - imageSize / 4.5f, 0);
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
        Picture item = new Picture("item");
        item.setImage(assetManager, "Interface/HUD/placeholder.png", true);
        item.setLocalTranslation(itemXLocation, itemYLocation, 0);
        item.setWidth(imageSize);
        item.setHeight(imageSize);
        //parent.attachChild(item);


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

        weaponIndices = new HashMap<String, Integer>();
        weaponIndices.put(StenGun.class.getName(), 0);
        weaponIndices.put(Rocket.class.getName(), 1);
        weaponIndices.put(DeafNut.class.getName(), 2);
        weaponIndices.put(Florets.class.getName(), 3);
        weaponIndices.put(HolyWater.class.getName(), 4);
        weaponIndices.put(HoneyBrew.class.getName(), 5);
        weaponIndices.put(SniperRifle.class.getName(), 6);
        
        weaponList = new HorizontalIconList(imageSize, iconOffset, 2 * imageSize, assetManager, hudNode);

        //test weapon list
        weaponList.addItem("pistole", 0, pistoleIcon, ColorRGBA.Yellow);
        weaponList.addItem("rocket", 1, rocketIcon, ColorRGBA.Yellow);
        weaponList.addItem("deafNut", 2, deafNutIcon, ColorRGBA.Yellow);
        weaponList.addItem("flower", 3, flowerIcon, ColorRGBA.Yellow);
        weaponList.addItem("holyWater", 4, holyWaterIcon, ColorRGBA.Yellow);
        weaponList.addItem("honeyBrew", 5, honeyBrewIcon, ColorRGBA.Yellow);
        weaponList.addItem("sniper", 6, pistoleIcon, ColorRGBA.Yellow);
        //weaponList.show();

        //parent.attachChild(hudNode);



        switch (mode) {
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
        teamList = new VerticalIconList(imageSize, teamListXLocation, teamListYLocation, assetManager, parent);
        teamList.addItem("Test1", 0, "Interface/HUD/placeholder.png", ColorRGBA.Blue);
        teamList.addItem("Test2", 1, "Interface/HUD/placeholder.png", ColorRGBA.Red);
        teamList.addItem("Test3", 2, "Interface/HUD/placeholder.png", ColorRGBA.Yellow);
        teamList.addItem("Test4", 3, "Interface/HUD/placeholder.png", ColorRGBA.Orange);
        teamList.addItem("Test5", 2, "Interface/HUD/placeholder.png", ColorRGBA.Green);
        //teamList.show();
    }

    /**
     * Attach the HUD to the scene graph
     */
    public void show() {
        if (!parent.hasChild(hudNode)) {
            //TODO add node
            Game.getInstance().addSpatial(parent, hudNode);
//        parent.attachChild(hudNode);
        }
    }

    /**
     * Detach the HUD from the scene graph
     */
    public void hide() {
        if (parent.hasChild(hudNode)) {
            //TODO add node
            Game.getInstance().removeSpatial(parent, hudNode);
//        parent.detachChild(hudNode);
        }
    }

    /**
     * Returns the singleton object.
     * @param parent The parent node, the HUD will be attached to
     * @param assetManager
     * @param width The horizontal screen resolution
     * @param height the vertical screen resolution
     * @param mode The game mode
     * @return The singleton object
     */
    public static HUD getInstance(Node parent, AssetManager assetManager, float width, float height, GameModeEnum mode) {
        if (instance == null) {
            instance = new HUD(parent, assetManager, width, height, mode);
        }
        return instance;
    }

    /**
     * Display some text in the message area
     * @param index The position in the message list
     * @param message 
     */
    public void setMessage(int index, String message) {
        messages.get(index).setText(message);
    }

    /**
     * Change the ammo of the currently active weapon
     * @param num 
     */
    public void setAmmo(int num) {
        weaponIndicator.setValue(num);
    }

    /**
     * Change the player's health
     * @param health 
     */
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

    
    /**
     * Change the jetpack energy
     * @param energy 
     */
    public void setJetpackEnergy(int energy) {
        jetpackIndicator.setValue(energy);
    }

    /**
     * Change the active weapon
     * @param name 
     */
    public void setActiveWeapon(String name) {
        if (!name.equals(activeWeapon)) {
            activeWeapon = name;
            weaponIndicator.setValue(weaponBars.get(name).getValue());
        }
    }
    
    public void selectWeapon(String name) {
        weaponList.highlight(weaponIndices.get(name));
    }

    /**
     * Change the ammo for the specified weapon
     * @param weapon
     * @param ammo 
     */
    public void setAmmo(String weapon, int ammo) {
        weaponBars.get(weapon).setValue(ammo);
        if (weapon.equals(activeWeapon)) {
            setAmmo(ammo);
        }
    }

    /**
     * Diplay the icon for a collected item
     * @param item 
     */
    public void setItem(String item) {
        if (hudNode.getChild("item") != null) {
            //TODO remove node
            Game.getInstance().removeSpatial(hudNode, hudNode.getChild("item"));
//      hudNode.detachChildNamed("item");
        }
        if (items.containsKey(item)) {
            Game.getInstance().addSpatial(hudNode, items.get(item));
            //TODO add node
//      hudNode.attachChild(items.get(item));
        }
    }
    /**
     * Remove the icon of an item
     * @param item 
     */
    public void removeItem(String item) {
        if (hudNode.getChild("item") != null) {
            Game.getInstance().removeSpatial(hudNode, items.get(item));
            //TODO remove node
//      hudNode.detachChild(items.get(item));
        }
    }

    /**
     * Set the player's display name
     * @param name 
     */
    public void setPlayerName(String name) {
        playerName.setText(name);
    }

    /**
     * Change the display name of the player's team
     * @param team 
     */
    public void setTeam(String team) {
        this.team.setText(team);
    }

    /**
     * Getter method for the weapon list
     * @return 
     */
    public IconList getWeaponList() {
        return weaponList;
    }

    /**
     * Getter method for the HUD's parent node
     * @return 
     */
    public Node getNode() {
        return hudNode;
    }
}
