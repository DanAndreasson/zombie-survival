package com.codescrew.zombiesurvival.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.codescrew.zombiesurvival.handlers.BBInput;
import com.codescrew.zombiesurvival.handlers.BBInputProcessor;
import com.codescrew.zombiesurvival.handlers.BoundedCamera;
import com.codescrew.zombiesurvival.handlers.Content;
import com.codescrew.zombiesurvival.handlers.GameStateManager;

public class Game extends ApplicationAdapter {

    public static final String TITLE = "Zombie Survival?!";
    public static final int SCALE = 3;
    public static final int V_WIDTH = 320*SCALE;
    public static final int V_HEIGHT = 240*SCALE;

    public static final float STEP = 1 / 60f;

    private SpriteBatch sb;
    private BoundedCamera cam;
    private OrthographicCamera hudCam;

    private GameStateManager gsm;

    public static Content res;

    public void create() {

        Gdx.input.setInputProcessor(new BBInputProcessor());

        res = new Content();
        res.loadTexture("images/menubg.png");
        res.loadTexture("images/menu.png");
        res.loadTexture("images/title.png");
        res.loadTexture("images/bgs.png");
        res.loadTexture("images/hud.png");
        res.loadTexture("images/zombie.png");
        res.loadTexture("images/brain.png");
        res.loadTexture("images/score-brain.png");
        res.loadTexture("images/spikes.png");
        res.loadTexture("images/tombstone.png");
        res.loadTexture("images/bigfont.png");
        res.loadTexture("images/zombie-air.png");
        res.loadTexture("maps/spike.png");

        res.loadSound("sfx/jump.wav");
        res.loadSound("sfx/crystal.wav");
        res.loadSound("sfx/levelselect.wav");
        res.loadSound("sfx/hit.wav");
        res.loadSound("sfx/changeblock.wav");

        res.loadMusic("music/bbsong.ogg");
        res.getMusic("bbsong").setLooping(true);
        res.getMusic("bbsong").setVolume(0.5f);
        res.getMusic("bbsong").play();

        cam = new BoundedCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        sb = new SpriteBatch();

        gsm = new GameStateManager(this);

    }

    public void render() {
        Gdx.gl.glClearColor(116f/255f, 200f/255f, 255f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render();
        BBInput.update();

    }

    public void dispose() {
        res.removeAll();
    }

    public void resize(int w, int h) {}

    public void pause() {}

    public void resume() {}

    public SpriteBatch getSpriteBatch() { return sb; }
    public BoundedCamera getCamera() { return cam; }
    public OrthographicCamera getHUDCamera() { return hudCam; }

}

