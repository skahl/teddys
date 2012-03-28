/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.hud;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for lists of icons with an optional indicator bar.
 * 
 * @author besient
 */
public abstract class IconList {
    
   protected List<String> indices; 
   protected Map<String, Picture> pictures;
   protected Map<String, BarIndicator> bars;
   protected Map<String, Picture> frames;
   
   protected float pictureSize, x, y;
   
   protected AssetManager assetManager;
   protected Node listNode, parent;
   
   private int highlightedItem = -1;
   
   /**
    * Contructor
    * 
    * @param entrySize The icon's size
    * @param x The x coordinate of list's head
    * @param y The y coordinate of the lists head
    * @param assetManager 
    * @param parent The parent node, the list will be attached to
    */
   public IconList(float entrySize, float x, float y, AssetManager assetManager, Node parent) {
       this.assetManager = assetManager;
       this.x = x;
       this.y = y;
       pictureSize = entrySize;
       this.parent = parent;
       
       indices = new ArrayList<String>();
       pictures = new HashMap<String, Picture>();
       bars = new HashMap<String, BarIndicator>();
       frames = new HashMap<String, Picture>();
       listNode = new Node("List");
   }
   
   /**
    * Add an item to the list
    * @param name The item's id
    * @param position The position in the
    * @param path The path to the icon file
    * @param color The color of the indicator bar
    */
   public abstract void addItem(String name, Integer position, String path, ColorRGBA color);
   
   /**
    * Remove an item from the list
    * @param name 
    */
   public abstract void removeItem(String name);
   
   /**
    * Set the value of the indicator bar for the specified entry
    * @param name The entry id
    * @param percentage 
    */
   public void setValue(String name, int percentage) {
       bars.get(name).setValue(percentage);
   }
   
   /**
    * Attach the list to the scene graph
    */
   public void show() {
       if (!parent.hasChild(listNode))
           parent.attachChild(listNode);
   }
   
   /**
    * Detach the list from the scene graph
    */
   public void hide() {
       if (parent.hasChild(listNode))
           parent.detachChild(listNode);
   }
   
   /**
    * Highlight a list entry
    * @param index 
    */
   public void highlight(int index) {
        if ((index >= 0) && (index < indices.size())) {
            if (highlightedItem >= 0) {
                listNode.detachChild(frames.get(indices.get(highlightedItem)));
            }
            listNode.attachChild(frames.get(indices.get(index)));
            highlightedItem = index;
        }
    }
    
   /**
    * Highlight the entry next to the one currently highlighted
    */
    public void highlightNext() {
        if (highlightedItem == indices.size() - 1) {
            highlight(0);
        } else {
            highlight(highlightedItem + 1);
        }
    }
    
    /**
    * Highlight the entry previous to the one currently highlighted
    */
    public void highlightPrevious() {
        if (highlightedItem <= 0) {
            highlight(indices.size() - 1);
        } else {
            highlight(highlightedItem - 1);
        }
    }
   
   

}
