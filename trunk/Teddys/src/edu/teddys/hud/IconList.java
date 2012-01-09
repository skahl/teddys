/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.hud;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.ui.Picture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author besient
 */
public class IconList {
    
   private List<String> indices; 
   private Map<String, Picture> pictures;
   private Map<String, BarIndicator> bars;
   
   private float pictureSize, x, y;
   
   private AssetManager assetManager;
   private Node listNode;
   
   
   public IconList(float entrySize, float x, float y, AssetManager assetManager, Node parent) {
       this.assetManager = assetManager;
       this.x = x;
       this.y = y;
       pictureSize = entrySize;
       
       indices = new ArrayList<String>();
       pictures = new HashMap<String, Picture>();
       bars = new HashMap<String, BarIndicator>();
       listNode = new Node("List");
       parent.attachChild(listNode);
   }
   
   public void addItem(String name, Integer position, String path, ColorRGBA color) {
       
    if (!indices.contains(name)) {
        Picture picture = new Picture(name);
        picture.setImage(assetManager, path, true);
        picture.setLocalTranslation(x, y - (position+1)*pictureSize, 0);
        picture.setWidth(pictureSize);
        picture.setHeight(pictureSize);
        
        BarIndicator bar = new BarIndicator(pictureSize, 
                pictureSize/5, 
                x + 1.25f*pictureSize, 
                y - (position+1)*pictureSize + pictureSize/2, 
                assetManager, color, listNode);
        
        if ((position != null) && (position <= indices.size())) {
            indices.add(position, name);
            pictures.put(name, picture);
            bars.put(name, bar);
        }
        listNode.attachChild(picture);
        
        for (int i = position; i < indices.size(); i++) {
           pictures.get(indices.get(i)).move(0, -pictureSize, 0);
           bars.get(indices.get(i)).getSpatial().move(0, -pictureSize, 0);
        }
    }
   }
   
   public void removeItem(String name) {
       
       int index = indices.indexOf(name);
       Picture p = pictures.get(name);
       Spatial b = bars.get(name).getSpatial();
       
       for (int i = index; i < indices.size(); i++) {
           pictures.get(indices.get(i)).move(0, pictureSize, 0);
           bars.get(indices.get(i)).getSpatial().move(0, pictureSize, 0);
       }
       
       listNode.detachChild(p);
       listNode.detachChild(b);
       indices.remove(index);
       pictures.remove(name);
       bars.remove(name);
       
   }
   
   public void setValue(String name, int percentage) {
       bars.get(name).setValue(percentage);
   }

}
