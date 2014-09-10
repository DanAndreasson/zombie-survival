package com.codescrew.zombiesurvival.entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.codescrew.zombiesurvival.main.Game;

public class Player extends B2DSprite {

    public static final float horizontalSpeed = 1.7f;
    private int numCrystals;
    private int totalCrystals;

    private int verticalForce;
    private float bodyMass;

    public Player(Body body) {

        super(body);

        verticalForce = 200;  // If higher then jump higher
        bodyMass = 1;

        Texture tex = Game.res.getTexture("zombie");
        TextureRegion[] sprites = new TextureRegion[3];
        for(int i = 0; i < sprites.length; i++) {
            sprites[i] = new TextureRegion(tex, i * 32, 0, 32, 32);
        }

        animation.setFrames(sprites, 1 / 12f);

        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();

    }

    public void collectBrain() { ++numCrystals; }
    public int getNumBrains() { return numCrystals; }

    public void setTotalBrains(int i) { totalCrystals = i; }
    public int getTotalBrains() { return totalCrystals; }

    public int getVerticalForce() { return verticalForce; }


    public float getBodyMass() {
        return bodyMass;
    }
}
