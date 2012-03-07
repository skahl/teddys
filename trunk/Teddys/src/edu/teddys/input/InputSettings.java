/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.input;

import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import edu.teddys.GameSettings;
import java.util.Arrays;
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
                } else {
                    actions.put(e, new Trigger[]{new MouseButtonTrigger(Integer.valueOf(s.substring(1)))});
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
            } else {
                settings.put(e.name(), "m" + ((MouseButtonTrigger) actions.get(e)[0]).getMouseButton());
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
     
            
        analogs.put(analog, trigger);
        analogNames.put(analog, keyPrefix + keyCode.toUpperCase());
    }

    public void setKey(ActionControllerEnum action, int key, String keyCode) {
        actions.put(action, new Trigger[]{new KeyTrigger(key)});
        actionNames.put(action, keyPrefix + keyCode.toUpperCase());
    }

    public void setMouseButton(AnalogControllerEnum analog, int key) {
        analogs.put(analog, new Trigger[]{new MouseButtonTrigger(key)});
        analogNames.put(analog, mousePrefix + String.valueOf(++key));
    }

    public void setMouseButton(ActionControllerEnum action, int key) {
        actions.put(action, new Trigger[]{new MouseButtonTrigger(key)});
        actionNames.put(action, mousePrefix + String.valueOf(++key));
    }
}