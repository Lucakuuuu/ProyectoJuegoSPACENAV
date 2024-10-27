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
    private final int initialYSpeed;
    private Sprite spr;

    public Ball2(int x, int y, int size, int xSpeed, int ySpeed, Texture tx) {
        spr = new Sprite(tx);
        this.x = x;
        this.y = y;
        this.initialXSpeed = xSpeed;
        this.initialYSpeed = ySpeed;

        // Ajustar posición inicial si el meteorito está fuera de los bordes
        if (x - size < 0) this.x = x + size;
        if (x + size > Gdx.graphics.getWidth()) this.x = x - size;
        if (y - size < 0) this.y = y + size;
        if (y + size > Gdx.graphics.getHeight()) this.y = y - size;

        spr.setPosition(this.x, this.y);
        this.xSpeed = initialXSpeed;
        this.ySpeed = initialYSpeed;
    }

    public void update() {
        x += xSpeed;
        y += ySpeed;

        // Rebote en los bordes de la pantalla
        if (x + xSpeed < 0 || x + xSpeed + spr.getWidth() > Gdx.graphics.getWidth()) {
            xSpeed = -xSpeed;
        }
        if (y + ySpeed < 0 || y + ySpeed + spr.getHeight() > Gdx.graphics.getHeight()) {
            ySpeed = -ySpeed;
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
            ySpeed = -initialYSpeed;
            b2.xSpeed = -b2.initialXSpeed;
            b2.ySpeed = -b2.initialYSpeed;

            // Separar los meteoritos para evitar que se queden pegados
            while (spr.getBoundingRectangle().overlaps(b2.getArea())) {
                x += xSpeed;
                y += ySpeed;
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
