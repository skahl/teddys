/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package perspectivetest.input;

import com.jme3.math.Vector2f;
import com.jme3.ui.Picture;

/**
 *
 * @author besient
 */
public class Cursor extends Picture {
    
    private float x, y, width, height;


    
    public Cursor(String name) {
        super(name);
    }
    
    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }
    
    public void setHeight(float height) {
        super.setHeight(height);
        this.height = height;
    }
    
    public void setWidth(float width) {
        super.setWidth(width);
        this.width = width;
    }
    
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        this.x = x;
        this.y = y;
    }
    
    public Vector2f getPosition() {
        return new Vector2f(x, y);
    }
    
    public void move(float x, float y) {
        this.x += x;
        this.y += y;
        super.setPosition(this.x, this.y);        
    }
    


}
