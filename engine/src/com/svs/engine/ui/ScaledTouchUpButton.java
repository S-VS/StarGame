package com.svs.engine.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import com.svs.engine.sprite.Sprite;

/**
 * Created by SVS on 13.12.2017.
 */

public class ScaledTouchUpButton extends Sprite {

    private int pointer;
    private boolean pressed;
    private float pressScale;
    private ActionListener actionListener;

    public ScaledTouchUpButton(TextureRegion region, ActionListener actionListener, float pressScale) {
        super(region);
        this.pressScale = pressScale;
        this.actionListener = actionListener;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (pressed || !isMe(touch)) return false;
        this.pointer = pointer;
        scale = pressScale;
        pressed = true;
        return true;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (this.pointer != pointer || !pressed) return false;
        if (isMe(touch)) {
            actionListener.actionPerformed(this);
            return true;
        }
        pressed = false;
        scale = 1f;
        return false;
    }
}
