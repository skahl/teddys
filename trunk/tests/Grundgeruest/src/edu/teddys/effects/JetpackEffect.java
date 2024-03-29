/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.effects;

import com.jme3.asset.AssetManager;
import com.jme3.effect.Particle;
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
        particlesPerSec = 10f;
        enabled = true;
        
        mother = new Node("node_"+name);
        pe = new ParticleEmitter("jetPack",
                ParticleMesh.Type.Triangle, 20);
        mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        
        init();
    }
    
    private void init() {
        
        mat.setTexture("Texture", assetManager.loadTexture("Textures/Effects/Smoke/Smoke.png"));
        
        pe.setMaterial(mat);
        pe.setImagesX(15); pe.setImagesY(1);
        pe.setEndColor(new ColorRGBA(.7f, .7f, .7f, .6f));   // gray
        pe.setStartColor(new ColorRGBA(.9f, .9f, .9f, .8f)); // white
        pe.getParticleInfluencer().setInitialVelocity(new Vector3f(0,0,0));
        pe.setStartSize(1f);
        pe.setEndSize(0.2f);
        pe.setGravity(0,0,0);
        pe.setLowLife(0.5f);
        pe.setHighLife(1.5f);
        pe.getParticleInfluencer().setVelocityVariation(0.1f);  
        pe.setParticlesPerSec(particlesPerSec);
        
        
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
    
    public void pause() {
        pe.setEnabled(false);
    }
    
    public void unpause() {
        pe.setEnabled(true);
    }
}
