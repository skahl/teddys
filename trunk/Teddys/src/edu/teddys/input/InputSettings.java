/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.input;


import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.system.AppSettings;
import edu.teddys.GameSettings;
import java.util.EnumMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;

/**
 *
 * @author besient
 */
public class InputSettings {
    
    private Map<ActionControllerEnum, Trigger[]> actions;
    private Map<AnalogControllerEnum, Trigger[]> analogs;
    
    private static InputSettings instance = null;
    
    private final String path = GameSettings.TITLE;
    
    private InputSettings() {
        
        actions = new EnumMap<ActionControllerEnum, Trigger[]>(ActionControllerEnum.class);
        analogs = new EnumMap<AnalogControllerEnum, Trigger[]>(AnalogControllerEnum.class);
        
        ActionControllerEnum[] actionEnum = ActionControllerEnum.values();
        AnalogControllerEnum[] analogEnum = AnalogControllerEnum.values();
        
        for (int i = 0; i < actionEnum.length; i++) {
            actions.put(actionEnum[i], actionEnum[i].getKeys());
        }
        for (int i = 0; i < analogEnum.length; i++) {
            analogs.put(analogEnum[i], analogEnum[i].getKeys());
        } 
            
        
    }
    
    public static InputSettings getInstance() {
        if (instance == null)
            instance = new InputSettings();
        
        return instance;
    }
    
    public void loadSettings(AppSettings settings) {
        
//        try {
//            settings.load(path);
//        } catch (BackingStoreException ex) {
//            
//        }
        
        for (ActionControllerEnum e: actions.keySet()) {
            if (settings.containsKey(e.name())) {
                String s = settings.getString(e.name());
                if (s.charAt(0) == 'k') 
                    actions.put(e, new Trigger[]{new KeyTrigger(Integer.valueOf(s.substring(1)))});
                else
                    actions.put(e, new Trigger[]{new MouseButtonTrigger(Integer.valueOf(s.substring(1)))});
            }
        }
        
        for (AnalogControllerEnum e: analogs.keySet()) {
            if (settings.containsKey(e.name())) {
                String s = settings.getString(e.name());
                if (s.charAt(0) == 'k') 
                    analogs.put(e, new Trigger[]{new KeyTrigger(Integer.valueOf(s.substring(1)))});
                else
                    analogs.put(e, new Trigger[]{new MouseButtonTrigger(Integer.valueOf(s.substring(1)))});
            }
        }
    }
    
    public void saveSettings(AppSettings settings) {
        for (ActionControllerEnum e: actions.keySet()) {
            if (actions.get(e)[0].getClass().equals(KeyTrigger.class))
                settings.putString(e.name(), "k" + ((KeyTrigger)actions.get(e)[0]).getKeyCode());
            else 
                settings.putString(e.name(), "m" + ((MouseButtonTrigger)actions.get(e)[0]).getMouseButton());
            try {
                settings.save(path);
            } catch (BackingStoreException ex) {
                
            }
        }
        
        for (AnalogControllerEnum e: analogs.keySet()) {
            if (analogs.get(e)[0].getClass().equals(KeyTrigger.class))
                settings.putString(e.name(), "k" + ((KeyTrigger)analogs.get(e)[0]).getKeyCode());
            else 
                settings.putString(e.name(), "m" + ((MouseButtonTrigger)analogs.get(e)[0]).getMouseButton());
            try {
                settings.save(path);
            } catch (BackingStoreException ex) {
                
            }
        }
        
        
    }
    
    public Trigger[] getKey(AnalogControllerEnum analog) {
        return analogs.get(analog);
    }
    
    public Trigger[] getKey(ActionControllerEnum action) {
       return actions.get(action); 
    }
    
        
    public void setKey(AnalogControllerEnum analog, int key) {
        analogs.put(analog, new Trigger[]{new KeyTrigger(key)});
    }
    
    public void setKey(ActionControllerEnum action, int key) {
        actions.put(action, new Trigger[]{new KeyTrigger(key)});
    }
    
    public void setMouseButton(AnalogControllerEnum analog, int key) {
        analogs.put(analog, new Trigger[]{new MouseButtonTrigger(key)});
    }
    
    public void setMouseButton(ActionControllerEnum action, int key) {
        actions.put(action, new Trigger[]{new MouseButtonTrigger(key)});
    }

    
    
}
