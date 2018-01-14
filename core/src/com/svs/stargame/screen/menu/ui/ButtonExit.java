package com.svs.stargame.screen.menu.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.svs.engine.math.Rect;
import com.svs.engine.ui.ActionListener;
import com.svs.engine.ui.ScaledTouchUpButton;

/**
 * Created by SVS on 17.12.2017.
 */

public class ButtonExit extends ScaledTouchUpButton {

    public ButtonExit(TextureAtlas atlas,
                      ActionListener actionListener,
                      float pressScale
    ) {
        super(atlas.findRegion("btExit"), actionListener, pressScale);
    }

    @Override
    public void resize(Rect worldBounds) {
        setBottom(worldBounds.getBottom());
        setRight(worldBounds.getRight());
    }
}
