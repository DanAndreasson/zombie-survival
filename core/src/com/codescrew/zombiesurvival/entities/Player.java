package com.codescrew.zombiesurvival.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.codescrew.zombiesurvival.main.Game;

public class Player extends B2DSprite {

    private static final String TAG = "Player";
    private int numCrystals;
    private int totalCrystals;

    private float bodyMass;
    private float lastDirection;

    // CONSTANTS
    public static final float RUNNING_SPEED = 2f;
    public static final float GRAVITY = -9.82f;
    public static final float JUMP_SPEED = 5.5f;
    private static final float WALLJUMP_X_SPEED = 2f;
    private static final float WALLJUMP_Y_SPEED = 5f;

    public Player(Body body) {

        super(body);

        bodyMass = 1f;

        Texture tex = Game.res.getTexture("zombie");
        TextureRegion[] sprites = new TextureRegion[4];
        for(int i = 0; i < sprites.length; i++) {
            sprites[i] = new TextureRegion(tex, i * 55, 0, 55, 61);
        }

        animation.setFrames(sprites, 1 / 12f);

        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();

    }

    public void collectBrain() { ++numCrystals; }
    public int getNumBrains() { return numCrystals; }

    public void setTotalBrains(int i) { totalCrystals = i; }
    public int getTotalBrains() { return totalCrystals; }



    public float getBodyMass() {
        return bodyMass;
    }

    public boolean inAir() {
        return body.getLinearVelocity().y != 0;
    }

    public void jump() {
        Gdx.app.log(TAG, "jump()");

        setYVelocity(JUMP_SPEED);
        applyGravity();

        Game.res.getSound("jump").play();
    }

    public void wallJump() {
        Gdx.app.log(TAG, "WallJump()");

        float velocity = WALLJUMP_X_SPEED;

        if (lastDirection > 0){
            velocity *= -1;
        }

        setYVelocity(WALLJUMP_Y_SPEED);
        setXVelocity(velocity);

        Game.res.getSound("jump").play();
    }

    public float getLastDirection() { return lastDirection; }

    public void setLastDirection(float d) {
        if (d != 0) lastDirection = d;
    }

    public float xVelocity(){
        return body.getLinearVelocity().x;
    }

    public float yVelocity(){
        return body.getLinearVelocity().y;
    }

    public void setXVelocity(float x) {
        body.setLinearVelocity(x, yVelocity());
    }

    public void setYVelocity(float v){
        body.setLinearVelocity(xVelocity(), v);
    }

    public void applyGravity(){
        body.applyForceToCenter(0, GRAVITY, true);
    }
}
