package com.codescrew.zombiesurvival.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;


public class BBContactListener implements ContactListener {

    private static final String TAG = "BBContactListener";
    private int numFootContacts;
    private Array<Body> bodiesToRemove;
    private boolean playerDead;

    public BBContactListener() {
        super();
        bodiesToRemove = new Array<Body>();
    }


    /**
     *
     * @param contact
     */
    public void beginContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();



        if(fa == null || fb == null) return;

        if (oneFixtureIs(fa, fb, "foot") && oneFixtureIs(fa, fb, "solid" )){
            ++numFootContacts;
        }

        if(oneFixtureIs(fa, fb, "brain" )) {
            Gdx.app.log(TAG, "Brain collected!");
            Fixture brain = fa;
            if (fixtureIs(fb, "brain")) brain = fb;
            bodiesToRemove.add(brain.getBody());
        }

        if(fixtureIs(fa, "spike")) {
            playerDead = true;
        }
        if(fixtureIs(fb, "spike")) {
            playerDead = true;
        }

    }

    private boolean oneFixtureIs(Fixture fa, Fixture fb, String fixture) {
        return fixtureIs(fa, fixture) || fixtureIs(fb, fixture);
    }

    public boolean fixtureIs(Fixture fix, String name) {
        return fix.getUserData() != null && fix.getUserData().equals(name);
    }

    public void endContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;


        if (oneFixtureIs(fa, fb, "foot") && oneFixtureIs(fa, fb, "solid" )){
            --numFootContacts;
        }
    }

    public boolean playerCanJump() { return numFootContacts > 0; }
    public Array<Body> getCollectedBrains() { return bodiesToRemove; }
    public boolean isPlayerDead() { return playerDead; }


    public void preSolve(Contact c, Manifold m) {}
    public void postSolve(Contact c, ContactImpulse ci) {}

}
