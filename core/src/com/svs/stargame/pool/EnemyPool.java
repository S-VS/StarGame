package com.svs.stargame.pool;

import com.svs.engine.math.Rect;
import com.svs.engine.pool.SpritesPool;
import com.svs.stargame.ship.EnemyShip;
import com.svs.stargame.ship.MainShip;

/**
 * Created by SVS on 21-Dec-17.
 */

public class EnemyPool extends SpritesPool<EnemyShip> {


    private final BulletPool bulletPool;
    private final ExplosionPool explosionPool;
    private final Rect worldBounds;
    private final MainShip mainShip;

    public EnemyPool(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, MainShip mainShip) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        this.mainShip = mainShip;
    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(bulletPool, explosionPool, worldBounds, mainShip);
    }
    protected void debugLog() {
        System.out.println("EnemyPool change active/free: " + activeObjects.size() + " / " + freeObjects.size());
    }
}
