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
import edu.teddys.BaseGame;

/**
 *
 * @author besient
 */
public class HorizontalIconList extends IconList {
    
  private BaseGame app;
    
    
    public HorizontalIconList(BaseGame app, float entrySize, float x, float y, AssetManager assetManager, Node parent) {
        super(entrySize, x, y, assetManager, parent);
        this.app = app;
    }

    @Override
    public void addItem(String name, Integer position, String path, ColorRGBA color) {
        
        Picture frame = new Picture("frame");
        frame.setImage(assetManager, "Interface/HUD/frame.png", true);
        frame.setLocalTranslation(x + position*(2.5f*pictureSize), y, -1);
        frame.setWidth(2.5f*pictureSize);
        frame.setHeight(pictureSize);
        //listNode.attachChild(frame);
        
        if (!indices.contains(name)) {
        Picture picture = new Picture(name);
        picture.setImage(assetManager, path, true);
        picture.setLocalTranslation(x + position*(2.5f*pictureSize), y, 0);
        picture.setWidth(pictureSize);
        picture.setHeight(pictureSize);
        
        
        
        BarIndicator bar = new BarIndicator(app, pictureSize, 
                pictureSize/5, 
                x + position*(2.5f*pictureSize) + 1.25f*pictureSize, 
                y + 0.4f*pictureSize, 
                assetManager, color, listNode);
        
        if ((position != null) && (position <= indices.size())) {
            indices.add(position, name);
            pictures.put(name, picture);
            bars.put(name, bar);
            frames.put(name, frame);
        }
        listNode.attachChild(picture);
        
        
        for (int i = position+1; i < indices.size(); i++) {
           pictures.get(indices.get(i)).move(2.5f*pictureSize, 0, 0);
           bars.get(indices.get(i)).getSpatial().move(2.5f*pictureSize, 0, 0);
           frames.get(indices.get(i)).move(2.5f*pictureSize, 0, 0);
        }
        
    }
    }

    @Override
    public void removeItem(String name) {
       int index = indices.indexOf(name);
       Picture p = pictures.get(name);
       Spatial b = bars.get(name).getSpatial();
       Picture f = frames.get(name);
       
       for (int i = index; i < indices.size(); i++) {
           pictures.get(indices.get(i)).move(-2.5f*pictureSize, 0, 0);
           bars.get(indices.get(i)).getSpatial().move(-2.5f*pictureSize, 0, 0);
           frames.get(indices.get(i)).move(-2.5f*pictureSize, 0, 0);
       }
       
       listNode.detachChild(p);
       listNode.detachChild(b);
       listNode.detachChild(f);
       indices.remove(index);
       pictures.remove(name);
       bars.remove(name);
       frames.remove(name);
    }
    
    
}
