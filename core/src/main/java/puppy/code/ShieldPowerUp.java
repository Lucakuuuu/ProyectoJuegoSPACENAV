package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShieldPowerUp extends PowerUp implements Updatable {
    private Texture[] frames; // Array de texturas para la animación
    private int currentFrame;
    private float animationTimer;
    private float frameDuration = 0.1f; // Duración de cada frame en segundos

    public ShieldPowerUp(float x, float y, float duration, Texture[] frames) {
        super(x, y, duration, frames); // Pasar todo el array de texturas
        this.frames = frames;
        this.currentFrame = 0;
        this.animationTimer = 0f;
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

    @Override
    public void applyEffect(Nave4 nave) {
        nave.activateShield();
    }
}
