/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 
 
package edu.teddys.input;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author besient
 */
public class Cursor extends Picture {
    
    private static Cursor instance = null;
    
    private float x, y, width, height;
    //private Map<String, Texture> textureMap;
    //private AssetManager assetManager;
    private Vector2f hotspot;


    
    private Cursor(String name) {
        super(name);
        //this.assetManager = assetManager;
        //textureMap = new HashMap<String, Texture>();
        hotspot = new Vector2f();
        
        /*
        Texture cursorTex = assetManager.loadTexture("Textures/fadenkreuz.png");
        setTexture(assetManager, (Texture2D) cursorTex, true);
        setHeight(64);
        setWidth(64);
        */
    }
    
    public static Cursor getInstance(String name) {
        if (instance == null) {
            instance = new Cursor(name);
        }
        return instance;
    }
    
    public void setHotspot(float x, float y) {
        hotspot.x = x;
        hotspot.y = y;
    }
    
    public Vector2f getHotspot() {
        return hotspot;
    }
    
    /*public void loadTexture(String name, String image) {
        textureMap.put(name, assetManager.loadTexture(image));
    }
    
    public void setTexture(String name) {
        setTexture(assetManager, (Texture2D) textureMap.get(name), true);
    }*/
    
    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }
    
    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        this.height = height;
    }
    
    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        this.width = width;
    }
    
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        this.x = x;
        this.y = y;
    }
    
    public Vector2f getPosition() {
        return new Vector2f(x, y);
    }
    
    public void move(float x, float y) {
        this.x += x;
        this.y += y;
        super.setPosition(this.x, this.y);        
    }
    


}
