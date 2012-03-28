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

/**
 * Implementation of an icon list arranging its entries vertically
 * 
 * @author besient
 */
public class VerticalIconList extends IconList {

    /**
     * {@inheritDoc} 
     */
    public VerticalIconList(float entrySize, float x, float y, AssetManager assetManager, Node parent) {
        super(entrySize, x, y, assetManager, parent);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void addItem(String name, Integer position, String path, ColorRGBA color) {
        if (!indices.contains(name)) {
            Picture picture = new Picture(name);
            picture.setImage(assetManager, path, true);
            picture.setLocalTranslation(x, y - (position + 1) * pictureSize, 0);
            picture.setWidth(pictureSize);
            picture.setHeight(pictureSize);

            BarIndicator bar = new BarIndicator(pictureSize,
                    pictureSize / 5,
                    x + 1.25f * pictureSize,
                    y - (position + 1) * pictureSize + pictureSize / 2,
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

    /**
     * {@inheritDoc} 
     */
    @Override
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
}
