
package edu.teddys.effects;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;



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
        setLifeTime(0.3f);
        setNumParticles(1);
        setVelocity(20f);//10f);
    }
    
    
}
