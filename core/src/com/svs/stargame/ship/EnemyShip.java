package com.svs.stargame.ship;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import com.svs.engine.math.Rect;
import com.svs.stargame.pool.BulletPool;
import com.svs.stargame.pool.ExplosionPool;

/**
 * Created by SVS on 21-Dec-17.
 */

public class EnemyShip extends Ship {

    private enum State {DESCENT, FIGHT}

    private State state;

    private Vector2 descentV = new Vector2(0, -0.15f);

    private MainShip mainShip;

    private final Vector2 v0 = new Vector2();

    public EnemyShip(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, MainShip mainShip) {
        super(bulletPool, explosionPool, worldBounds);
        this.mainShip = mainShip;
        this.v.set(v0);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        pos.mulAdd(v, deltaTime);

        switch (state) {
            case DESCENT:
                if (getTop() <= worldBounds.getTop()) {
                    v.set(v0);
                    state = State.FIGHT;
                }
                break;
            case FIGHT:
                reloadTimer += deltaTime;
                if (reloadTimer >= reloadInterval) {
                    reloadTimer = 0f;
                    shoot();
                }
                if (getBottom() < worldBounds.getBottom()){
                    mainShip.damage(bulletDamage);
//                    boom();
                    destroy();
                }
                break;
        }
    }


    public void set(
            TextureRegion[] regions,//текстура коробля
            Vector2 v0, //начальная скорость
            TextureRegion bulletRegion, //текстура пули
            float bulletHeight, //высота пули
            float bulletVY, //скорость пули
            int bulletDamage, // урон
            float reloadInterval, //скорость перезарядки
            Sound shootSound, //звук выстрела
            float hight,   //размер корабля
            int hp //жизнь
            ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0f, bulletVY);
        this.bulletDamage = bulletDamage;
        this.reloadInterval = reloadInterval;
        this.shootSound = shootSound;
        this.hp = hp;
        setHeightProportion(hight);
        reloadTimer = reloadInterval;
        v.set(descentV);
        state = State.DESCENT;
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(
                bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > getTop()
                || bullet.getTop() < pos.y
        );
    }
}
