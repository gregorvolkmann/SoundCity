package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    private float frustumSize = 1;
    protected Geometry player;
    
    @Override
    public void simpleInitApp() {
        // setup environment variables
        speed = 50;
        flyCam.setMoveSpeed(50.0f);
        
        // switch camera into 2DMode
        rootNode.setCullHint(Spatial.CullHint.Never);
        cam.setParallelProjection(true);
        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustum(-1000, 1000, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);
        
        // create player
        Box playerShape = new Box(Vector3f.ZERO, 1, 1, 1);
        player = new Geometry("Box", playerShape);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        player.setMaterial(mat);
        
        // create world
        Box worldShape = new Box(Vector3f.ZERO, 100, 1, 100);
        Geometry world = new Geometry("world", worldShape);
        
        Material mat_stl = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex_ml = assetManager.loadTexture("Scenes/WorldScene.jpg");
        mat_stl.setTexture("ColorMap", tex_ml);
        world.setMaterial(mat_stl);
        
        world.setLocalTranslation(0, -2.0f, 0);
        
        // add some light
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        
        // spawn objects
        rootNode.attachChild(player);
        rootNode.attachChild(world);
        rootNode.addLight(sun);
        
        // align world to view
        rootNode.rotate(FastMath.DEG_TO_RAD*90, 0, 0);
        rootNode.scale(0.01f);
        
        // custom movement (unfinished!)
        initKeys();
    }
    
    private void initKeys() {
        inputManager.clearMappings();
        
        inputManager.addMapping("fwd", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("bwd", new KeyTrigger(KeyInput.KEY_G));
        
        inputManager.addMapping("lft", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("rgt", new KeyTrigger(KeyInput.KEY_H));
        
        inputManager.addListener(analogListener, new String[] {"fwd", "bwd", "lft", "rgt"});
    }

    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            if(name.equals("fwd")) {
                Vector3f v = player.getLocalTranslation();
                player.setLocalTranslation(v.x, v.y, v.z - value*speed);
            }
            if (name.equals("bwd")) {
                Vector3f v = player.getLocalTranslation();
                player.setLocalTranslation(v.x, v.y, v.z + value*speed);
            }
            if (name.equals("lft")) {
                Vector3f v = player.getLocalTranslation();
                player.setLocalTranslation(v.x - value*speed, v.y, v.z);
            }
            if (name.equals("rgt")) {
                Vector3f v = player.getLocalTranslation();
                player.setLocalTranslation(v.x + value*speed, v.y, v.z);
            }
        }
    };
    
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
