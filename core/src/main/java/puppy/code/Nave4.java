package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Nave4 {

    private boolean destruida = false;
    private int vidas = 3;
    private float xVel = 0;
    private float yVel = 0;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax = 50;
    private int tiempoHerido;
    private int speed;

    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala, int speed) {
        sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 45, 45);
        this.speed = speed;
    }

    private float baseXVel = 0;
    private float baseYVel = 0;
    private float slowXVel = 0;
    private float slowYVel = 0;
    private boolean enRalentizacion = false;
    private int tiempoRalentizacionMax = 30;
    private int tiempoRalentizacion = 0;

    public void draw(SpriteBatch batch, PantallaJuego juego) {
        float x = spr.getX();
        float y = spr.getY();

        if (!herido) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) baseXVel = -speed;
            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) baseXVel = speed;
            else baseXVel = 0;

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) baseYVel = -speed;
            else if (Gdx.input.isKeyPressed(Input.Keys.UP)) baseYVel = speed;
            else baseYVel = 0;

            if (enRalentizacion) {
                xVel = slowXVel;
                yVel = slowYVel;
                tiempoRalentizacion--;

                if (tiempoRalentizacion <= 0) {
                    enRalentizacion = false;
                    xVel = baseXVel;
                    yVel = baseYVel;
                }
            } else {
                xVel = baseXVel;
                yVel = baseYVel;
            }

            // Limitar el movimiento dentro de los bordes de la pantalla
            if (x + xVel < 0) x = 0;
            else if (x + xVel + spr.getWidth() > 1280) x = 1280 - spr.getWidth();

            if (y + yVel < 0) y = 0;
            else if (y + yVel + spr.getHeight() > 720) y = 720 - spr.getHeight();

            spr.setPosition(x + xVel, y + yVel);
            spr.draw(batch);
        } else {
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Bullet bala = new Bullet(spr.getX() + spr.getWidth() / 2 - 5, spr.getY() + spr.getHeight(), 0, 8, txBala);
            juego.agregarBala(bala);
            soundBala.play();
        }
    }

    public boolean checkCollision(Ball2 b) {
        if (!herido && b.getArea().overlaps(spr.getBoundingRectangle())) {
            slowXVel = xVel * 0.5f;
            slowYVel = yVel * 0.5f;
            enRalentizacion = true;
            tiempoRalentizacion = tiempoRalentizacionMax;

            if (xVel == 0) xVel += b.getXSpeed() / 2;
            xVel = -xVel;
            b.setXSpeed(-b.getXSpeed());

            if (yVel == 0) yVel += b.getySpeed() / 2;
            yVel = -yVel;
            b.setySpeed(-b.getySpeed());

            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
            if (vidas <= 0) destruida = true;

            return true;
        }
        return false;
    }

    public boolean estaDestruido() {
        return !herido && destruida;
    }
    public boolean estaHerido() {
        return herido;
    }
    public int getVidas() { return vidas; }
    public int getX() { return (int) spr.getX(); }
    public int getY() { return (int) spr.getY(); }
    public void setVidas(int vidas2) { vidas = vidas2; }

    public void draw(SpriteBatch batch, PantallaInstrucciones juego) {
        float x = spr.getX();
        float y = spr.getY();

        if (!herido) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) baseXVel = -3;
            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) baseXVel = 3;
            else baseXVel = 0;

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) baseYVel = -3;
            else if (Gdx.input.isKeyPressed(Input.Keys.UP)) baseYVel = 3;
            else baseYVel = 0;

            if (enRalentizacion) {
                xVel = slowXVel;
                yVel = slowYVel;
                tiempoRalentizacion--;

                if (tiempoRalentizacion <= 0) {
                    enRalentizacion = false;
                    xVel = baseXVel;
                    yVel = baseYVel;
                }
            } else {
                xVel = baseXVel;
                yVel = baseYVel;
            }

            // Limitar el movimiento dentro de los bordes de la pantalla
            if (x + xVel < 0) x = 0;
            else if (x + xVel + spr.getWidth() > 1280) x = 1280 - spr.getWidth();

            if (y + yVel < 0) y = 0;
            else if (y + yVel + spr.getHeight() > 720) y = 720 - spr.getHeight();

            spr.setPosition(x + xVel, y + yVel);
            spr.draw(batch);
        } else {
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Bullet bala = new Bullet(spr.getX() + spr.getWidth() / 2 - 5, spr.getY() + spr.getHeight(), 0, 8, txBala);
            juego.agregarBala(bala);
            soundBala.play();
        }
    }
}
