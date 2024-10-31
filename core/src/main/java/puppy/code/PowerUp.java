package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import static com.badlogic.gdx.math.MathUtils.random;

public abstract class PowerUp {
    protected float x, y;
    protected boolean active = true;
    protected boolean inUse = false;
    protected float duration;
    protected Texture[] textures;
    protected float visibilityTimer = VISIBLE_DURATION; // Control de visibilidad (10s visible, 5s invisible)
    protected static final float VISIBLE_DURATION = 10f;
    protected static final float INVISIBLE_DURATION = 5f;
    protected static final float USED_COOLDOWN = 10f;

    public PowerUp(float x, float y, float duration, Texture[] textures) {
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.textures = textures;
    }

    public abstract void applyEffect(Nave4 nave);

    public void update(float delta) {
        if (inUse) {
            handleCooldown(delta);
        } else {
            handleVisibility(delta);
        }
    }

    private void handleCooldown(float delta) {
        visibilityTimer -= delta;
        if (visibilityTimer <= 0) {
            visibilityTimer = USED_COOLDOWN; // Enfriamiento después de ser usado
            inUse = false;
            active = false;
        }
    }

    private void handleVisibility(float delta) {
        visibilityTimer -= delta;
        if (active) {
            if (visibilityTimer <= 0) {
                active = false;
                visibilityTimer = INVISIBLE_DURATION; // Desactiva por 5 segundos
            }
        } else {
            if (visibilityTimer <= 0) {
                active = true;
                visibilityTimer = VISIBLE_DURATION; // Vuelve a activarse durante 10 segundos
                resetPosition(); // Reaparece en nueva posición aleatoria
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if (active && textures.length > 0) {
            batch.draw(textures[0], x, y); // Usa el primer texture para dibujar
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, textures[0].getWidth(), textures[0].getHeight()); // Usar el primer texture para calcular dimensiones
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        this.inUse = !active;
        visibilityTimer = active ? VISIBLE_DURATION : USED_COOLDOWN;
    }

    public void resetPosition() {
        x = random.nextInt(Gdx.graphics.getWidth() - 32);
        y = random.nextInt(Gdx.graphics.getHeight() - 32);
    }
}
