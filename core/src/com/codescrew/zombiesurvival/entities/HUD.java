package com.codescrew.zombiesurvival.entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.codescrew.zombiesurvival.handlers.B2DVars;
import com.codescrew.zombiesurvival.main.Game;

public class HUD {

    private Player player;

    private TextureRegion container;
    private TextureRegion[] blocks;
    private TextureRegion brain;
    private TextureRegion[] font;

    public HUD(Player player) {

        this.player = player;

        Texture tex = Game.res.getTexture("hud");

        Texture brainTex = Game.res.getTexture("score-brain");
        brain = new TextureRegion(brainTex, 0, 0, 100, 69);

        setBigFont();

    }

    private void setBigFont(){
        Texture tex = Game.res.getTexture("bigfont");
        font = new TextureRegion[11];
        for(int i = 0; i < font.length; i++) {
            font[i] = new TextureRegion(tex, i * 16, 0, 16, 20);
        }
    }



    public void render(SpriteBatch sb) {

        sb.begin();

        // draw crystal
        sb.draw(brain, Game.V_WIDTH-100, Game.V_HEIGHT-100);

        // draw crystal amount
        drawString(sb, player.getNumBrains() + " / " + player.getTotalBrains(),
                Game.V_WIDTH-70, Game.V_HEIGHT-100);

        sb.end();

    }

    private void drawString(SpriteBatch sb, String s, float x, float y) {
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(c == '/') c = 10;
            else if(c >= '0' && c <= '9') c -= '0';
            else continue;
            sb.draw(font[c], x + i * 9, y);
        }
    }

}