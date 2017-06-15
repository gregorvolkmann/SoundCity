import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.Intersection;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends SimpleApplication implements ActionListener {
    private KI ki;
    private SoundFactory sf1;
    private SoundFactory sf2;
    private SoundFactory sf3;
    private BulletAppState bulletAppState;
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    // states
    private CountDownState cds;
    
    // rigids
    private CharacterControl player;
    private RigidBodyControl world;
    private RigidBodyControl npcRigid;
    private RigidBodyControl npcRigid2;
    
    //Collision
    private CollManager collmanager;
    
    // variables
    private Vector3f walkDirection = new Vector3f();
    private float x = 0,z=0;
    private boolean left=false,right=false,up=false,down=false,space=false;
    private boolean replace=false, mute=false,isPlayingSound=true,resume=false;
    private boolean debugState = false, chunk1b=false, chunk2b=false, chunk3b=false;
    private boolean pause = false;
    private int found;
    
    // nodes
    private Node playerNode = new Node("player");
    private Node npcNode = new Node ("npcNode1");
    private Node npcNode2 = new Node ("npcNode2");
    private Node soundNode1 = new Node("Sound1");
    private Node soundNode2 = new Node("Sound2");
    private Node soundNode3 = new Node("Sound3");
    
    // models
    private Spatial worldModel;
    private Spatial playerModel;
    private Spatial npc1;
    private Spatial npc2;
    
    private Spatial soundModel1;
    private Spatial soundModel2;
    private Spatial soundModel3;

    
    // audio
    private AudioNode audio_music;
    private AudioNode footSteps;
    private AudioNode stressBing;
    private AudioNode respawned;
    private AudioNode bumped;
    private AudioNode soundfound;
    private AudioNode chunk1;
    private AudioNode chunk2;
    private AudioNode chunk3;
    private AudioNode finalsong;
    private AudioNode gameover;
    
    //HUD
    private HUD hud;
    
    private static Main app;
    public static void main(String[] args) {
        app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        cds = new CountDownState(this, 10.0f);
        stateManager.attach(cds);
        setDisplayStatView(false);
        ki = new KI();
        found = 0;
        
        showHUD();
        createWorld();
        createPlayer();
        createLight();
        createNPC();
        
        initCam(playerModel);
        initAudio();
        initSky();
        setupListener();
        speed = 9.0f;
              
        
        // ### song spawning test ###
        logger.log(Level.INFO, 
                "worldXLength: {0} \n worldZLength: {1}", 
                new Object[] {world.getPhysicsSpace().getWorldMax().x - world.getPhysicsSpace().getWorldMin().x, 
                    world.getPhysicsSpace().getWorldMax().z - world.getPhysicsSpace().getWorldMin().z});
        
        // some car
        Spatial car = assetManager.loadModel("Models/car/car.j3o");
        car.setLocalScale(0.7f);
        car.setLocalTranslation(17.0f, 0.5f, -1f);
        rootNode.attachChild(car);
        
        soundModel1 = assetManager.loadModel("Models/sound/sound.j3o");
        soundModel2 = assetManager.loadModel("Models/sound/sound.j3o");
        soundModel3 = assetManager.loadModel("Models/sound/sound.j3o");
        rootNode.attachChild(soundNode1);
        rootNode.attachChild(soundNode2);
        rootNode.attachChild(soundNode3);
        sf1 = new SoundFactory(soundModel1,"Sound1");
        sf2 = new SoundFactory(soundModel2,"Sound2");
        sf3 = new SoundFactory(soundModel3,"Sound3");
        spawnSound(sf1);
        spawnSound(sf2);
        spawnSound(sf3);
        
        cds.setEnabled(true);
    }
    
    /**
     * Himmel und Hintergrund wird generiert
     */
    private void initSky() {
        Texture west = assetManager.loadTexture("Scenes/Univ.png");
        Texture east = assetManager.loadTexture("Scenes/Univ.png");
        Texture north = assetManager.loadTexture("Scenes/Univ.png");
        Texture south = assetManager.loadTexture("Scenes/Univ.png");
        Texture up = assetManager.loadTexture("Scenes/Univ.png");
        Texture down = assetManager.loadTexture("Scenes/Univ.png");

        Spatial skyBox = SkyFactory.createSky(assetManager, west, east, north, south, up, down);
        rootNode.attachChild(skyBox);
    }

    /**
     * Methode um die Musikklasse zu initialisieren und eine BGM-Audio Node zu generieren
     * Einstellungen werden von der Musikklasse bezogen
     * Hier spielt die Musik!
     */
    private void initAudio() {
        Sound sdtrck = new Sound("Sounds/song.ogg");
        audio_music = new AudioNode(assetManager, sdtrck.getFilepath(), sdtrck.isBGM());
        audio_music.setLooping(sdtrck.isLoop());
        audio_music.setVolume(0.4f);
        audio_music.play();     
        
        Sound steps = new Sound("Sounds/footstepsmarching.wav");
        footSteps = new AudioNode(assetManager, steps.getFilepath(), false);
        footSteps.setLooping(true);
        footSteps.setVolume(0.5F);
        footSteps.setRefDistance(0.05F);
        footSteps.play();
        
        Sound stressSound = new Sound("Sounds/stress.ogg");
        stressBing = new AudioNode(assetManager, stressSound.getFilepath(), false);
        stressBing.setLooping(false);
        stressBing.setVolume(2);
        rootNode.attachChild(stressBing);
        
        Sound respawn = new Sound("Sounds/respawn.ogg");
        respawned = new AudioNode(assetManager, respawn.getFilepath(), false);
        respawned.setLooping(false);
        respawned.setVolume(2);
        respawned.attachChild(respawned);
        
        Sound bump = new Sound("Sounds/hit.ogg");
        bumped = new AudioNode(assetManager, bump.getFilepath(), false);
        bumped.setLooping(false);
        bumped.setVolume(2);
        rootNode.attachChild(bumped);
        
        Sound sf = new Sound("Sounds/soundfound.ogg");
        soundfound = new AudioNode(assetManager, sf.getFilepath(), false);
        soundfound.setLooping(false);
        soundfound.setVolume(2);
        rootNode.attachChild(soundfound);
        
        Sound c1 = new Sound("Sounds/chunk1.ogg");
        chunk1 = new AudioNode(assetManager, c1.getFilepath(), false);
        chunk1.setLooping(false);
        chunk1.setVolume(0.07f);
        rootNode.attachChild(chunk1);
        
        Sound c2 = new Sound("Sounds/chunk2.ogg");
        chunk2 = new AudioNode(assetManager, c2.getFilepath(), false);
        chunk2.setLooping(false);
        chunk2.setVolume(0.07f);
        rootNode.attachChild(chunk2);
        
        Sound c3 = new Sound("Sounds/chunk3.ogg");
        chunk3 = new AudioNode(assetManager, c3.getFilepath(), false);
        chunk3.setLooping(false);
        chunk3.setVolume(0.07f);
        rootNode.attachChild(chunk3);
        
        Sound fs = new Sound("Sounds/the_beatles-help.ogg");
        finalsong = new AudioNode(assetManager, fs.getFilepath(), false);
        finalsong.setLooping(false);
        finalsong.setVolume(2);
        rootNode.attachChild(finalsong);
        
        Sound go = new Sound("Sounds/gameover.ogg");
        gameover = new AudioNode(assetManager, go.getFilepath(), false);
        gameover.setLooping(false);
        gameover.setVolume(2);
        rootNode.attachChild(gameover);
     }
    
    /**
     * Initialisiert die Kamera und die ChaseCam
     * Kamera soll den Player folgen
     * @param player Das Playermodelobject
     */
    private void initCam(Spatial player) {
        // Enable a chase cam
        ChaseCamera chaseCam = new ChaseCamera(cam, player, inputManager);
        chaseCam.setTrailingEnabled(false);
        chaseCam.setDefaultHorizontalRotation(FastMath.DEG_TO_RAD*(90));
        chaseCam.setDefaultVerticalRotation(FastMath.DEG_TO_RAD*(45));
        cam.setLocation(player.getLocalTranslation().add(0f, 10f, 0f));
    }
    
    /**
     * Licht in der Welt positionieren
     */
    private void createLight() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);
    }
    
    /**
     * Map wird geladen
     */
    private void createWorld() {
        worldModel = assetManager.loadModel("Scenes/city/sound_city.j3o");
        worldModel.setLocalScale(2.0f);
        CollisionShape worldcshape = CollisionShapeFactory.createMeshShape(worldModel);
        world = new RigidBodyControl(worldcshape);
        world.setMass(0f);
        world.setFriction(1f);
        bulletAppState.getPhysicsSpace().add(world);
        worldModel.addControl(world);
        rootNode.attachChild(worldModel);
    }
    
    /**
     * Player wird erstellt
     */
    private void createPlayer() {
        // create model
        playerModel = assetManager.loadModel("Models/player/player.j3o");
        playerNode.attachChild(playerModel);
        playerModel.move(0f, -1f, 0);
        // create physics
        CapsuleCollisionShape playerShape = new CapsuleCollisionShape(0.5f, 1f, 1);
        
        player = new CharacterControl(playerShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(25);
        player.setGravity(30);
        // merge playermodel and playerphysics
        rootNode.attachChild(playerNode);
        playerNode.addControl(player);
        
        player.setPhysicsLocation(playerModel.getLocalTranslation());
        bulletAppState.getPhysicsSpace().add(player);
    }
    
    /**
     * NPC generieren
     * @param loc Location
     */
    private void createNPC() {
        npc1 = assetManager.loadModel("Models/npc/npc.j3o");
        npcNode.attachChild(npc1);
        CollisionShape noteshape = CollisionShapeFactory.createDynamicMeshShape(npc1);
        npcRigid = new RigidBodyControl(noteshape,60.0f);
        npcRigid.setFriction(0f);
        npcRigid.setMass(10f);
        npcRigid.setPhysicsLocation(ki.makeSpawnpoint());
        collmanager = new CollManager();
        bulletAppState.getPhysicsSpace().addCollisionListener(collmanager);
        
        npc1.addControl(npcRigid);
        bulletAppState.getPhysicsSpace().add(npcRigid);
        rootNode.attachChild(npcNode);
    }
    
    /**
     * Key Settings - Trigger
     */
    public void setupListener() {
        inputManager.addMapping("resume", new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addListener(this,"Left");
        inputManager.addListener(this,"Right");
        inputManager.addListener(this,"Up");
        inputManager.addListener(this,"Down");
        inputManager.addListener(this,"Space");
        inputManager.addListener(this,"Replace");
        inputManager.addListener(this,"Mute");
        inputManager.addListener(this,"Space");
        inputManager.addListener(this, "DebugMode");
        inputManager.addListener(this,"chunk1");
        inputManager.addListener(this,"chunk2");
        inputManager.addListener(this,"chunk3");
        inputManager.addListener(this,"resume");
    }

    /**
     * Was passieren soll wenn welche Taste gedrückt wird
     * @param key Taste
     * @param pressed Key gedrückt
     * @param tpf 
     */
    @Override
    public void onAction(String key, boolean pressed, float tpf) {
        
        if (key.equals("Left")) { left = pressed; }
        else if (key.equals("Right")) { right = pressed; }
        else if (key.equals("Up")) { up = pressed; }
        else if (key.equals("Down")) { down = pressed; }
        else if (key.equals("Replace")) { replace = pressed; }
        else if (key.equals("Mute")) { mute = pressed; }
        else if (key.equals("Space")) { space = pressed; }
        else if (key.equals("chunk1")) { chunk1b = pressed; }
        else if (key.equals("chunk2")) { chunk2b = pressed; }
        else if (key.equals("chunk3")) { chunk3b = pressed; }
        else if (key.equals("resume")) { resume = pressed; }
        else if (key.equals("DebugMode") && !pressed) {
            debugState = !debugState;
            bulletAppState.getPhysicsSpace().enableDebug(assetManager);
            rootNode.depthFirstTraversal(new SceneGraphVisitor() {
                public void visit(Spatial spatial) {
                    if (spatial instanceof Geometry) {
                        ((Geometry)spatial).getMaterial().getAdditionalRenderState().setWireframe(debugState);
                    }
                }
            });
        }
    }

    /**
     * Methode zum ständigen Überprüfen und Ausführen von Aktionen
     * @param tpf 
     */
    @Override
    public void simpleUpdate(final float tpf) {
        
        if(resume)
        {
            System.out.println("Resuming");
            
            finalsong.stop();
            audio_music.play();
            guiNode.detachChildNamed("Win");
            found = 2;
            hud.resetStress();
            pause = false;
            resume = false;
        }
        
        if(!pause)
        {
            fallCheck();
            moveNPC();
            checkFindings();
            checkStress(collmanager.getHurtCount());
            checkDistance(player.getPhysicsLocation().x, player.getPhysicsLocation().z, 
                    npcRigid.getPhysicsLocation().x, npcRigid.getPhysicsLocation().z);
            checkGameOver();
            checkWin();

            //Players Ears 3D Audio
            listener.setLocation(playerNode.getLocalTranslation());
            listener.setRotation(playerNode.getLocalRotation());

            // movement
            Vector3f playerDir = player.getViewDirection().mult(0.04f);
            Vector3f playerLeft = player.getViewDirection().cross(Vector3f.UNIT_Y);
            walkDirection.set(0, 0, 0);

            if(chunk1b && found>0) 
            {
                chunk1.play(); 
            }
            if(chunk2b && found>1) 
            {
                chunk2.play(); 
            }
            if(chunk3b && found>2) 
            {
                chunk3.play(); 
            }
            if(up) { walkDirection.set(playerDir.negate().mult(speed * tpf)); }
            if(down) { walkDirection.set(playerDir.mult(0.6f * speed * tpf)); }
            if(left) { player.setViewDirection(player.getViewDirection().interpolate(playerLeft.negate(), speed*0.1f*tpf).normalize()); };
            if(right) { player.setViewDirection(player.getViewDirection().interpolate(playerLeft, speed*0.1f*tpf).normalize()); }
            if(space) { player.jump(); }

            player.setWalkDirection(walkDirection);
           
            if(replace) {
                resetPlayer();
                replace = false;
            }
            if(mute) {
                if(isPlayingSound) {
                    logger.log(Level.INFO, "Stopping sound");
                    audio_music.pause();
                    isPlayingSound = false;
                    mute = false;
                } else {
                    logger.log(Level.INFO, "Playing sound");
                    audio_music.play();
                    isPlayingSound = true;
                    mute = false;
                }
            }
            if(collmanager.checkHit()) {
                bumped.playInstance();
                collmanager.resetHit();
            } 

            soundRotation(tpf);
            }
        }

    
        private void checkDistance(float px, float pz, float nx, float nz)
        {
        }
        
        private void checkWin()
        {
            if(found>2)
            {
                guiNode.attachChild(hud.makeWin(assetManager, settings.getWidth(), settings.getHeight()));
                player.setWalkDirection(new Vector3f(0f,0f,0f));
                up = false;
                down = false;
                right = false;
                left = false;
                finalsong.play();
                pause = true;
                resetPlayer();
                audio_music.stop();
                footSteps.stop();
            }
                
        }
        
        private void checkGameOver()
        {
            if(hud.getStress()>370)
            {
                System.out.println("Game Over");
                up = false;
                down = false;
                right = false;
                left = false;
                player.setWalkDirection(new Vector3f(0f,0f,0f));
                guiNode.attachChild(hud.makeGameOver(assetManager, settings.getWidth(), settings.getHeight()));
                pause = true;
                gameover.playInstance();
                audio_music.stop();
                footSteps.stop();
            }
        }

        /**
         * AudioNode gefunden
         */
        private void checkFindings() {
            if(collmanager.checkSoundFound(1)) {
                soundfound.play();
                collmanager.markSoundFound(1);
                rootNode.detachChildNamed("Sound1");
                bulletAppState.getPhysicsSpace().remove(sf1.getModel());
                found+=1;
                manageNoteHUD(found);
            }
            else if(collmanager.checkSoundFound(2)) {
                soundfound.play();
                collmanager.markSoundFound(2);
                rootNode.detachChildNamed("Sound2");
                bulletAppState.getPhysicsSpace().remove(sf2.getModel());
                found+=1;
                manageNoteHUD(found);
            }
            else  if(collmanager.checkSoundFound(3)) {
                soundfound.play();
                collmanager.markSoundFound(3);
                rootNode.detachChildNamed("Sound3");
                bulletAppState.getPhysicsSpace().remove(sf3.getModel());
                found+=1;
                manageNoteHUD(found);
        }
    }
    
    /**
     * HUD für Noten
     * @param found Anzhal
     */
    private void manageNoteHUD(int found) {
        if(found==1)
        {
           guiNode.detachChildNamed("SoundFound0");
           guiNode.attachChild(hud.nextLevel(assetManager, settings.getWidth(), settings.getHeight(),1));
        }
        else if(found==2)
        {
           guiNode.detachChildNamed("SoundFound1");
           guiNode.attachChild(hud.nextLevel(assetManager, settings.getWidth(), settings.getHeight(),2)); 
        }
        else if(found==3)
        {
           guiNode.detachChildNamed("SoundFound2");
           guiNode.attachChild(hud.nextLevel(assetManager, settings.getWidth(), settings.getHeight(),3));
        }
    }
    
    /**
     * Rotation des AudioNode Models
     * @param tpf 
     */
    private void soundRotation(final float tpf)
    {
         soundNode1.depthFirstTraversal(new SceneGraphVisitor() {
            public void visit(Spatial spatial) {
                if(spatial instanceof Geometry) {
                    ((Spatial)spatial).rotate(0, tpf, 0);
                }
            }
        });
        soundNode2.depthFirstTraversal(new SceneGraphVisitor() {
            public void visit(Spatial spatial) {
                if(spatial instanceof Geometry) {
                    ((Spatial)spatial).rotate(0, tpf, 0);
                }
            }
        });
        soundNode3.depthFirstTraversal(new SceneGraphVisitor() {
            public void visit(Spatial spatial) {
                if(spatial instanceof Geometry) {
                    ((Spatial)spatial).rotate(0, tpf, 0);
                }
            }
        });
        soundModel1.rotate(0, tpf, 0);
        soundModel2.rotate(0, tpf, 0);
        soundModel3.rotate(0, tpf, 0);
    }
    
    /**
     * NPC bewegen
     */
    private void moveNPC() {
        float y = npcRigid.getPhysicsLocation().y;
        if(z > 100f) 
        {
            npcRigid.setPhysicsLocation(new Vector3f(0f,10f,0f));
        }
        else 
        {
            npcRigid.setLinearVelocity(ki.getDirection(npcRigid.getPhysicsLocation(),"a"));
        }
        
        footSteps.setPositional(true);
        footSteps.setLocalTranslation(npcRigid.getPhysicsLocation());
    }
    
    /**
     * Player Position resetten
     */
    private void resetPlayer() {
        player.setPhysicsLocation(new Vector3f(0,50,0));
        respawned.playInstance();
    }
    
    private void resetNPC(int i) {
        if(i==1)
            npcRigid.setPhysicsLocation(new Vector3f(0,2,0));
        else
            npcRigid2.setPhysicsLocation(new Vector3f(0,2,0));
    }
    
    /**
     * Überprüfen ob der Player aus der Karte fällt
     */
    private void fallCheck() {
        if(player.getPhysicsLocation().y < -20) { resetPlayer(); }
        if(npcRigid.getPhysicsLocation().y < -1) { resetNPC(1); }
    }
    
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    /**
     * HUD anzeigen
     */
    private void showHUD() {
        hud = new HUD();
        ArrayList<Picture> al = hud.genHUDElements(assetManager, settings.getWidth(),settings.getHeight());
        for(Picture pic : al)
        {
            guiNode.attachChild(pic);
        }
    }

    private void checkStress(int npcHits) {
        if(npcHits>3)
        {
            guiNode.detachChildNamed("StressBar");
            stressBing.playInstance();
            guiNode.attachChild(hud.extendStress(assetManager, settings.getWidth(), settings.getHeight()));
            collmanager.resetHurtCount();
        }
    }
    
    private void spawnSound(SoundFactory sf) {
        if(sf.createSound()) {
            String name = sf.getModel().getName();
            System.out.println("Soundmodel spawned: "+name);
            
            if(name.contains("1"))
            {
                soundNode1.attachChild(sf.getModel());
                
            }
            else if(name.contains("2"))
            {
                soundNode2.attachChild(sf.getModel());
            }
            if(name.contains("3"))
            {
                soundNode3.attachChild(sf.getModel());
            }
            
            bulletAppState.getPhysicsSpace().add(sf.getShape());
        }
        else
        {
            System.out.println("Soundmodel not spawned: "+sf.getModel().getName());
        }
    }
}