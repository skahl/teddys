/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.input;

import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import edu.teddys.GameSettings;
import java.util.EnumMap;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 *
 * @author besient
 */
public class InputSettings {

    private Map<ActionControllerEnum, Trigger[]> actions;
    private Map<AnalogControllerEnum, Trigger[]> analogs;
    private Map<ActionControllerEnum, String> actionNames;
    private Map<AnalogControllerEnum, String> analogNames;
    private static InputSettings instance = null;
    private final String path = GameSettings.TITLE + " Input Settings";
    private Preferences settings;
    private final String keyPrefix = "Key ";
    private final String mousePrefix = "Mouse ";
    private final String nameSuffix = "_NAME";
    private final String wheelUp = "Wheel UP";
    private final String wheelDown = "Wheel DOWN";

    private InputSettings() {

        actions = new EnumMap<ActionControllerEnum, Trigger[]>(ActionControllerEnum.class);
        analogs = new EnumMap<AnalogControllerEnum, Trigger[]>(AnalogControllerEnum.class);

        actionNames = new EnumMap<ActionControllerEnum, String>(ActionControllerEnum.class);
        analogNames = new EnumMap<AnalogControllerEnum, String>(AnalogControllerEnum.class);

        ActionControllerEnum[] actionEnum = ActionControllerEnum.values();
        AnalogControllerEnum[] analogEnum = AnalogControllerEnum.values();

        for (int i = 0; i < actionEnum.length; i++) {
            actions.put(actionEnum[i], actionEnum[i].getKeys());
        }
        for (int i = 0; i < analogEnum.length; i++) {
            analogs.put(analogEnum[i], analogEnum[i].getKeys());
        }

        settings = Preferences.userRoot().node(path);



        for (ActionControllerEnum e : actions.keySet()) {
            String s = settings.get(e.name(), "");
            if (!s.equals("")) {
                if (s.charAt(0) == 'k') {
                    actions.put(e, new Trigger[]{new KeyTrigger(Integer.valueOf(s.substring(1)))});
                } else if (s.charAt(0) == 'm') {
                    actions.put(e, new Trigger[]{new MouseButtonTrigger(Integer.valueOf(s.substring(1)))});
                } else if (s.equals(wheelUp)) {
                    actions.put(e, new Trigger[]{new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true)});
                } else if (s.equals(wheelDown)) {
                    actions.put(e, new Trigger[]{new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false)});
                }
            }
            String n = settings.get(e.name() + nameSuffix, "");
            if (!n.equals("")) {
                actionNames.put(e, n);
            }
        }

        for (AnalogControllerEnum e : analogs.keySet()) {
            String s = settings.get(e.name(), "");
            if (!s.equals("")) {
                if (s.charAt(0) == 'k') {
                    analogs.put(e, new Trigger[]{new KeyTrigger(Integer.valueOf(s.substring(1)))});
                } else {
                    analogs.put(e, new Trigger[]{new MouseButtonTrigger(Integer.valueOf(s.substring(1)))});
                }
            }
            String n = settings.get(e.name() + nameSuffix, "");
            if (!n.equals("")) {
                analogNames.put(e, n);
            }
        }

    }

    public static InputSettings getInstance() {
        if (instance == null) {
            instance = new InputSettings();
        }

        return instance;
    }

    public void saveSettings() {
        for (ActionControllerEnum e : actions.keySet()) {
            if (actions.get(e)[0].getClass().equals(KeyTrigger.class)) {
                settings.put(e.name(), "k" + ((KeyTrigger) actions.get(e)[0]).getKeyCode());
            } else if (actions.get(e)[0].getClass().equals(MouseButtonTrigger.class)) {
                settings.put(e.name(), "m" + ((MouseButtonTrigger) actions.get(e)[0]).getMouseButton());
            } else {
                if (((MouseAxisTrigger)actions.get(e)[0]).isNegative()) {
                    settings.put(e.name(), wheelDown);
                } else {
                    settings.put(e.name(), wheelUp);
                }
                    
            }

            settings.put(e.name() + nameSuffix, actionNames.get(e));

        }

        for (AnalogControllerEnum e : analogs.keySet()) {
            if (analogs.get(e)[0].getClass().equals(KeyTrigger.class)) {
                settings.put(e.name(), "k" + ((KeyTrigger) analogs.get(e)[0]).getKeyCode());
            } else {
                settings.put(e.name(), "m" + ((MouseButtonTrigger) analogs.get(e)[0]).getMouseButton());
            }

            settings.put(e.name() + nameSuffix, analogNames.get(e));

        }


    }

    
    private void clearActionValue(Map<ActionControllerEnum, Trigger[]> map, Trigger[] value) {
        for (ActionControllerEnum a: map.keySet()) {
            if ((map.get(a).length > 0) && map.get(a)[0].getName().equals(value[0].getName())) {
                map.put(a, new Trigger[]{});
                actionNames.put(a, "");
            }
            
        }        
    }
    
    private void clearAnalogValue(Map<AnalogControllerEnum, Trigger[]> map, Trigger[] value) {
        for (AnalogControllerEnum a: map.keySet()) {
            if ((map.get(a).length > 0) && map.get(a)[0].getName().equals(value[0].getName())) {
                map.put(a, new Trigger[]{});
                analogNames.put(a, "");
            }
        }        
    }
    
    
    public Trigger[] getKey(AnalogControllerEnum analog) {
        return analogs.get(analog);
    }

    public Trigger[] getKey(ActionControllerEnum action) {
        return actions.get(action);
    }

    public String getName(AnalogControllerEnum analog) {
        return analogNames.get(analog);

    }

    public String getName(ActionControllerEnum action) {
        return actionNames.get(action);

    }

    public void setKey(AnalogControllerEnum analog, int key, String keyCode) {
        Trigger[] trigger = new Trigger[]{new KeyTrigger(key)};
        clearAnalogValue(analogs, trigger);            
        analogs.put(analog, trigger);
        analogNames.put(analog, keyPrefix + keyCode.toUpperCase());
    }

    public void setKey(ActionControllerEnum action, int key, String keyCode) {
        Trigger[] trigger = new Trigger[]{new KeyTrigger(key)};
        clearActionValue(actions, trigger);  
        actions.put(action, trigger);
        actionNames.put(action, keyPrefix + keyCode.toUpperCase());
        
    }

    public void setMouseButton(AnalogControllerEnum analog, int key) {
        Trigger[] trigger = new Trigger[]{new MouseButtonTrigger(key)};
        clearAnalogValue(analogs, trigger);
        analogs.put(analog, new Trigger[]{new MouseButtonTrigger(key)});
        analogNames.put(analog, mousePrefix + String.valueOf(++key));
    }

    public void setMouseButton(ActionControllerEnum action, int key) {
        Trigger[] trigger = new Trigger[]{new MouseButtonTrigger(key)};
        clearActionValue(actions, trigger);
        actions.put(action, trigger);
        actionNames.put(action, mousePrefix + String.valueOf(++key));
    }
    
    public void setMouseWheel(ActionControllerEnum action, boolean dir) {
        Trigger[] trigger = new Trigger[]{new MouseAxisTrigger(MouseInput.AXIS_WHEEL, dir)};
        if (dir)             
            actionNames.put(action, wheelUp);
         else
            actionNames.put(action, wheelDown);
        
        actions.put(action, trigger);
    }
}