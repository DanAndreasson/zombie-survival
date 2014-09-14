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

    private int numFootContacts;
    private Array<Body> bodiesToRemove;
    private boolean playerDead;

    public BBContactListener() {
        super();
        bodiesToRemove = new Array<Body>();
    }


    /**
     *
     *
     * @param contact
     */
    public void beginContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();



        if(fa == null || fb == null) return;


        if(fixtureIs(fa, "foot")) {
            numFootContacts++;
        }
        if(fixtureIs(fb, "foot")) {
            numFootContacts++;
        }

        if(fixtureIs(fa, "crystal")) {
            bodiesToRemove.add(fa.getBody());
        }
        if(fixtureIs(fb, "crystal")) {
            bodiesToRemove.add(fb.getBody());
        }

        if(fixtureIs(fa, "spike")) {
            playerDead = true;
            Gdx.app.log("BBContactListener", "FA: Spike");
        }
        if(fixtureIs(fb, "spike")) {
            playerDead = true;
            Gdx.app.log("BBContactListener", "FB: Spike");

        }

    }

    public boolean fixtureIs(Fixture fix, String name) {
        return fix.getUserData() != null && fix.getUserData().equals(name);
    }

    public void endContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;

        if(fixtureIs(fa, "foot")) {
            numFootContacts--;
        }
        if(fixtureIs(fb, "foot")) {
            numFootContacts--;
        }

    }

    public boolean playerCanJump() { return numFootContacts > 0; }
    public Array<Body> getBodies() { return bodiesToRemove; }
    public boolean isPlayerDead() { return playerDead; }

    public void preSolve(Contact c, Manifold m) {}
    public void postSolve(Contact c, ContactImpulse ci) {}

}
