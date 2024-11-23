package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import static com.badlogic.gdx.math.MathUtils.random;
import strategies.PowerUpStrategy;

public abstract class PowerUp {
    protected float x, y;
    protected boolean active = true;
    protected boolean inUse = false;
    protected float duration;
    protected Texture[] textures;
    protected float visibilityTimer = VISIBLE_DURATION; // Control de visibilidad
    protected static final float VISIBLE_DURATION = 10f;
    protected static final float INVISIBLE_DURATION = 5f;
    protected static final float USED_COOLDOWN = 10f;

    private PowerUpStrategy strategy;

    public PowerUp(float x, float y, float duration, Texture[] textures, PowerUpStrategy strategy) {
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.textures = textures;
        this.strategy = strategy;
    }

    /**
     * Método Template que define un power-up:
     * - Control de visibilidad
     * - Activación del efecto
     * - Aplicación del efecto
     * - Manejo de duración
     **/
    public final void ejecutar(float delta, Nave4 nave) {
        if (inUse) {
            strategy.aplicarEfecto(nave, delta); //usamos la estrategia
            manejarDuracion(delta);    // Paso común
        } else {
            controlarVisibilidad(delta);  // Paso común
            if (active && debeActivarse(nave)) { // Paso "gancho"
                strategy.activar(nave);              // Paso específico implementado por subclases
                inUse = true;
            }
        }
    }

    // Pasos comunes para todos los power-ups
    private void controlarVisibilidad(float delta) {
        visibilityTimer -= delta;
        if (active) {
            if (visibilityTimer <= 0) {
                active = false;
                visibilityTimer = INVISIBLE_DURATION;
            }
        } else {
            if (visibilityTimer <= 0) {
                active = true;
                visibilityTimer = VISIBLE_DURATION;
                resetPosition();
            }
        }
    }

    private void manejarDuracion(float delta) {
        duration -= delta;
        if (duration <= 0) {
            inUse = false;
            active = false;
        }
    }

    protected boolean debeActivarse(Nave4 nave)
    {
        return nave.getBounds().overlaps(getBounds());
    }

    public void draw(SpriteBatch batch) {
        if (active && textures.length > 0) {
            batch.draw(textures[0], x, y); // Usa la primera textura para representar el power-up
        }
    }

    // Métodos auxiliares comunes
    public Rectangle getBounds()
    {
        return new Rectangle(x, y, textures[0].getWidth(), textures[0].getHeight());
    }

    public void resetPosition() {
        x = random.nextInt(Gdx.graphics.getWidth() - 32);
        y = random.nextInt(Gdx.graphics.getHeight() - 32);
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        this.inUse = !active;
        visibilityTimer = active ? VISIBLE_DURATION : USED_COOLDOWN;
    }

    public void update(float delta) {}
}
