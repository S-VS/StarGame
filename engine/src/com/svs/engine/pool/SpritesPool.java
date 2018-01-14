package com.svs.engine.pool;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.svs.engine.sprite.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SVS on 20.12.2017.
 */

public abstract class SpritesPool<T extends Sprite> {

    protected final List<T> activeObjects = new ArrayList<T>();
    protected final List<T> freeObjects = new ArrayList<T>();

    protected abstract T newObject();

    public T obtain() {
        T object;
        if (freeObjects.isEmpty()) {
            object = newObject();
        } else {
            object = freeObjects.remove(freeObjects.size() - 1);
        }
        activeObjects.add(object);
        debugLog();
        return object;
    }

    public void updateActiveSprites(float delta) {
        for (int i = 0; i < activeObjects.size(); i++) {
            Sprite sprite = activeObjects.get(i);
            if (sprite.isDestroyed()) {
                throw new RuntimeException("Попытка обновления объекта помеченного на удаление");
            }
            sprite.update(delta);
        }
    }

    public void freeAllDestroyedActiveObjects() {
        for (int i = 0; i < activeObjects.size(); i++) {
            T sprite = activeObjects.get(i);
            if (sprite.isDestroyed()) {
                free(sprite);
                i--;
                sprite.flushDestroy();
            }
        }
    }

    public void drawActiveObject(SpriteBatch batch) {
        for (int i = 0; i < activeObjects.size(); i++) {
            Sprite sprite = activeObjects.get(i);
            if (sprite.isDestroyed()) {
                throw new RuntimeException("Попытка отрисовки объекта помеченного на удаление");
            }
            sprite.draw(batch);
        }
    }

    public void freeAllActiveObject() {
        freeObjects.addAll(activeObjects);
        activeObjects.clear();
    }

    private void free(T object) {
        if (!activeObjects.remove(object)) {
            throw new RuntimeException("Попытка удаления несуществующего объекта");
        }
        freeObjects.add(object);
        debugLog();
    }

//удалить, только во время разработки
    protected void debugLog() {

    }

    public List<T> getActiveObjects() {
        return activeObjects;
    }

    public void dispose() {
        activeObjects.clear();
        freeObjects.clear();
    }
}
