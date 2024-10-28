package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Ball2 {
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;
    private final int initialXSpeed;
    private Sprite spr;

    public Ball2(int x, int y, int size, int xSpeed, int ySpeed, Texture tx) {
        spr = new Sprite(tx);
        this.x = x;
        this.initialXSpeed = xSpeed;

        //validar que borde de esfera no quede fuera
        if (x-size < 0) this.x = x+size;
        if (x+size > Gdx.graphics.getWidth())this.x = x-size;

        this.y = y;
        //validar que borde de esfera no quede fuera
        if (y-size < 0) this.y = y+size;
        if (y+size > Gdx.graphics.getHeight())this.y = y-size;

        spr.setPosition(x, y);
        this.setXSpeed(xSpeed);
        this.setySpeed(ySpeed);
    }

    public void update() {
        x += xSpeed;
        y += ySpeed;

        // Rebote en los bordes de la pantalla
        if (x < 0 || x + spr.getWidth() >= 1280) {
            xSpeed = -xSpeed;
            x = Math.max(0, Math.min(x, 1280 - (int) spr.getWidth())); // Ajusta la posición para mantener dentro del límite
        }
        if (y < 0 || y + spr.getHeight() >= 720) {
            ySpeed = -ySpeed;
            y = Math.max(0, Math.min(y, 720 - (int) spr.getHeight())); // Ajusta la posición para mantener dentro del límite
        }

        spr.setPosition(x, y);
    }

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public void checkCollision(Ball2 b2) {
        if (spr.getBoundingRectangle().overlaps(b2.getArea())) {
            // Rebote sin cambiar la velocidad original
            xSpeed = -initialXSpeed;
            b2.xSpeed = -b2.initialXSpeed;

            // Separar los meteoritos para evitar que se queden pegados
            while (spr.getBoundingRectangle().overlaps(b2.getArea())) {
                x += xSpeed / 2;
                y += ySpeed / 2;
                spr.setPosition(x, y);
            }
        }
    }

    public int getXSpeed() {
        return xSpeed;
    }

    public void setXSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }
}
