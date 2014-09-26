package com.codescrew.zombiesurvival.states;

import static com.codescrew.zombiesurvival.handlers.B2DVars.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.codescrew.zombiesurvival.entities.B2DSprite;
import com.codescrew.zombiesurvival.handlers.Animation;
import com.codescrew.zombiesurvival.handlers.B2DVars;
import com.codescrew.zombiesurvival.handlers.Background;
import com.codescrew.zombiesurvival.handlers.GameButton;
import com.codescrew.zombiesurvival.handlers.GameStateManager;
import com.codescrew.zombiesurvival.main.Game;


public class Menu extends GameState {

    private boolean debug = false;

    private Background bg;
    private Animation animation;
    private GameButton highscoreButton;
    private GameButton playButton;
    private GameButton title;

    private World world;
    private Box2DDebugRenderer b2dRenderer;

    private Array<B2DSprite> blocks;

    public Menu(GameStateManager gsm) {

        super(gsm);

        Texture tex = Game.res.getTexture("menubg");
        bg = new Background(new TextureRegion(tex), cam, 1f);
        bg.setVector(0, 0);

        tex = Game.res.getTexture("zombie");
        TextureRegion[] sprites = new TextureRegion[4];
        for(int i = 0; i < sprites.length; i++) {
            sprites[i] = new TextureRegion(tex, i * 55, 0, 55, 61);
        }
        animation = new Animation(sprites, 1 / 12f);

        tex = Game.res.getTexture("menu");
        playButton = new GameButton(new TextureRegion(tex, 0, 0, 185, 52),
                Game.V_WIDTH/2, 350, cam, false);


        tex = Game.res.getTexture("menu");
        highscoreButton = new GameButton(new TextureRegion(tex, 0, 54, 433, 51),
                Game.V_WIDTH/2, 250, cam, false);

        tex = Game.res.getTexture("title");
        title = new GameButton(new TextureRegion(tex, 0, 0, 792, 74),
                Game.V_WIDTH/2, 550, cam, false);

        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

        world = new World(new Vector2(0, 0), true);

    }


    public void handleInput() {

        // mouse/touch input
        if(playButton.isClicked()) {
            //Game.res.getSound("brain").play();
            gsm.setState(GameStateManager.LEVEL_SELECT);
        }

    }

    public void update(float dt) {

        handleInput();

        world.step(dt / 5, 8, 3);

        bg.update(dt);
        animation.update(dt);

        playButton.update(dt);

    }

    public void render() {

        sb.setProjectionMatrix(cam.combined);

        // draw background
        bg.render(sb);

        // draw button
        playButton.render(sb);

        // draw button
        highscoreButton.render(sb);

        // draw button
        title.render(sb);

        // draw bunny
        sb.begin();
        sb.draw(animation.getFrame(), 340, 62);
        sb.end();

    }

    public void dispose() {
        // everything is in the resource manager com.neet.blockbunny.handlers.Content
    }

}
