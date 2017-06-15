import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Onur
 */
public class KI {
 
    private float currentx;
    private float currenty;
    private float currentz;
    
    private Random rand;
    private boolean toleft;
    private boolean todown;
    private boolean toleft2;
    private boolean vertmode = true;
    private boolean hortmode = false;
    private boolean walktocenter = false;
    private int hortcount = 0;
    private int vertcount = 0;
    
    private ArrayList<Vector3f> spawnpoints;
    
    public KI()
    {
        spawnpoints = new ArrayList<Vector3f>();
        createSpawnSet();
        rand = new Random();
        toleft = false;
        toleft2 = false;
        todown = false;
    }
    
    public Vector3f getDirection(Vector3f current, String mode)
    {
        float x = 0f;
        float y = 0f;
        float z = 0f;
        
        if(walktocenter)
        {
           if(current.x < 0.01f)
           {
               print("Center reached");
               walktocenter = false;
           }
           if(current.x>0)
           {
               x = -1f;
           }
           else if(current.x<-1)
           {
               x = 1f;
           }
        }
        else
        {
            x = giveX(current.x,mode);
            z = giveZ(current.x, current.z);
        }
        return new Vector3f(x,y,z);
    }
    
    private float giveX(float c,String mode)
    {
        float walkdir = 0f;
        
        if(mode.equals("a"))
        {
          if(vertmode)
          {
            if(c<19 && !toleft)
            {
                walkdir = 1f;
                if(c>=18)
                {
                    toleft = true;
                    vertcount +=1;
                    if(vertcount>2)
                    {
                        vertmode = false;
                        hortmode = true;
                        vertcount = 0;
                        walktocenter = true;
                    }
                }
            }
            else if(toleft && c>-19)
            {
                walkdir = -1f;
                if(c<-18)
                {
                    toleft = false;
                    print("Switchting X to right");
                    vertcount+=1;
                }
            }
         }     
        }
        else if(mode.equals("b"))
        {
          if(vertmode)
          {
            if(c<9 && !toleft2)
            {
                walkdir = -2f;
                if(c>=8.9f)
                {
                    print("Switching B");
                    toleft2 = true;
                    //vertcount +=1;
                    if(vertcount>2)
                    {
                        vertmode = false;
                        hortmode = true;
                        vertcount = 0;
                        walktocenter = true;
                    }
                }
            }
            else if(toleft2 && c>-9)
            {
                walkdir = 2f;
                if(c<=-8f)
                {
                    toleft2 = false;
                    vertcount+=1;
                }
            }
         }     
        }
        return walkdir;
    }
    
    
    private float giveZ(float cx,float cz)
    {
        float walkdir = 0f;
        
        if(vertmode && !walktocenter)
        {
            if(cz>1)
            {
                walkdir = -2f;
            }
            else if (cz<-1)
            {
                walkdir = 2f;
            }
            else
            {
                walkdir = 0f;
            }
        }
        if(hortmode && !walktocenter)
        {
            if(cz<19 && !todown)
            {
                walkdir = 1f;
                
                if(cz>17.97f)
                {
                    todown = true;
                    print("Switching Z to downward");
                    hortcount+=1;
                    if(hortcount>2)
                    {
                        hortcount = 0;
                        hortmode = false;
                        vertmode = true;
                        walktocenter = true;
                    }
                }
            }
            else if(cz>-18 &&todown)
            {
                walkdir = -1f;
                if(cz<-17.1f)
                {
                    todown = false;
                    print("Switchung Z to upward");
                }
            }
        }
        return walkdir;
    }
    
    
  
    public Vector3f makeSpawnpoint()
    {
        return spawnpoints.get(rand.nextInt(4));
    }
    
    private void createSpawnSet()
    {
        Vector3f v1 = new Vector3f(-10f,2f,0f);
        Vector3f v2 = new Vector3f(-18f,2f,0f);
        Vector3f v3 = new Vector3f(18f,2f,0f);
        Vector3f v4 = new Vector3f(0f,2f,18f);
        Vector3f v5 = new Vector3f(0f,2f,-18f);
        
        spawnpoints.add(v1);
        spawnpoints.add(v2);
        spawnpoints.add(v3);
        spawnpoints.add(v4);
        spawnpoints.add(v5);
    }
    
    private void print(Object s)
    {
        System.out.println(s);
    }
}
