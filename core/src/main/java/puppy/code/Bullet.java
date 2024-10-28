package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {

    private int xSpeed;
    private int ySpeed;
    private boolean destroyed = false;
    private Sprite spr;

    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture tx) {
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public void update() {
        if (!destroyed) { // Solo actualiza si no está destruido
            spr.setPosition(spr.getX() + xSpeed, spr.getY() + ySpeed);

            // Verifica si está fuera de los límites y destruye la bala si es necesario
            if (spr.getX() < 0 || spr.getX() + spr.getWidth() > 1280 || spr.getY() < 0 || spr.getY() + spr.getHeight() > 720) {
                destroyed = true;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if (!destroyed) { // Dibuja solo si no está destruido
            spr.draw(batch);
        }
    }

    public boolean checkCollision(Ball2 b2) {
        if (!destroyed && spr.getBoundingRectangle().overlaps(b2.getArea())) {
            this.destroyed = true; // Marca la bala como destruida
            return true; // Colisión detectada
        }
        return false;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
