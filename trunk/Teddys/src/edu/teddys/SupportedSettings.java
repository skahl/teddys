/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys;

import com.jme3.system.AppSettings;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.prefs.BackingStoreException;

/**
 *
 * taken from com.jme3.app.SettingsDialog.java
 */
public class SupportedSettings {

    private static final DisplayMode[] modes;

    static {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        modes = device.getDisplayModes();
        Arrays.sort(modes, new DisplayModeSorter());
    }
    
    public static boolean verifyAndSaveCurrentSelection(AppSettings source, String display, String depthString, String aaString, String freqString, boolean fullscreen, boolean vsync) {

        int width = Integer.parseInt(display.substring(0, display.indexOf(" x ")));
        display = display.substring(display.indexOf(" x ") + 3);
        int height = Integer.parseInt(display);

        int depth = -1;
        if (depthString.equals("???")) {
            depth = 0;
        } else {
            depth = Integer.parseInt(depthString.substring(0, depthString.indexOf(' ')));
        }


        int freq = -1;
        if (fullscreen) {
            if (freqString.equals("???")) {
                freq = 0;
            } else {
                freq = Integer.parseInt(freqString.substring(0, freqString.indexOf(' ')));
            }
        }

        int multisample = -1;
        if (aaString.equals("Disabled")) {
            multisample = 0;
        } else {
            multisample = Integer.parseInt(aaString.substring(0, aaString.indexOf('x')));
        }

        // FIXME: Does not work in Linux
        /*
         * if (!fullscreen) { //query the current bit depth of the desktop int
         * curDepth = GraphicsEnvironment.getLocalGraphicsEnvironment()
         * .getDefaultScreenDevice().getDisplayMode().getBitDepth(); if (depth >
         * curDepth) { showError(this,"Cannot choose a higher bit depth in
         * windowed " + "mode than your current desktop bit depth"); return
         * false; } }
         */

        String renderer = "LWJGL-OpenGL2";//(String) rendererCombo.getSelectedItem();

        boolean valid = false;

        // test valid display mode when going full screen
        if (!fullscreen) {
            valid = true;
        } else {
            GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            valid = device.isFullScreenSupported();
        }

        if (valid) {
            //use the GameSettings class to save it.
            source.setWidth(width);
            source.setHeight(height);
            source.setBitsPerPixel(depth);
            source.setFrequency(freq);
            source.setFullscreen(fullscreen);
            source.setVSync(vsync);
            //source.setRenderer(renderer);
            source.setSamples(multisample);

            String appTitle = source.getTitle();

            try {
                source.save(appTitle);
            } catch (BackingStoreException ex) {
                
            }
        } else {
            
        }

        return valid;
    }


    /**
     * Returns every unique resolution from an array of <code>DisplayMode</code>s.
     */
    public static String[] getResolutions() {
        ArrayList<String> resolutions = new ArrayList<String>(modes.length);
        for (int i = 0; i < modes.length; i++) {
            String res = modes[i].getWidth() + " x " + modes[i].getHeight();
            if (!resolutions.contains(res)) {
                resolutions.add(res);
            }
        }

        String[] res = new String[resolutions.size()];
        resolutions.toArray(res);
        return res;
    }

    /**
     * Returns every possible bit depth for the given resolution.
     */
    public static String[] getDepths(String resolution) {
        ArrayList<String> depths = new ArrayList<String>(4);
        for (int i = 0; i < modes.length; i++) {
            // Filter out all bit depths lower than 16 - Java incorrectly
            // reports
            // them as valid depths though the monitor does not support them
            if (modes[i].getBitDepth() < 16 && modes[i].getBitDepth() > 0) {
                continue;
            }

            String res = modes[i].getWidth() + " x " + modes[i].getHeight();
            String depth = modes[i].getBitDepth() + " bpp";
            if (res.equals(resolution) && !depths.contains(depth)) {
                depths.add(depth);
            }
        }

        if (depths.size() == 1 && depths.contains("-1 bpp")) {
            // add some default depths, possible system is multi-depth supporting
            depths.clear();
            depths.add("24 bpp");
        }

        String[] res = new String[depths.size()];
        depths.toArray(res);
        return res;
    }

    /**
     * Returns every possible refresh rate for the given resolution.
     */
    public static String[] getFrequencies(String resolution) {
        ArrayList<String> freqs = new ArrayList<String>(4);
        for (int i = 0; i < modes.length; i++) {
            String res = modes[i].getWidth() + " x " + modes[i].getHeight();
            String freq;
            if (modes[i].getRefreshRate() == DisplayMode.REFRESH_RATE_UNKNOWN) {
                freq = "???";
            } else {
                freq = modes[i].getRefreshRate() + " Hz";
            }

            if (res.equals(resolution) && !freqs.contains(freq)) {
                freqs.add(freq);
            }
        }

        String[] res = new String[freqs.size()];
        freqs.toArray(res);
        return res;
    }
    
    public static String[] getAAs() {
        String[] choices = new String[]{"Disabled", "2x", "4x", "6x", "8x", "16x"};
        return choices;
    }

    private static class DisplayModeSorter implements Comparator<DisplayMode> {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(DisplayMode a, DisplayMode b) {
            // Width
            if (a.getWidth() != b.getWidth()) {
                return (a.getWidth() > b.getWidth()) ? 1 : -1;
            }
            // Height
            if (a.getHeight() != b.getHeight()) {
                return (a.getHeight() > b.getHeight()) ? 1 : -1;
            }
            // Bit depth
            if (a.getBitDepth() != b.getBitDepth()) {
                return (a.getBitDepth() > b.getBitDepth()) ? 1 : -1;
            }
            // Refresh rate
            if (a.getRefreshRate() != b.getRefreshRate()) {
                return (a.getRefreshRate() > b.getRefreshRate()) ? 1 : -1;
            }
            // All fields are equal
            return 0;
        }
    }
}
