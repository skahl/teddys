
package edu.teddys.effects;

import com.jme3.math.ColorRGBA;
import edu.teddys.states.Game;



/**
 *
 * @author skahl
 */
public class StenGunShot extends GunShot {
    
    public StenGunShot() {
        
        // init Baerenpistole
        this.init("Sten Gun", Game.getInstance().getAssetManager(), "Textures/Effects/kugel_pistole.png");
            
        setColor(new ColorRGBA(1f, 1f, 1f, 1f));
        setSize(0.1f);
        setLifeTime(0.3f);
        setNumParticles(1);
        setVelocity(20f);//10f);
    }
    
    
}
