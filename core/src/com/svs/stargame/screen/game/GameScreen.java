package com.svs.stargame.screen.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import com.svs.engine.Base2DScreen;
import com.svs.engine.Sprite2DTexture;
import com.svs.engine.font.Font;
import com.svs.engine.math.Rect;
import com.svs.engine.math.Rnd;
import com.svs.engine.ui.ActionListener;
import com.svs.stargame.Background;
import com.svs.stargame.bullet.Bullet;
import com.svs.stargame.common.EnemiesEmitter;
import com.svs.stargame.pool.BulletPool;
import com.svs.stargame.pool.EnemyPool;
import com.svs.stargame.pool.ExplosionPool;
import com.svs.stargame.screen.game.ui.ButtonNewGame;
import com.svs.stargame.screen.game.ui.MessageGameOver;
import com.svs.stargame.ship.EnemyShip;
import com.svs.stargame.ship.MainShip;
import com.svs.stargame.star.TrackingStar;

import java.util.List;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.right;

/**
 * Created by SVS on 17.12.2017.
 */

public class GameScreen extends Base2DScreen implements ActionListener{

    private static final float FONT_SIZE = 0.02f;

    private static final String FRAGS = "Frags:";
    private static final String HP = "HP:";
    private static final String STAGE = "Stage:";


    Sprite2DTexture textureBackground;
    Background background;

    private TextureAtlas atlas;
    private MainShip mainShip;

    private TrackingStar star[];
    private TrackingStar starMiddle[];
    private TrackingStar starSmall[];

    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private EnemyPool enemyPool;

    private Sound soundExpolsion;
    private Sound soundBullet;
    private Sound soundLaser;
    private Music fonMusic;

    private EnemiesEmitter enemiesEmitter;

    private int frags; //кол-во убитых врагов

    private enum State {PLAYING, GAME_OVER}
    private State state;

    private MessageGameOver messageGameOver;
    private ButtonNewGame buttonNewGame;

