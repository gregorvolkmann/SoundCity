/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Einstellungen für die BGM-Musik
 * 
 */
public class Sound {
    
    private String filepath;
    private boolean bgm;
    private boolean loop;
    private float volume;
    
    /**
     * Konstruktor
     * Einstellungen
     */
    public Sound(String path) 
    {
        this.filepath = path;
        this.bgm = true;
        this.loop = false;
        this.volume = 2;
    }
    
    public String getFilepath() {
        return filepath;
    }
    
    public boolean isBGM() {
        return bgm;
    }
    
    public boolean isLoop() {
        return loop;
    }
    
    public float getVolume() {
        return volume;
    }
    
    public void update() {
        
    }
    
}
