package com.codescrew.zombiesurvival.states;


import static com.codescrew.zombiesurvival.handlers.B2DVars.PPM;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.codescrew.zombiesurvival.entities.Brain;
import com.codescrew.zombiesurvival.entities.HUD;
import com.codescrew.zombiesurvival.entities.Player;
import com.codescrew.zombiesurvival.handlers.B2DVars;
import com.codescrew.zombiesurvival.handlers.BBContactListener;
import com.codescrew.zombiesurvival.handlers.BBInput;
import com.codescrew.zombiesurvival.handlers.Background;
import com.codescrew.zombiesurvival.handlers.BoundedCamera;
import com.codescrew.zombiesurvival.handlers.GameStateManager;
import com.codescrew.zombiesurvival.main.Game;


public class Play extends GameState {

    private static final String TAG = "Play";
    private boolean debug = false;

    private World world;
    private Box2DDebugRenderer b2dRenderer;
    private BBContactListener cl;
    private BoundedCamera b2dCam;

    private Player player;

    private TiledMap tileMap;
    private int tileMapWidth;
    private int tileMapHeight;
    private int tileSize;
    private OrthogonalTiledMapRenderer tmRenderer;

//    private Array<Crystal> crystals;
//    private Array<Spike> spikes;

    private Background[] backgrounds;
    private HUD hud;

    public static int level;
    private Array<Brain> brains;

    public Play(GameStateManager gsm) {

        super(gsm);

        // set up the box2d world and contact listener
        world = new World(new Vector2(0, -7f), true);
        cl = new BBContactListener();
        world.setContactListener(cl);
        b2dRenderer = new Box2DDebugRenderer();


        // create walls
        createWalls();
        cam.setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);

        // create player
        createPlayer();

        // create crystals
        createBrains();
        player.setTotalBrains(brains.size);

        // create spikes
        //createSpikes();

        // create backgrounds
        Texture bgs = Game.res.getTexture("bgs");
        TextureRegion sky = new TextureRegion(bgs, 0, 0, 960, 720);
        TextureRegion clouds = new TextureRegion(bgs, 0, 720, 960, 720);
        TextureRegion mountains = new TextureRegion(bgs, 0, 1440, 960, 720);
        // TextureRegion sky = new TextureRegion(bgs, 0, 0, Game.V_WIDTH, Game.V_HEIGHT);
        //TextureRegion clouds = new TextureRegion(bgs, 0, Game.V_HEIGHT, Game.V_WIDTH, Game.V_HEIGHT);
        //TextureRegion mountains = new TextureRegion(bgs, 0, Game.V_WIDTH*2, Game.V_WIDTH, Game.V_HEIGHT);
        backgrounds = new Background[3];
        backgrounds[0] = new Background(sky, cam, 0f);
        backgrounds[1] = new Background(clouds, cam, 0.1f);
        backgrounds[2] = new Background(mountains, cam, 0.2f);

        // create hud
        hud = new HUD(player);