    private Font font;
    private StringBuilder sbFrags = new StringBuilder();
    private StringBuilder sbHP = new StringBuilder();
    private StringBuilder sbStage = new StringBuilder();


    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        fonMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        fonMusic.setLooping(true);
        fonMusic.play();
        this.soundExpolsion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        this.soundBullet = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        this.soundLaser = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));

        textureBackground = new Sprite2DTexture("textures/bg.png");
        background = new Background(new TextureRegion(textureBackground));
        atlas = new TextureAtlas("textures/mainAtlas.tpack");


        this.bulletPool = new BulletPool();
        this.explosionPool = new ExplosionPool(atlas, soundExpolsion);


        this.mainShip = new MainShip(atlas,bulletPool,explosionPool,worldBounds,soundLaser);

        star = new TrackingStar[30];
        for (int i = 0; i < star.length; i++) {
            star[i] = new TrackingStar(atlas, Rnd.nextFloat(-0.005f, 0.005f), Rnd.nextFloat(-0.5f, -0.1f), 0.01f, mainShip.getV(), 0.2f);
        }
        starMiddle = new TrackingStar[45];
        for (int i = 0; i < starMiddle.length; i++) {
            starMiddle[i] = new TrackingStar(atlas, Rnd.nextFloat(-0.001f, 0.001f), Rnd.nextFloat(-0.15f, -0.04f), 0.007f, mainShip.getV(), 0.07f);
        }
        starSmall = new TrackingStar[130];
        for (int i = 0; i < starSmall.length; i++) {
            starSmall[i] =  new TrackingStar(atlas, Rnd.nextFloat(-0.0006f, 0.0006f), Rnd.nextFloat(-0.07f, -0.007f), 0.005f, mainShip.getV(), 0.01f);
        }

        this.enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, mainShip);
        this.enemiesEmitter = new EnemiesEmitter(enemyPool, worldBounds, atlas, soundBullet);

        this.messageGameOver = new MessageGameOver(atlas);
        this.buttonNewGame = new ButtonNewGame(atlas, this);

        this.font = new Font("font/font.fnt", "font/font.png");
        this.font.setWorldSize(FONT_SIZE);

        startNewGame();
    }

    public void printInfo() {
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbStage.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft(), worldBounds.getTop());
        font.draw(batch, sbHP.append(HP).append(mainShip.getHp()), worldBounds.pos.x, worldBounds.getTop(), center);
        font.draw(batch, sbStage.append(STAGE).append(enemiesEmitter.getStage()), worldBounds.getRight(), worldBounds.getTop(), right);
    }

    @Override
    public void render(float delta) {
        update(delta);
        if (state == State.PLAYING) checkCollisions();
        checkCollisions();
        deleteAllDestroyed();
        draw();
    }

    public void update(float delta) {
        for (int i = 0; i < star.length; i++) {
            star[i].update(delta);
        }
        for (int i = 0; i < starMiddle.length; i++) {
            starMiddle[i].update(delta);
        }
        for (int i = 0; i < starSmall.length; i++) {
            starSmall[i].update(delta);
        }
        explosionPool.updateActiveSprites(delta);

        switch (state) {
            case PLAYING:
                bulletPool.updateActiveSprites(delta);
                enemyPool.updateActiveSprites(delta);
                enemiesEmitter.generateEnemies(delta, frags);
                mainShip.update(delta);
                if (mainShip.isDestroyed()) state = State.GAME_OVER;
                break;
            case GAME_OVER:
                break;
        }
    }

    public void checkCollisions() {

        //столкновение
        List<EnemyShip> enemyList = enemyPool.getActiveObjects();
        for (EnemyShip enemyShip : enemyList) {
            if (enemyShip.isDestroyed()) {
                continue;
            }
            float minDist = enemyShip.getHalfWidth() + mainShip.getHalfWidth();
            if (enemyShip.pos.dst2(mainShip.pos) < minDist * minDist) {
                mainShip.damage(enemyShip.getBulletDamage());
                enemyShip.boom();
                enemyShip.destroy();
                mainShip.boom();
                mainShip.destroy();
                return;
            }
        }
        //Попадание пуль
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (EnemyShip enemyShip : enemyList) {
            if (enemyShip.isDestroyed()) continue;
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != mainShip || bullet.isDestroyed()) continue;
                if (enemyShip.isBulletCollision(bullet)) {
                    enemyShip.damage(bullet.getDamage());
                    bullet.destroy();
                    if (enemyShip.isDestroyed()) {
                        frags++;
                        break;
                    }
                }
            }
        }
        //попадание в игровой корабль
        for (Bullet bullet : bulletList) {
            if (bullet.isDestroyed() || bullet.getOwner() == mainShip) continue;
            if (mainShip.isBulletCollision(bullet)) {
                mainShip.damage(bullet.getDamage());
                bullet.destroy();
            }
        }

    }

    public void deleteAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
    }

    public void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < star.length; i++) {
            star[i].draw(batch);
        }
        for (int i = 0; i < starMiddle.length; i++) {
            starMiddle[i].draw(batch);
        }
        for (int i = 0; i < starSmall.length; i++) {
            starSmall[i].draw(batch);
        }
        if(!mainShip.isDestroyed())mainShip.draw(batch);
        bulletPool.drawActiveObject(batch);
        explosionPool.drawActiveObject(batch);
        enemyPool.drawActiveObject(batch);
        if (state == State.GAME_OVER){
            messageGameOver.draw(batch);
            buttonNewGame.draw(batch);
        }

        printInfo();
        batch.end();
    }

    @Override
    public void dispose() {
        textureBackground.dispose();
        atlas.dispose();
        bulletPool.dispose();
        explosionPool.dispose();
        enemyPool.dispose();

        soundExpolsion.dispose();
        soundLaser.dispose();
        soundBullet.dispose();
        fonMusic.dispose();

        font.dispose();

        super.dispose();
    }

    @Override
    protected void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (int i = 0; i < star.length; i++) {
            star[i].resize(worldBounds);
        }
        for (int i = 0; i < starMiddle.length; i++) {
            starMiddle[i].resize(worldBounds);
        }
        for (int i = 0; i < starSmall.length; i++) {
            starSmall[i].resize(worldBounds);
        }
        mainShip.resize(worldBounds);
    }

    private void startNewGame() {
        state = State.PLAYING;
        frags = 0;

        mainShip.setToNewGame();
        enemiesEmitter.setToNewGame();

        bulletPool.freeAllActiveObject();
        enemyPool.freeAllActiveObject();
        explosionPool.freeAllActiveObject();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyDown(keycode);
        }
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyUp(keycode);
        }
        return false;
    }
    @Override
    public void touchDown(Vector2 touch, int pointer) {
        switch (state) {
            case PLAYING:
                mainShip.touchDown(touch, pointer);
                break;
            case GAME_OVER:
                buttonNewGame.touchDown(touch, pointer);
                break;
        }
    }
    @Override
    public void touchUp(Vector2 touch, int pointer) {
        switch (state) {
            case PLAYING:
                mainShip.touchUp(touch, pointer);
                break;
            case GAME_OVER:
                buttonNewGame.touchUp(touch, pointer);
                break;
        }
    }
    @Override
    public void actionPerformed(Object src) {
        if (src == buttonNewGame) {
            startNewGame();
        } else {
            throw new RuntimeException("Unknowm src = " + src);
        }
    }
}