package com.codescrew.zombiesurvival.handlers;


import com.codescrew.zombiesurvival.main.Game;
import com.codescrew.zombiesurvival.states.GameState;
import com.codescrew.zombiesurvival.states.LevelSelect;
import com.codescrew.zombiesurvival.states.Play;
import com.codescrew.zombiesurvival.states.Menu;

import java.util.Stack;


public class GameStateManager {
    private Game game;

    private Stack<GameState> gameStates;

    public static final int MENU = 1;
    public static final int PLAY = 2;
    public static final int LEVEL_SELECT = 3;

    public GameStateManager(Game game) {
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(MENU);
    }

    public void update(float dt) {
        gameStates.peek().update(dt);
    }

    public void render() {
        gameStates.peek().render();
    }

    public Game game() { return game; }

    private GameState getState(int state) {
        if(state == MENU) return new Menu(this);
        if(state == PLAY) return new Play(this);
        if(state == LEVEL_SELECT) return new LevelSelect(this);
        return null;
    }

    public void setState(int state) {
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        gameStates.push(getState(state));
    }

    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }

}
