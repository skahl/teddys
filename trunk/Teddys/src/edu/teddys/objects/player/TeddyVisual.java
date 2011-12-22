
package edu.teddys.objects.player;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture.WrapMode;

/**
 *
 * @author skahl
 */
public class TeddyVisual {
    
    Box box;
    Geometry geo;
    Material mat;
    
    public TeddyVisual(Node node, AssetManager assetManager) {

        box = new Box(0.3f, 0.3f, 0.01f);
        geo = new Geometry(node.getName(), box);
        
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Textures/teddy.png", true)));
        //mat.getTextureParam("ColorMap").getTextureValue().setWrap(WrapMode.EdgeClamp);
        //mat.getAdditionalRenderState().setAlphaFallOff(0.5f);
        mat.getAdditionalRenderState().setAlphaTest(true);
        //mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        //mat.getAdditionalRenderState().setPointSprite(true);
        //mat.getAdditionalRenderState().set
        
        geo.setMaterial(mat);
        geo.setShadowMode(ShadowMode.Cast);
        geo.setQueueBucket(Bucket.Transparent);
        
        node.attachChild(geo);
    }
    
    public Geometry getGeo() {
        return geo;
    }

    public Material getMat() {
        return mat;
    }
    
    public float getWidth() {
        return box.getXExtent();
    }
    
    public float getHeight() {
        return box.getYExtent();
    }

    public Box getBox() {
        return box;
    }
}