        // set up box2d cam
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, (Game.V_WIDTH / PPM) * 2, (Game.V_HEIGHT / PPM) * 2);
        b2dCam.setBounds(0, (tileMapWidth * tileSize) / PPM, 0, (tileMapHeight * tileSize) / PPM);

    }

    /**
     * Creates the player.
     * Sets up the box2d body and sprites.
     */
    private void createPlayer() {

        MapLayer ml = tileMap.getLayers().get("spawn");

        MapObject mo = ml.getObjects().get(0);
        float spawnX = (Float) mo.getProperties().get("x");
        float spawnY = (Float) mo.getProperties().get("y");

        Gdx.app.log("Play", spawnX + "");
        Gdx.app.log("Play", spawnY + "");

        // create bodydef
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(spawnX / PPM, spawnY / PPM);

        bdef.fixedRotation = true;

        // create body from bodydef
        Body body = world.createBody(bdef);

        // create box shape for player collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(14 / PPM, 20 / PPM);

        // create fixturedef for player collision box
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1;
        fdef.friction = 0;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_WALKABLE_BLOCK | B2DVars.BIT_BRAIN | B2DVars.BIT_SPIKE;

        // create player collision box fixture
        body.createFixture(fdef);
        shape.dispose();

        // create box shape for player foot
        shape = new PolygonShape();
        shape.setAsBox(14 / PPM, 10 / PPM, new Vector2(0, -10 / PPM), 0);

        // create fixturedef for player foot
        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_WALKABLE_BLOCK;

        // create player foot fixture
        body.createFixture(fdef).setUserData("foot");
        shape.dispose();

        // create new player
        player = new Player(body);
        body.setUserData(player);

        // final tweaks, manually set the player body mass to 1 kg
        MassData md = body.getMassData();
        md.mass = player.getBodyMass();
        body.setMassData(md);

    }

    /**
     * Sets up the tile map collidable tiles.
     * Reads in tile map layers and sets up box2d bodies.
     */
    private void createWalls() {

        // load tile map and map renderer
        try {
            tileMap = new TmxMapLoader().load("maps/level" + level + ".tmx");
        } catch (Exception e) {
            System.out.println("Cannot find file: maps/level" + level + ".tmx");
            e.printStackTrace();
            Gdx.app.exit();
        }
        tileMapWidth = (Integer) tileMap.getProperties().get("width");
        tileMapHeight = (Integer) tileMap.getProperties().get("height");
        tileSize = (Integer) tileMap.getProperties().get("tilewidth");
        tmRenderer = new OrthogonalTiledMapRenderer(tileMap);

        // read each of the "red" "green" and "blue" layers
        TiledMapTileLayer layer;
        layer = (TiledMapTileLayer) tileMap.getLayers().get("blocks");
        createBlocks(layer, B2DVars.BIT_WALKABLE_BLOCK);

    }

    /**
     * Creates box2d bodies for all non-null tiles
     * in the specified layer and assigns the specified
     * category bits.
     *
     * @param layer the layer being read
     * @param bits  category bits assigned to fixtures
     */
    private void createBlocks(TiledMapTileLayer layer, short bits) {

        if (layer == null) return;

        // tile size
        float ts = layer.getTileWidth();

        // go through all cells in layer
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {

                // get cell
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                // check that there is a cell
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                // create body from cell
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((col + 0.5f) * ts / PPM, (row + 0.5f) * ts / PPM);
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[4];
                v[0] = new Vector2(-ts / 2 / PPM, -ts / 2 / PPM);
                v[1] = new Vector2(-ts / 2 / PPM, ts / 2 / PPM);
                v[2] = new Vector2(ts / 2 / PPM, ts / 2 / PPM);
                v[3] = new Vector2(ts / 2 / PPM, -ts / 2 / PPM);
                cs.createChain(v);
                FixtureDef fd = new FixtureDef();
                fd.friction = 0;
                fd.shape = cs;
                fd.filter.categoryBits = bits;
                fd.filter.maskBits = B2DVars.BIT_PLAYER;
                world.createBody(bdef).createFixture(fd).setUserData("solid");
                cs.dispose();

            }
        }

    }

    /**
     * Set up box2d bodies for crystals in tile map "crystals" layer
     */
    private void createBrains() {

        // create list of crystals
        brains = new Array<Brain>();

        // get all crystals in "crystals" layer,
        // create bodies for each, and add them
        // to the crystals list
        MapLayer ml = tileMap.getLayers().get("brains");
        if(ml == null) return;

        for(MapObject mo : ml.getObjects()) {
            BodyDef cdef = new BodyDef();
            cdef.type = BodyDef.BodyType.StaticBody;
            float x = (Float) mo.getProperties().get("x") / PPM;
            float y = (Float) mo.getProperties().get("y") / PPM;
            cdef.position.set(x, y);
            Body body = world.createBody(cdef);
            FixtureDef cfdef = new FixtureDef();
            CircleShape cshape = new CircleShape();
            cshape.setRadius(8 / PPM);
            cfdef.shape = cshape;
            cfdef.isSensor = true;
            cfdef.filter.categoryBits = B2DVars.BIT_BRAIN;
            cfdef.filter.maskBits = B2DVars.BIT_PLAYER;
            body.createFixture(cfdef).setUserData("brain");
            Brain b = new Brain(body);
            body.setUserData(b);
            brains.add(b);
            cshape.dispose();
        }
    }

    /**
     * Set up box2d bodies for spikes in "spikes" layer
     */
   /* private void createSpikes() {

        spikes = new Array<Spike>();

        MapLayer ml = tileMap.getLayers().get("spikes");
        if(ml == null) return;

        for(MapObject mo : ml.getObjects()) {
            BodyDef cdef = new BodyDef();
            cdef.type = BodyDef.BodyType.StaticBody;
            float x = (float) mo.getProperties().get("x") / PPM;
            float y = (float) mo.getProperties().get("y") / PPM;
            cdef.position.set(x, y);
            Body body = world.createBody(cdef);
            FixtureDef cfdef = new FixtureDef();
            CircleShape cshape = new CircleShape();
            cshape.setRadius(5 / PPM);
            cfdef.shape = cshape;
            cfdef.isSensor = true;
            cfdef.filter.categoryBits = B2DVars.BIT_SPIKE;
            cfdef.filter.maskBits = B2DVars.BIT_PLAYER;
            body.createFixture(cfdef).setUserData("spike");
            Spike s = new Spike(body);
            body.setUserData(s);
            spikes.add(s);
            cshape.dispose();
        }

    }*/



    private void playerJump() {
        if (cl.playerCanJump() && player.inAir()) {
            player.wallJump();
        } else if (cl.playerCanJump()) {
            player.jump();
        }
    }


    public void handleInput() {
        // Touches screen
        if (BBInput.isPressed()) {
            playerJump();
        }

    }

    public void update(float dt) {

        // check input
        handleInput();

        // update box2d world
        world.step(Game.STEP, 1, 1);

        // check for collected crystals
        Array<Body> bodies = cl.getCollectedBrains();
        for (int i = 0; i < bodies.size; i++) {
            Body b = bodies.get(i);
            brains.removeValue((Brain) b.getUserData(), true);
            world.destroyBody(bodies.get(i));
            player.collectBrain();
//            Game.res.getSound("brain").play();
        }
        bodies.clear();

        // update player
        player.update(dt);

        player.setLastDirection(player.xVelocity());


        if(!player.inAir() && player.xVelocity() < 0){
            player.getBody().applyForceToCenter(9.82f, Player.GRAVITY, true);
        }
        else if (!player.inAir() ||
                (player.xVelocity() == 0 && !cl.playerCanJump()))
        {
            player.setXVelocity(Player.RUNNING_SPEED);
        }

        player.applyGravity();

        // check player win
        if (player.getBody().getPosition().x * PPM > tileMapWidth * tileSize) {
            Game.res.getSound("levelselect").play();
            gsm.setState(GameStateManager.LEVEL_SELECT);
        }

        // check player failed
        if (player.getBody().getPosition().y < 0) {
            Game.res.getSound("hit").play();
            gsm.setState(GameStateManager.MENU);
        }


        if (cl.isPlayerDead()) {
            Game.res.getSound("hit").play();
            gsm.setState(GameStateManager.MENU);
        }

    }

    public void render() {
        Gdx.gl.glClearColor(116/255f, 200/255f, 255/255f, 1);

        // camera follow player
        cam.setPosition(player.getPosition().x * PPM + Game.V_WIDTH / 4, player.getPosition().y * PPM + Game.V_HEIGHT / 4);
        cam.update();




        // draw bgs
        sb.setProjectionMatrix(hudCam.combined);
        for (int i = 0; i < backgrounds.length; i++) {
            backgrounds[i].render(sb);
        }

        // draw tilemap
        tmRenderer.setView(cam);
        tmRenderer.render();

        // draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        // draw brains
        for(Brain brain : brains) {
            brain.render(sb);
        }

        // draw spikes
        /*for(int i = 0; i < spikes.size; i++) {
            spikes.get(i).render(sb);
        }*/

        // draw hud
        sb.setProjectionMatrix(hudCam.combined);
        hud.render(sb);

        // debug draw box2d
        if (debug) {
            b2dCam.setPosition(player.getPosition().x + Game.V_WIDTH / 4 / PPM, Game.V_HEIGHT / 2 / PPM);
            b2dCam.update();
            b2dRenderer.render(world, b2dCam.combined);
        }

    }

    public void dispose() {

    }

}