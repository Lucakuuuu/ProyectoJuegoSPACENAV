package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import strategies.PowerUpStrategy;

public class ShieldPowerUp extends PowerUp implements Updatable {
    private Texture[] frames; // Array de texturas para la animación
    private int currentFrame;
    private float animationTimer;
    private float frameDuration = 0.1f; // Duración de cada frame en segundos

    public ShieldPowerUp(float x, float y, float duration, Texture[] frames, PowerUpStrategy strategy) {
        super(x, y, duration, frames, strategy); // Inicializa usando el constructor de PowerUp
        this.frames = frames;
        this.currentFrame = 0;
        this.animationTimer = 0f;
    }

    @Override
    public void update(float delta) {
        animationTimer += delta;
        if (animationTimer > frameDuration) {
            animationTimer -= frameDuration;
            currentFrame = (currentFrame + 1) % frames.length;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (isActive()) { // Verifica si el power-up está activo
            batch.draw(frames[currentFrame], x, y); // Dibuja el frame actual
        }
    }
}
