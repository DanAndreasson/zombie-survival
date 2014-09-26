package com.codescrew.zombiesurvival.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.codescrew.zombiesurvival.handlers.Animation;
import com.codescrew.zombiesurvival.main.Game;

public class Brain extends B2DSprite{

    public Brain(Body body) {
        super(body);

        Texture tex = Game.res.getTexture("brain");
        TextureRegion[] sprites = new TextureRegion[1];
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new TextureRegion(tex, i * 32, 0, 32, 22);
        }

        animation.setFrames(sprites, 1 / 12f);

        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
}
