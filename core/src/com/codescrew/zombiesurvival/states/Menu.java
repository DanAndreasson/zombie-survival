package com.codescrew.zombiesurvival.states;

import static com.codescrew.zombiesurvival.handlers.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.codescrew.zombiesurvival.entities.B2DSprite;
import com.codescrew.zombiesurvival.entities.Player;
import com.codescrew.zombiesurvival.handlers.Animation;
import com.codescrew.zombiesurvival.handlers.B2DVars;
import com.codescrew.zombiesurvival.handlers.BBInput;
import com.codescrew.zombiesurvival.handlers.Background;
import com.codescrew.zombiesurvival.handlers.GameButton;
import com.codescrew.zombiesurvival.handlers.GameStateManager;
import com.codescrew.zombiesurvival.main.Game;


public class Menu extends GameState {

    private static final String TAG = "Menu";

    private Background bg;
    private GameButton highscoreButton;
    private GameButton playButton;
    private GameButton title;

    private World world;
    private Player player;
    private  Box2DDebugRenderer debugRenderer;

    public Menu(GameStateManager gsm) {

        super(gsm);

        debugRenderer = new Box2DDebugRenderer();

        Texture tex = Game.res.getTexture("menubg");
        bg = new Background(new TextureRegion(tex), cam, 0f);
        bg.setVector(0, 0);

        tex = Game.res.getTexture("menu");
        playButton = new GameButton(new TextureRegion(tex, 0, 0, 185, 52),
                Game.V_WIDTH/2, 350, cam, false);

        highscoreButton = new GameButton(new TextureRegion(tex, 0, 54, 433, 51),
                Game.V_WIDTH/2, 250, cam, false);

        tex = Game.res.getTexture("title");
        title = new GameButton(new TextureRegion(tex, 0, 0, 792, 74),
                Game.V_WIDTH/2, 550, cam, false);

        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);



        world = new World(new Vector2(0, 0), true);

        createFloor();
        createPlayer();

    }


    public void handleInput() {

        // mouse/touch input
        if(playButton.isClicked()) {
            // Wait some ms to let the player jump first.
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            gsm.setState(GameStateManager.LEVEL_SELECT);
                        }
                    },
                    500
            );
        }else if(BBInput.isPressed()){
            player.jump();
        }



    }
    float playerStart = 3.4f;
    public void update(float dt) {

        handleInput();

        world.step(Game.STEP, 1, 1);

        bg.update(dt);

        player.update(dt);

        player.getBody().applyForceToCenter(0, -9.2f, true);

        playButton.update(dt);

    }

    public void render() {

        sb.setProjectionMatrix(cam.combined);
        debugRenderer.render(world, cam.combined);
        // draw background
        bg.render(sb);

        // draw button
        playButton.render(sb);

        // draw button
        highscoreButton.render(sb);

        // draw button
        title.render(sb);

        player.render(sb);

    }

    public void dispose() {
    }

    private void createPlayer() {

        float spawnX = 340f;
        float spawnY = 63f;

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

        // create player collision box fixture
        body.createFixture(fdef);
        shape.dispose();

        // create box shape for player foot
        shape = new PolygonShape();
        shape.setAsBox(14 / PPM, 10 / PPM, new Vector2(0, -10 / PPM), 0);

        fdef.shape = shape;

        body.createFixture(fdef);
        shape.dispose();

        player = new Player(body);
        body.setUserData(player);

        MassData md = body.getMassData();
        md.mass = player.getBodyMass();
        body.setMassData(md);
    }

    private void createFloor(){
        float spawnX = 340f;
        float spawnY = 62f;
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        float ts = 32f;
        bdef.position.set(spawnX / PPM, spawnY / PPM);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(14 / PPM, 10 / PPM, new Vector2(0, -10 / PPM), 0);
        FixtureDef fd = new FixtureDef();
        fd.friction = 0;
        fd.shape = shape;
        world.createBody(bdef).createFixture(fd);
        shape.dispose();
    }

}
