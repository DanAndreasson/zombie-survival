package com.codescrew.zombiesurvival.states;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.codescrew.zombiesurvival.handlers.GameButton;
import com.codescrew.zombiesurvival.handlers.GameStateManager;
import com.codescrew.zombiesurvival.main.Game;

public class LevelSelect extends GameState {

    private TextureRegion reg;

    private GameButton[][] buttons;

    public LevelSelect(GameStateManager gsm) {

        super(gsm);

        reg = new TextureRegion(Game.res.getTexture("menubg"), 0, 0, Game.V_WIDTH, Game.V_HEIGHT);

        int contentMarginLeft =  270;
        int contentMarginTop =  150;
        int btnWidth = 100;
        int btnHeight = 100;
        int btnMargin = 50;

        TextureRegion buttonReg = new TextureRegion(Game.res.getTexture("tombstone"), 0, 0, 68, 68);
        buttons = new GameButton[5][5];
        for(int row = 0; row < buttons.length; row++) {
            for(int col = 0; col < buttons[0].length; col++) {

                buttons[row][col] = new GameButton(buttonReg,
                        contentMarginLeft + col * btnWidth,
                        Game.V_HEIGHT - contentMarginTop - row * btnHeight,
                        cam, true);

                buttons[row][col].setText(row * buttons[0].length + col + 1 + "");
            }
        }

        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

    }

    public void handleInput() {

    }

    public void update(float dt) {
        handleInput();

        for(int row = 0; row < buttons.length; row++) {
            for(int col = 0; col < buttons[0].length; col++) {
                buttons[row][col].update(dt);
                if(buttons[row][col].isClicked()) {
                    Play.level = row * buttons[0].length + col + 1;
                    Game.res.getSound("levelselect").play();
                    gsm.setState(GameStateManager.PLAY);
                }
            }
        }
    }

    public void render() {

        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        sb.draw(reg, 0, 0);
        sb.end();

        for(int row = 0; row < buttons.length; row++) {
            for(int col = 0; col < buttons[0].length; col++) {
                buttons[row][col].render(sb);
            }
        }

    }

    public void dispose() {
    }

}