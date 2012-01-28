
package edu.teddys.effects;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;



/**
 *
 * @author skahl
 */
public class ShotBaerenpistole extends GunShot {
    
    public ShotBaerenpistole(String name, AssetManager assetManager) {
        
        // init Baerenpistole
        this.init(name, assetManager, "Textures/Effects/kugel_pistole.png");
            
        setColor(new ColorRGBA(1f, 1f, 1f, 1f));
        setSize(0.1f);
        setLifeTime(1f);
        setNumParticles(1);
    }
    
    
}
