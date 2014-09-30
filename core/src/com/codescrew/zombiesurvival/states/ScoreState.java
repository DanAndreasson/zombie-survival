package com.codescrew.zombiesurvival.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.codescrew.zombiesurvival.entities.Player;
import com.codescrew.zombiesurvival.handlers.Background;
import com.codescrew.zombiesurvival.handlers.GameButton;
import com.codescrew.zombiesurvival.handlers.GameStateManager;
import com.codescrew.zombiesurvival.main.Game;

public class ScoreState extends GameState{

    private static final String TAG = "ScoreState";
    private final Background bg;
    private final World world;
    private final GameButton gameover;
    private final GameButton nextlevel;
    private final GameButton playagain;
    private final TextureRegion brain;
    private TextureRegion[] font;

    private final String scoreString;


    public ScoreState(GameStateManager gsm) {

        super(gsm);

        setBigFont();

        Texture tex = Game.res.getTexture("menubg");
        bg = new Background(new TextureRegion(tex), cam, 0f);
        bg.setVector(0, 0);

       // Texture brainTex = Game.res.getTexture("score-brain");
        //brain = new TextureRegion(brainTex, 0, 0, 100, 69);
        tex = Game.res.getTexture("brain");
        brain = new TextureRegion(tex, 0, 0, 32, 22);

        tex = Game.res.getTexture("game-over");
        gameover = new GameButton(new TextureRegion(tex, 0, 0, 792, 103),
                Game.V_WIDTH/2, 550, cam, false);

        tex = Game.res.getTexture("scoremenu");

        playagain = new GameButton(new TextureRegion(tex, 0, 0, 446, 52),
                Game.V_WIDTH/2, 350, cam, false);

        nextlevel = new GameButton(new TextureRegion(tex, 0, 54, 436, 51),
                Game.V_WIDTH/2, 250, cam, false);


        world = new World(new Vector2(0, 0), true);

        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

        Player player = Play.getPlayer();
        int score = player.getNumBrains();
        int maxScore = player.getTotalBrains();

        scoreString = score + "/" + maxScore;

    }

    @Override
    public void handleInput() {

        if (playagain.isClicked()){
            Gdx.app.log(TAG, "Play again: " + Play.level);
            gsm.setState(GameStateManager.PLAY);
        }
        else if (nextlevel.isClicked()){
            ++Play.level;
            gsm.setState(GameStateManager.PLAY);
            Gdx.app.log(TAG, "Play next level: " + Play.level);
        }

    }

    private void setBigFont(){
        Texture tex = Game.res.getTexture("bigfont");
        font = new TextureRegion[11];
        for(int i = 0; i < font.length; i++) {
            font[i] = new TextureRegion(tex, i * 16, 0, 16, 20);
        }
    }

    @Override
    public void update(float dt) {

        handleInput();

        world.step(dt / 5, 8, 3);

        playagain.update(dt);
        nextlevel.update(dt);

        bg.update(dt);

    }

    private void drawString(SpriteBatch sb, String s, float x, float y) {
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(c == '/') c = 10;
            else if(c >= '0' && c <= '9') c -= '0';
            else continue;
            sb.draw(font[c], x + i * 16, y);
        }
    }

    @Override
    public void render() {

        sb.setProjectionMatrix(cam.combined);

        bg.render(sb);
        gameover.render(sb);
        playagain.render(sb);
        nextlevel.render(sb);

        sb.begin();

        sb.draw(brain, Game.V_WIDTH / 2 - 100, Game.V_HEIGHT / 2 + 70);
        drawString(sb, scoreString, Game.V_WIDTH / 2 - 60, Game.V_HEIGHT / 2 + 70);

        sb.end();

    }

    @Override
    public void dispose() {

    }
}
