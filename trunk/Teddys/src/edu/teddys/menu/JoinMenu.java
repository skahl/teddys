/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.menu;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import edu.teddys.network.NetworkSettings;
import edu.teddys.network.TeddyClient;

/**
 *
 * @author besient
 */
public class JoinMenu extends MessagePopupController {
    
    private TextField ipField;
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen);
        
        ipField = screen.findNiftyControl("IP_TEXT_FIELD", TextField.class);
        ipField.setText(NetworkSettings.DEFAULT_SERVER+":"+NetworkSettings.SERVER_PORT);
    }
    
    public void connect() {
        String[] connectData = ipField.getText().split(":");
        if (connectData.length != 0) {
            TeddyClient.getInstance().join(connectData[0], Integer.parseInt(connectData[1]));
        }
    }
    
    public void back() {
        nifty.gotoScreen(MenuTypes.MAIN_MENU.name());
    }
    
    
}
