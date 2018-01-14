package com.svs.stargame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class StarGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture fonstar;
	Vector2 move;
	float x = 0;
	float y = 0;

	boolean left = true;
	boolean right= true;
	boolean down= true;
	boolean up= true;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture(".\\android\\assets\\badlogic.jpg"); //указал такой путь, потому что если просто указать имя файла пишет ошибку при запуске, не находит файл
		fonstar = new Texture(".\\android\\assets\\fonstar.jpg");
		move = new Vector2();
		move.set(0, 1);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		move.x = 100 * Gdx.graphics.getDeltaTime();
		move.y = 100 * Gdx.graphics.getDeltaTime();

		right = !left || x < 0;
		left = !right || x > img.getWidth();
		up = !down || y < 0;
		down = !up || y > img.getHeight();

		if (left) move.x = -move.x;
		if (down) move.y = -move.y;

		x += move.x;
		y += move.y;

		batch.begin();
		batch.draw(fonstar, 0 , 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(img, x, y);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
