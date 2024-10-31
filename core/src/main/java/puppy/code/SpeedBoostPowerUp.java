package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpeedBoostPowerUp extends PowerUp implements Updatable {
    private float speedMultiplier;
    private Texture[] frames; // Array de texturas para la animación
    private int currentFrame;
    private float animationTimer;
    private float frameDuration = 0.1f;

    // Constructor modificado para recibir un array de texturas
    public SpeedBoostPowerUp(float x, float y, float duration, float multiplier, Texture[] frames) {
        super(x, y, duration, frames); // Asegúrate de pasar todo el array
        this.speedMultiplier = multiplier;
        this.frames = frames; // Esto debe inicializar correctamente
    }

    @Override
    public void applyEffect(Nave4 nave) {
        nave.setSpeedMultiplier(speedMultiplier);
    }

    @Override
    public void update(float delta) {
        super.update(delta); // Llamar al método update de PowerUp

        animationTimer += delta;
        if (animationTimer > frameDuration) {
            animationTimer -= frameDuration;
            currentFrame = (currentFrame + 1) % frames.length;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (active) { // Asegurarse de que el power-up esté activo
            batch.draw(frames[currentFrame], x, y); // Dibujar el frame actual
        }
    }
}
