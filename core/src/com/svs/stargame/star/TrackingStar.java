package com.svs.stargame.star;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by SVS on 19.12.2017.
 */

public class TrackingStar extends Star{


    private final Vector2 trackingV;

    private final Vector2 sumV = new Vector2();
    protected float scalar;


    public TrackingStar(TextureAtlas atlas, float vx, float vy, float width, Vector2 trackingV, float scalar) {
        super(atlas, vx, vy, width);
        this.trackingV = trackingV;
        this.scalar = scalar;
    }

    @Override
    public void update(float deltaTime) {
        sumV.setZero().mulAdd(trackingV, scalar).rotate(180).add(v);
        pos.mulAdd(sumV, deltaTime);
        checkAndHandleBounds();
    }
}
