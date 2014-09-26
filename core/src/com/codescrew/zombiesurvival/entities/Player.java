package com.codescrew.zombiesurvival.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.codescrew.zombiesurvival.main.Game;

public class Player extends B2DSprite {

    private static final String TAG = "Player";
    private int numCrystals;
    private int totalCrystals;

    private TextureRegion[] zombieWalkSprites;
    private TextureRegion[] zombieAirSprites;

    private boolean flip;
    private boolean facingRight;
    private float lastDirection;

    private float bodyMass;

    // CONSTANTS
    public static final float RUNNING_SPEED = 2f;
    public static final float GRAVITY = -9.82f;
    public static final float JUMP_SPEED = 5.5f;
    private static final float WALLJUMP_X_SPEED = 2f;
    private static final float WALLJUMP_Y_SPEED = 5f;

    public Player(Body body) {

        super(body);

        bodyMass = 1f;

        lastDirection = 1;
        flip = false;
        facingRight = true;


        initZombieWalkSprites();
        initZombieAirSprites();

        setAnimation(zombieWalkSprites, 1 / 12f);

        width = zombieWalkSprites[0].getRegionWidth();
        height = zombieWalkSprites[0].getRegionHeight();


    }

    private void initZombieWalkSprites(){
        Texture tex = Game.res.getTexture("zombie");
        zombieWalkSprites = new TextureRegion[4];
        for(int i = 0; i < zombieWalkSprites.length; i++) {
            zombieWalkSprites[i] = new TextureRegion(tex, i * 55, 0, 55, 61);
        }
    }

    private void initZombieAirSprites(){
        Texture tex = Game.res.getTexture("zombie-air");
        zombieAirSprites = new TextureRegion[3];
        for(int i = 0; i < zombieAirSprites.length; i++) {
            zombieAirSprites[i] = new TextureRegion(tex, i * 55, 0, 55, 61);
        }
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

    public void changeSprites(){
        if (yVelocity() == 0)
            setAnimation(zombieWalkSprites, 1 / 12f);
        else
            setAnimation(zombieAirSprites, 1 / 12f);

    }

    public void update(float dt){
        float xVelo = body.getLinearVelocity().x;
        float yVelo = body.getLinearVelocity().y;
        flip = yVelo!= 0 && ((facingRight && xVelo < 0) || (!facingRight && xVelo > 0));
        if (flip) {
            facingRight = !facingRight;
            animation.flipFrames();
        }
        animation.update(dt);
    }
}
