
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;


/**
 *
 * @author Onur
 */
public class CollManager implements PhysicsCollisionListener{
 
    private int hurtcount = 0;
    private boolean hit = false;
    private boolean soundfound1;
    private boolean soundfound2;
    private boolean soundfound3;
    
    public void collision(PhysicsCollisionEvent event) {
        if(event.getNodeA().getName().contains("npc") && event.getNodeB().getName().contains("player"))
        {
            this.hurtcount+=1;
            this.hit = true;
        }
        if(event.getNodeA().getName().contains("Sound") && event.getNodeB().getName().contains("player"))
        {
            print(event.getNodeA().getName());
            if(event.getNodeA().getName().contains("1"))
            {
                soundfound1 = true;
            }
            else if(event.getNodeA().getName().contains("2"))
            {
                soundfound2 = true;
            }
            else if(event.getNodeA().getName().contains("3"))
            {
                soundfound3 = true;
            }
        }
    }
    
    public boolean checkHit()
    {
        return this.hit;
    }
    
    public boolean checkSoundFound(int i)
    {
        boolean b = false;
        if(i==1)
            b = this.soundfound1;
        else if(i==2)
            b =  this.soundfound2;
        else if(i==3)
            b = this.soundfound3;
        return b;
    }
    
    public void markSoundFound(int i)
    {
        if (i==1)
            this.soundfound1 = false;
        if (i==2)
            this.soundfound2 = false;
        if (i==3)
            this.soundfound3 = false;
    }
    
    public void resetHit()
    {
        this.hit = false;
    }
    
    public int getHurtCount()
    {
        return this.hurtcount/10;
    }
    
    public void resetHurtCount()
    {
        print("Hurtcount resetted");
        this.hurtcount = 0;
    }
    
    private void print(Object s)
    {
        System.out.println(s);
    }



}
