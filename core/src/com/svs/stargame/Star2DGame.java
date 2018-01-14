package com.svs.stargame;

import com.badlogic.gdx.Game;

import com.svs.stargame.screen.menu.MenuScreen;

/**
 * Created by SVS on 09.12.2017.
 */

public class Star2DGame extends Game {

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }
}
