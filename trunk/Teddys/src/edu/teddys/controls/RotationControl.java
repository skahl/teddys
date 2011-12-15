
package edu.teddys.controls;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 *
 * @author skahl
 */
public class RotationControl extends AbstractControl {
    private boolean paused;
    
    private Float speed;
    private float tpf;
    static private Future future = null;
    private ScheduledThreadPoolExecutor threadPool;
    
    
    // threaded Callable
    private Callable<Float> rotateThis = new Callable<Float>() {
        public Float call() throws Exception {
            
            // TODO: DATA FROM OUTSIDE SHOULD GET CLONED BEFORE IT IS USED!
            
            
            Float newspeed = 1f*tpf;
            
            return newspeed;
        }
    };
    
    
    public RotationControl(ScheduledThreadPoolExecutor threadPool) {
        this.threadPool = threadPool;
        paused = false;
        speed = 0f;
        tpf = 1f;
    }

    @Override
    protected void controlUpdate(float tpf) {
        this.tpf = tpf;
        
        if(!paused) {
            try {
                if (future == null) {
                    future = threadPool.submit(rotateThis);
                } else if (future.isDone()) {
                    speed = (Float)future.get();
                    future = null;
                } else if (future.isCancelled()) {
                    future = null;
                }
            }
            catch(Exception e){ 
                e.printStackTrace();
            }

            if(speed != null) {
                spatial.rotate(0f, speed, speed);
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public Control cloneForSpatial(Spatial spatial) {
        RotationControl control = new RotationControl(threadPool);
        control.setSpeed(speed);
        control.setSpatial(spatial);
        
        return control;
    }
    
    
    
    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    
    
    
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
