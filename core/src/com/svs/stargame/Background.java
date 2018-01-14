package com.svs.stargame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.svs.engine.math.Rect;
import com.svs.engine.sprite.Sprite;

public class Background extends Sprite {

    public Background(TextureRegion region) {
        super(region);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight());
        pos.set(worldBounds.pos);
    }
}