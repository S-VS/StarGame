package com.svs.stargame.star;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import com.svs.engine.math.Rect;
import com.svs.engine.math.Rnd;
import com.svs.engine.sprite.Sprite;

/**
 * Created by SVS on 17.12.2017.
 */

public class Star extends Sprite {
    protected final Vector2 v = new Vector2();
    private Rect worldBounds;

    public Star(TextureAtlas atlas, float vx, float vy, float width) {
        super(atlas.findRegion("star"));
        v.set(vx, vy);
        setWidthProportion(width);
    }

    @Override
    public void update(float deltaTime) {
        pos.mulAdd(v, deltaTime);
        checkAndHandleBounds();
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(posX, posY);
    }

    protected void checkAndHandleBounds() {
        if (getRight() < worldBounds.getLeft()) setLeft(worldBounds.getRight());
        if (getLeft() > worldBounds.getRight()) setRight(worldBounds.getLeft());
        if (getTop() < worldBounds.getBottom()) setBottom(worldBounds.getTop());
        if (getBottom() > worldBounds.getTop()) setTop(worldBounds.getBottom());
    }
}
