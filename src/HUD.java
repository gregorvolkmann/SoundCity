import com.jme3.asset.AssetManager;
import com.jme3.ui.Picture;
import java.util.ArrayList;

/**
 *
 * @author Onur
 */
public class HUD{
    
    private ArrayList<Picture> al;
    private int stresswidth = 10;
    private int currentNoteHUD = 0;
    
    public ArrayList<Picture> genHUDElements(AssetManager am, int w, int h)
    {
        al = new ArrayList<Picture>();
        
        Picture controls = new Picture("Controls");
        controls.setImage(am, "HUD/Controls.png", true);
        controls.setWidth(150);
        controls.setHeight(116);
        controls.setPosition(w-170, h-840);
        al.add(controls);
        
        Picture logo = new Picture("Logo");
        logo.setImage(am,"HUD/SoundCityLogo.png",true);
        logo.setWidth(200);
        logo.setHeight(49);
        logo.setPosition(w-210, h-50);
        al.add(logo);
        
        Picture stressText = new Picture("StressText");
        stressText.setImage(am, "HUD/StressText.png", true);
        stressText.setWidth(196);
        stressText.setHeight(53);
        stressText.setPosition(w-1140, h-70);
        al.add(stressText);
        
        Picture stressBar = new Picture("StressBar");
        stressBar.setImage(am, "HUD/StressBar.png", true);
        stressBar.setWidth(stresswidth);
        stressBar.setHeight(20);
        stressBar.setPosition(w-1130, h-90);
        al.add(stressBar);
        
        Picture soundFound = new Picture("SoundFound0");
        soundFound.setImage(am, "HUD/Soundfound0.png", true);
        soundFound.setWidth(274);
        soundFound.setHeight(112);
        soundFound.setPosition(w-720, h-130);
        al.add(soundFound);
        
        return al;
    }

    public Picture extendStress(AssetManager am, int w, int h)
    {
        this.stresswidth +=20;
        Picture stressBar = new Picture("StressBar");
        stressBar.setImage(am, "HUD/StressBar.png", true);
        stressBar.setWidth(stresswidth);
        stressBar.setHeight(20);
        stressBar.setPosition(w-1130, h-90);
        return stressBar;
    }
    
    public int getStress()
    {
        return this.stresswidth;
    }
    
    public Picture nextLevel(AssetManager am, int w, int h, int level)
    {
        Picture soundFound = new Picture("SoundFound"+level);
        soundFound.setImage(am, "HUD/Soundfound"+level+".png", true);
        soundFound.setWidth(274);
        soundFound.setHeight(112);
        soundFound.setPosition(w-720, h-130);
        return soundFound;
    }
    
    public Picture makeGameOver(AssetManager am,int w, int h)
    {
        Picture go = new Picture("GameOver");
        go.setImage(am, "HUD/GO.png", true);
        go.setWidth(600);
        go.setHeight(226);
        go.setPosition(w-850, h/2);
        return go;
    }
     
    public Picture makeWin(AssetManager am,int w, int h)
    {
        Picture go = new Picture("Win");
        go.setImage(am, "HUD/win.png", true);
        go.setWidth(600);
        go.setHeight(452);
        go.setPosition(w-850, h-600);
        return go;
    }

    public int getCurrentNoteHUDLevel()
    {
        return this.currentNoteHUD;
    }
    
    public void resetStress()
    {
        this.currentNoteHUD-=100;
    }
    
}
