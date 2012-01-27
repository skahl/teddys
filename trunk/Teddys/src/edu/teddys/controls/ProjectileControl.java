/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.controls;

import com.jme3.bullet.control.RigidBodyControl;
import edu.teddys.effects.GunShot;

/**
 *
 * @author besient, skahl
 */
public class ProjectileControl extends RigidBodyControl {
    
    private GunShot projectile;
    
    public ProjectileControl(GunShot projectile) {
        this.projectile = projectile;
    }
    
    
    
}
