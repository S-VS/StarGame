package com.svs.stargame.screen.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import com.svs.engine.Base2DScreen;
import com.svs.engine.Sprite2DTexture;
import com.svs.engine.math.Rect;
import com.svs.engine.math.Rnd;
import com.svs.engine.ui.ActionListener;
import com.svs.stargame.Background;
import com.svs.stargame.screen.game.GameScreen;
import com.svs.stargame.screen.menu.ui.ButtonExit;
import com.svs.stargame.screen.menu.ui.ButtonNewGame;
import com.svs.stargame.star.Star;

/**
 * Created by SVS on 09.12.2017.
 */
public class MenuScreen extends Base2DScreen implements ActionListener{

    private static final float BUTTON_PRESS_SCALE = 0.9f;
    private static final float BUTTON_HEIGHT = 0.15f;

    Sprite2DTexture textureBackground;
    Background background;
    private Music fonMusic;
    private TextureAtlas textureAtlas;
    private ButtonExit buttonExit;
    private ButtonNewGame buttonNewGame;

    private Star star[];
    private Star starMiddle[];
    private Star starSmall[];

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        textureBackground = new Sprite2DTexture("android/assets/textures/bg.png");
        background = new Background(new TextureRegion(textureBackground));
        fonMusic = Gdx.audio.newMusic(Gdx.files.internal("android/assets/sounds/music.mp3"));
        fonMusic.setLooping(true);
        fonMusic.play();
        textureAtlas = new TextureAtlas("android/assets/textures/menuAtlas.tpack");
        buttonExit = new ButtonExit(textureAtlas, this, BUTTON_PRESS_SCALE);
        buttonExit.setHeightProportion(BUTTON_HEIGHT);
        buttonNewGame = new ButtonNewGame(textureAtlas, this, BUTTON_PRESS_SCALE);
        buttonNewGame.setHeightProportion(BUTTON_HEIGHT);


        star = new Star[50];
        for (int i = 0; i < star.length; i++) {
            star[i] =  new Star(textureAtlas, Rnd.nextFloat(-0.005f, 0.005f), Rnd.nextFloat(-0.5f, -0.1f), 0.01f);
        }
        starMiddle = new Star[60];
        for (int i = 0; i < starMiddle.length; i++) {
            starMiddle[i] =  new Star(textureAtlas, Rnd.nextFloat(-0.001f, 0.001f), Rnd.nextFloat(-0.15f, -0.04f), 0.007f);
        }
        starSmall = new Star[150];
        for (int i = 0; i < starSmall.length; i++) {
            starSmall[i] =  new Star(textureAtlas, Rnd.nextFloat(-0.0006f, 0.0006f), Rnd.nextFloat(-0.07f, -0.007f), 0.005f);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
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
        buttonExit.draw(batch);
        buttonNewGame.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        textureAtlas.dispose();
        textureBackground.dispose();
        fonMusic.dispose();
        batch = null;
    }

    @Override
    protected void resize(Rect worldBounds) {
        background.resize(worldBounds);
        buttonExit.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
        for (int i = 0; i < star.length; i++) {
            star[i].resize(worldBounds);
        }
        for (int i = 0; i < starMiddle.length; i++) {
            starMiddle[i].resize(worldBounds);
        }
        for (int i = 0; i < starSmall.length; i++) {
            starSmall[i].resize(worldBounds);
        }
    }

    @Override
    public void touchDown(Vector2 touch, int pointer) {
        buttonExit.touchDown(touch, pointer);
        buttonNewGame.touchDown(touch, pointer);
    }

    @Override
    public void touchUp(Vector2 touch, int pointer) {
        buttonExit.touchUp(touch, pointer);
        buttonNewGame.touchUp(touch, pointer);
    }

    @Override
    public void actionPerformed(Object src) {
        if (src == buttonExit) {
            Gdx.app.exit();
        } else if (src == buttonNewGame) {
            game.setScreen(new GameScreen(game));
        } else {
            throw new RuntimeException("Unknown scr = " + src);
        }
    }
}