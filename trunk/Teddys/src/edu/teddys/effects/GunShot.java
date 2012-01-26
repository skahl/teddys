
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
public class GunShot {
    Node mother;
    ParticleEmitter pe;
    Material mat;
    AssetManager assetManager;
    
    
    protected void init(String name, AssetManager assetManager, String texture) {
        
        this.assetManager = assetManager;
        mother = new Node(name+"_gun");
        pe = new ParticleEmitter(name+"_gunshots", ParticleMesh.Type.Triangle, 1);
        mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        
        mat.getAdditionalRenderState().setAlphaTest(true);
        
        mat.setTexture("Texture", assetManager.loadTexture(texture));
        pe.setMaterial(mat);
        pe.setImagesX(1); pe.setImagesY(1);
        pe.setEndColor(new ColorRGBA(0f, 0f, 0f, 1f));   // red
        pe.setStartColor(new ColorRGBA(0f, 0f, 0f, 1f)); // yellow-ish
        pe.getParticleInfluencer().setInitialVelocity(new Vector3f(10f,0f,0f));
        pe.setStartSize(0.25f);
        pe.setEndSize(0.25f);
        pe.setGravity(0,0,0);
        pe.setLowLife(0.9f);
        pe.setHighLife(1f);
        pe.getParticleInfluencer().setVelocityVariation(0f);
        pe.setParticlesPerSec(0.0f);
        
        
        mother.attachChild(pe);
    }
    
    public void setVector(Vector3f initialVector) {
        pe.getParticleInfluencer().setInitialVelocity(initialVector);
    }
    
    public void setNumParticles(int numParticles) {
        pe.setNumParticles(numParticles);
    }
    
    public void setLifeTime(float life) {
        pe.setLowLife(life);
        pe.setHighLife(life);
    }
    
    public void setGravity(float x, float y, float z) {
        pe.setGravity(x, y, z);
    }
    
    public void setSize(float factor) {
        pe.setStartSize(factor);
        pe.setEndSize(factor);
    }
    
    public Node getNode() {
        return mother;
    }
    
    public ParticleEmitter getParticleEmitter() {
        return pe;
    }
    
    public void shoot() {
        pe.emitAllParticles();
    }
}
