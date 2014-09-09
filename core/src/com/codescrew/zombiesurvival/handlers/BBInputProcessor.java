package com.codescrew.zombiesurvival.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by raimat on 2014-09-09.
 */
public class BBInputProcessor extends InputAdapter {

    public boolean mouseMoved(int x, int y) {
        BBInput.x = x;
        BBInput.y = y;
        return true;
    }

    public boolean touchDragged(int x, int y, int pointer) {
        BBInput.x = x;
        BBInput.y = y;
        BBInput.down = true;
        return true;
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        BBInput.x = x;
        BBInput.y = y;
        BBInput.down = true;
        return true;
    }

    public boolean touchUp(int x, int y, int pointer, int button) {
        BBInput.x = x;
        BBInput.y = y;
        BBInput.down = false;
        return true;
    }

    public boolean keyDown(int k) {
        if(k == Input.Keys.Z) BBInput.setKey(BBInput.BUTTON1, true);
        if(k == Input.Keys.X) BBInput.setKey(BBInput.BUTTON2, true);
        return true;
    }

    public boolean keyUp(int k) {
        if(k == Input.Keys.Z) BBInput.setKey(BBInput.BUTTON1, false);
        if(k == Input.Keys.X) BBInput.setKey(BBInput.BUTTON2, false);
        return true;
    }

}