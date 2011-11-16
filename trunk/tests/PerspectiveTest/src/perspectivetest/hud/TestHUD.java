package perspectivetest.hud;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * 
 * @author besient
 */
public class TestHUD extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private SimpleApplication app;
    
    private int health = 97;
    
    public int getHealth() {
        return health;
    }
  
    public void bind(Nifty nifty, Screen screen) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onStartScreen() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}