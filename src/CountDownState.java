import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Node;
import com.jme3.system.NanoTimer;

/**Countdown AppState at start of play
 *
 * @author Gregor
 */
public class CountDownState extends AbstractAppState {
    // General
    private Application app;
    private AssetManager assetManager;
    private InputManager inputManager;
    // GUI
    private Node guiNode;
    private BitmapFont guiFont;
    private BitmapText countText;
    private int viewPortWidth, viewPortHeight;
    // Timer
    private NanoTimer timer;
    private float countDownTime, time;

    public CountDownState(SimpleApplication app, float countDownTime) {
        this.app = app;
        this.guiNode = app.getGuiNode();
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        // timer & his variables
        this.timer = new NanoTimer();
        this.countDownTime = countDownTime;
        this.time = 0.0f;
        // GUI init
        this.viewPortWidth = 1152;
        this.viewPortHeight = 864;
        this.guiFont = this.assetManager.loadFont("Interface/Fonts/Default.fnt");
        this.countText = new BitmapText(guiFont, false);
        this.countText.setName("CountText");
        this.countText.setSize(100);
        this.countText.setText("Get Ready");
        this.countText.setLocalTranslation((viewPortWidth - countText.getLineWidth()) / 2, (viewPortHeight - countText.getLineHeight()) / 2, 0);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.guiNode.attachChild(countText);
        this.timer.reset();
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        // finished countdown
        if(timer.getTimeInSeconds() >= this.countDownTime) {
            this.countText.setText("");
            this.guiNode.detachChildNamed("CountText");
            setupMapping();
            this.setEnabled(false);
        }
        // update countdown seconds
        if(timer.getTimeInSeconds() - this.time >= 1.0f) {
            if((int)(this.countDownTime - this.timer.getTimeInSeconds()) < 1) {
                this.countText.setText("Go!");
            } else {
                this.countText.setText(String.valueOf((int)(this.countDownTime - this.timer.getTimeInSeconds())));
                this.countText.setLocalTranslation((viewPortWidth - countText.getLineWidth()) / 2, (viewPortHeight - countText.getLineHeight()) / 2, 0);
                this.time += 1.0f;
            }
        }
    }
    
    /**
     * Key Settings - Trigger
     */
    public void setupMapping() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Replace", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("Mute", new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("DebugMode", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("chunk1", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("chunk2", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("chunk3", new KeyTrigger(KeyInput.KEY_3));
    }
}
