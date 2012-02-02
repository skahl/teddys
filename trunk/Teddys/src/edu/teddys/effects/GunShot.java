
package edu.teddys.effects;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import edu.teddys.MegaLogger;

/**
 *
 * @author skahl
 */
public class GunShot {
    Node mother;
    ParticleEmitter pe;
    Material mat;
    AssetManager assetManager;
    int numParticles;
    boolean shooting;
    float velocity;
    Vector3f vector;
    
    
    protected void init(String name, AssetManager assetManager, String texture) {
        
        this.assetManager = assetManager;
        vector = new Vector3f();
        
        numParticles = 1;
        shooting = false;
        
        mother = new Node(name+"_gun");
        pe = new ParticleEmitter(name+"_gunshots", ParticleMesh.Type.Triangle, 1);
        mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture(texture));
        mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        
        pe.setMaterial(mat);
        pe.setImagesX(1); pe.setImagesY(1);
        pe.setEndColor(new ColorRGBA(0f, 0f, 0f, 1f));
        pe.setStartColor(new ColorRGBA(0f, 0f, 0f, 1f));
        pe.getParticleInfluencer().setInitialVelocity(new Vector3f(10f, 0f, 0f));
        pe.setStartSize(0.25f);
        pe.setEndSize(0.25f);
        pe.setGravity(0,0,0);
        pe.setLowLife(0.5f);
        pe.setHighLife(0.5f);
        pe.getParticleInfluencer().setVelocityVariation(0f);  
        pe.setParticlesPerSec(0);
        pe.setInWorldSpace(true);
        pe.setFacingVelocity(true);
        
        mother.attachChild(pe);
    }
    
    public void setColor(ColorRGBA rgba) {
        pe.setStartColor(rgba);
        pe.setEndColor(rgba);
    }
    
    public void setVelocity(float velocity) {
        this.velocity = velocity;
    } 
    
    public void setVector(Vector3f initialVector) {
        
        vector = initialVector;
        
        //mother.rotateUpTo(initialVector.cross(Vector3f.UNIT_Z).normalize());
        
        
        pe.getParticleInfluencer().setInitialVelocity(vector.mult(velocity));
        
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
        if(!shooting) {
            shooting = true;
            pe.emitAllParticles(); // for immediate shot
            
            pe.setParticlesPerSec(numParticles);
        }
    }
    
    public void stop() {
        shooting = false;
        pe.setParticlesPerSec(0);
    }
}
