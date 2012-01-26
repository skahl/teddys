
package edu.teddys.effects;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author skahl
 */
public class JetpackEffect {
    Node mother;
    ParticleEmitter pe;
    Material mat;
    AssetManager assetManager;
    String name;
    float particlesPerSec;
    
    boolean enabled;
    
    public JetpackEffect(String name, AssetManager assetManager) {
        this.name = name;
        this.assetManager = assetManager;
        particlesPerSec = 30f;
        enabled = false;
        
        mother = new Node("node_"+name);
        pe = new ParticleEmitter("jetPack",
                ParticleMesh.Type.Triangle, 40);
        mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        
        init();
    }
    
    private void init() {
        
        mat.setTexture("Texture", assetManager.loadTexture("Textures/Effects/Smoke/Smoke.png"));
        pe.setMaterial(mat);
        pe.setImagesX(15); pe.setImagesY(1);
        pe.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
        pe.setStartColor(new ColorRGBA(1f, 1f, 0.5f, 1f)); // yellow-ish
        pe.getParticleInfluencer().setInitialVelocity(new Vector3f(-1f,-4f,0f));
        pe.setStartSize(0.25f);
        pe.setEndSize(0.08f);
        pe.setGravity(0,0,0);
        pe.setLowLife(0.2f);
        pe.setHighLife(0.4f);
        pe.getParticleInfluencer().setVelocityVariation(0.1f);  
        pe.setParticlesPerSec(0f);
        
        
        mother.attachChild(pe);
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enable) {
        if(!enable) {
            pe.setParticlesPerSec(0);
            enabled = false;
        } else {
            pe.setParticlesPerSec(particlesPerSec);
            enabled = true;
        }
    }
    
    public Node getNode() {
        return mother;
    }
    
}
