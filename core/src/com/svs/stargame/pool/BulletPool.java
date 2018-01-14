package com.svs.stargame.pool;

import com.svs.engine.pool.SpritesPool;
import com.svs.stargame.bullet.Bullet;

/**
 * Created by SVS on 20.12.2017.
 */

public class BulletPool extends SpritesPool<Bullet> {
    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    @Override
    protected void debugLog() {
        System.out.println("Bullet pool change active/free: " + activeObjects.size() + " / " + freeObjects.size());
    }
}
