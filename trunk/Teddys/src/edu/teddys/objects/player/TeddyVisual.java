
package edu.teddys.objects.player;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import edu.teddys.MegaLogger;
import edu.teddys.effects.GunShot;
import edu.teddys.effects.JetpackEffect;
import edu.teddys.effects.ShotBaerenpistole;

/**
 *
 * @author skahl
 */
public class TeddyVisual {
    
    // control attributes
    boolean isRunning;
    boolean runLeft;
    
    // visual attributes
    Quad quad;
    Geometry geo;
    Material standing;
    Material running;
    
    // effect attributes
    JetpackEffect jetpackFx;
    GunShot currentWeapon;
    
    
    public TeddyVisual(Node node, AssetManager assetManager) {
        // control init
        isRunning = false;
        runLeft = false;
        
        // effect init
        jetpackFx = new JetpackEffect(node.getName(), assetManager);
        jetpackFx.getNode().setLocalTranslation(-0.3f, -0.25f, 0.0f);
        node.attachChild(jetpackFx.getNode());
        
        // gun init
        currentWeapon = new ShotBaerenpistole(node.getName(), assetManager);
        currentWeapon.getNode().setLocalTranslation(0.2f, 0.1f, 0f);
        node.attachChild(currentWeapon.getNode());
        
        // quad and materials init
        quad = new Quad(0.6f, 0.79f); //Box(0.3f, 0.3f, 0.01f);
        geo = new Geometry(node.getName(), quad);
        geo.setLocalTranslation(new Vector3f(-0.3f,-0.3f, 0.0f));
        
        // texture and material for standing teddy
        Texture blueStand = assetManager.loadTexture("Textures/teddy_blue_baerenpistole_stand.png");
        
        standing = assetManager.loadMaterial("Materials/teddyStand/teddyStand.j3m");
        standing.getAdditionalRenderState().setAlphaTest(true);
        standing.setTexture("TexMap", blueStand);
        standing.setInt("SelectedTile", 3);
        standing.setInt("MaxTiles", 7);
        standing.setBoolean("Mirrored", false);
        
        // texture and material for running teddy (with animation)
        Texture blueRun = assetManager.loadTexture("Textures/teddy_blue_baerenpistole_run.png");
        
        running = assetManager.loadMaterial("Materials/teddyRun/teddyRun.j3m");
        running.getAdditionalRenderState().setAlphaTest(true);
        running.setTexture("TexMap", blueRun);
        running.setInt("SelectedTile", 3);
        running.setInt("MaxTilesX", 7);
        running.setInt("MaxTilesY", 4);
        running.setFloat("Speed", 8.0f);
        running.setBoolean("Mirrored", false);
        running.setBoolean("Reverse", false);
        
        
        geo.setMaterial(standing);
        
        // shadow and transparency settings
        geo.setShadowMode(ShadowMode.Cast);
        geo.setQueueBucket(Bucket.Transparent);
        
        node.attachChild(geo);
    }
    
    public Geometry getGeo() {
        return geo;
    }

    public Material getMat() {
        return geo.getMaterial();
    }
    
    public float getWidth() {
        return quad.getWidth();//.getXExtent();
    }
    
    public float getHeight() {
        return quad.getHeight();//.getYExtent();
    }

    public Quad getBox() {
        return quad;
    }
    
    public GunShot getWeapon() {
        return currentWeapon;
    }
    
    public JetpackEffect getJetpack() {
        return jetpackFx;
    }
    
    public void run() {
        if(!isRunning) {
            isRunning = true;
            geo.setMaterial(running);
        }
    }
    
    public void runLeft() {
        if(!runLeft) {
            runLeft=true;
        }
        
        run();
    }
    
    public void runRight() {
        if(runLeft) {
            runLeft=false;
        }
        
        run();
    }
    
    public void stand() {
        if(isRunning) {
            isRunning = false;
            
            geo.setMaterial(standing);
        }
    }
    
    public void setViewVector(Vector2f vector) {
        
        boolean mirrored;
        int selectedTile;

        if(vector.x > 0f) {
           mirrored = false;
        } else {
           mirrored = true;
        }
            
            
        if(vector.y > 0f) {

           if(vector.y > 0.5f) {
               // check 1st quadrant cases
               if(vector.y > 0.825f) {
                   selectedTile = 0;
               } else {
                   selectedTile = 1;
               }
           } else {
               // check 2nd quadrant cases
               if(vector.y > 0.165f) {
                   selectedTile = 2;
               } else {
                   selectedTile = 3;
               }
           }

        } else {

           if(vector.y > -0.5f) {
               // check 3rd quadrant cases
               if(vector.y > -0.165f) {
                   selectedTile = 3;
               } else {
                   selectedTile = 4;
               }
           } else {
               // check 4th quadrant cases
               if(vector.y > -0.825f) {
                   selectedTile = 5;
               } else {
                   selectedTile = 6;
               }
           }

        }
        
        //MegaLogger.getLogger().info(new Throwable("selectedTile: "+String.valueOf(selectedTile)+" Vector-Y: "+String.valueOf(vector.y)));
        
        // set shader control
        standing.setInt("SelectedTile", selectedTile);
        standing.setBoolean("Mirrored", mirrored);
        running.setInt("SelectedTile", selectedTile);
        running.setBoolean("Mirrored", mirrored);
        
        if(mirrored) {
            if(runLeft) {
                running.setBoolean("Reverse", false);
            } else {
                running.setBoolean("Reverse", true);
            }
            
            // move jetpack
            jetpackFx.getNode().setLocalTranslation(0.30f, -0.25f, 0.0f);
            jetpackFx.switchVelocity();
        } else {
            if(runLeft) {
                running.setBoolean("Reverse", true);
            } else {
                running.setBoolean("Reverse", false);
            }
            
            // move jetpack
            jetpackFx.getNode().setLocalTranslation(-0.30f, -0.25f, 0.0f);
            jetpackFx.switchVelocity();
        }
    }
}
