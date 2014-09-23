package com.codescrew.zombiesurvival.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.codescrew.zombiesurvival.main.Game;

public class Player extends B2DSprite {

    public static final float horizontalSpeed = 1.7f;
    private static final String TAG = "Player";
    private int numCrystals;
    private int totalCrystals;

    private int verticalForce;
    private float bodyMass;
    private float levelForce = 1.2f;
    private float lastDirection;

    public Player(Body body) {

        super(body);

        verticalForce = 80;  // If higher then jump higher
        bodyMass = 1;

        Texture tex = Game.res.getTexture("zombie");
        TextureRegion[] sprites = new TextureRegion[3];
        for(int i = 0; i < sprites.length; i++) {
            sprites[i] = new TextureRegion(tex, i * 21, 0, 21, 30);
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

    public boolean inAir() {
        return body.getLinearVelocity().y != 0;
    }

    public void jump() {
        //getBody().setLinearVelocity(getBody().getLinearVelocity().x, 1.7f);
        //getBody().applyForceToCenter(levelForce, getVerticalForce(), true);
        getBody().applyLinearImpulse(new Vector2(0, 3), body.getPosition(), true);
        Game.res.getSound("jump").play();
    }

    public void wallJump() {
        float xVelo = 3f;
        if (lastDirection > 0)
            xVelo *= -1;
        if (xVelo > 0 )
            xVelo *= 2;
        Gdx.app.log(TAG, (xVelo+""));
        //body.setLinearVelocity(xVelo, 2f);
        //getBody().applyForceToCenter(10f, getVerticalForce(), true);
        getBody().setapplyLinearImpulse(new Vector2(xVelo, 3f), body.getPosition(), true);
        Game.res.getSound("jump").play();
    }

    public float getLastDirection() { return lastDirection; }

    public void setLastDirection(float d) {
        if (d != 0)
            lastDirection = d;


    }

    public float getLevelForce() {
        return levelForce;
    }
}
