import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Collections;

/**Factory zum erstellen von SoundFragmenten
 * 
 * @author Gregor
 */
public class SoundFactory {
    private ArrayList<int[]> spawnPoints;
    private Spatial model;
    private CollisionShape shape;
    private RigidBodyControl rigid;
    private String name;
    
    SoundFactory(Spatial soundModel, String name) {
        // spawnpoints
        spawnPoints = new ArrayList<int[]>();
        spawnPoints.add(new int[]{3, 3});
        spawnPoints.add(new int[]{3, -3});
        spawnPoints.add(new int[]{-3, 3});
        spawnPoints.add(new int[]{-3, -3});
        spawnPoints.add(new int[]{3, 10});
        spawnPoints.add(new int[]{3, -10});
        spawnPoints.add(new int[]{10, 3});
        spawnPoints.add(new int[]{-10, 3});
        spawnPoints.add(new int[]{0, 0});
        Collections.shuffle(spawnPoints);
        // model
        this.model = soundModel;
        this.model.setName(name);
        this.model.scale(0.25f);
    }
    /**Generiert ein neues SoundFragment,
     * ein vorher bestehendes Fragment wird dabei dereferenziert.
     * 
     * @return Gibt wenn noch ein ungenutzter Spawnpunkt verfügbar war true zurück.
     */
    public boolean createSound() {
        if(spawnPoints.isEmpty()) {
            return false;
        }
        // new model
        this.model = this.model.clone();
        // generate spawnpoint
        int[] position = spawnPoints.remove(0);
        this.model.setLocalTranslation((int)position[0], 1f, (int)position[1]);
        // physics
        this.shape = CollisionShapeFactory.createBoxShape((Node) this.model);
        this.rigid = new RigidBodyControl(shape, 0);
        this.model.addControl(rigid);
        return true;
    }
    
    public Spatial getModel() {
        return this.model;
    }
    
    public RigidBodyControl getShape() {
        return this.rigid;
    }
}
