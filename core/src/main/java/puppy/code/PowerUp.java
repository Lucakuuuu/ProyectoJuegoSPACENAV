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
    protected Texture texture;
    protected float visibilityTimer = 10f;  // Control de visibilidad (10s visible, 5s invisible)
    protected static final float VISIBLE_DURATION = 10f;
    protected static final float INVISIBLE_DURATION = 5f;
    protected static final float USED_COOLDOWN = 10f;

    public PowerUp(float x, float y, float duration, Texture texture) {
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.texture = texture;
    }

    public abstract void applyEffect(Nave4 nave);

    public void update(float delta) {
        // Si el power-up está en uso, desactiva visibilidad y cuenta para el enfriamiento
        if (inUse) {
            visibilityTimer -= delta;
            if (visibilityTimer <= 0) {
                visibilityTimer = USED_COOLDOWN; // 10 segundos después de ser usado
                inUse = false;
                active = false;
            }
        } else {
            visibilityTimer -= delta;
            if (active) {
                // Power-up visible y contando hacia su desaparición
                if (visibilityTimer <= 0) {
                    active = false;
                    visibilityTimer = INVISIBLE_DURATION; // Desactiva por 5 segundos
                }
            } else {
                // Power-up invisible, cuenta para reaparecer
                if (visibilityTimer <= 0) {
                    active = true;
                    visibilityTimer = VISIBLE_DURATION; // Vuelve a activarse durante 10 segundos
                    resetPosition(); // Reaparece en nueva posición aleatoria
                }
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if (active) {
            batch.draw(texture, x, y);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        inUse = !active;
        if (active) {
            visibilityTimer = VISIBLE_DURATION;
        } else {
            visibilityTimer = USED_COOLDOWN;
        }
    }

    public void resetPosition() {
        x = random.nextInt(Gdx.graphics.getWidth() - 32);
        y = random.nextInt(Gdx.graphics.getHeight() - 32);
    }
}

