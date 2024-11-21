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
        super(x, y, duration, frames); // Inicializa los atributos de la clase base
        this.speedMultiplier = multiplier;
        this.frames = frames;
        this.currentFrame = 0;
        this.animationTimer = 0f;
    }

    /**
     * Activa el aumento de velocidad al recoger el power-up.
     */
    @Override
    protected void activar(Nave4 nave) {
        nave.setSpeedMultiplier(speedMultiplier); // Incrementa la velocidad de la nave
    }

    /**
     * Aplica el efecto del aumento de velocidad mientras el power-up está activo.
     */
    @Override
    protected void aplicarEfecto(Nave4 nave, float delta) {
        // Si necesitas una lógica continua durante el efecto, puedes agregarla aquí.
    }

    /**
     * Actualiza la animación del power-up.
     */
    @Override
    public void update(float delta) {
        animationTimer += delta;
        if (animationTimer > frameDuration) {
            animationTimer -= frameDuration;
            currentFrame = (currentFrame + 1) % frames.length; // Avanza al siguiente frame
        }
    }

    /**
     * Dibuja el power-up en pantalla con animación.
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (isActive()) { // Verifica si el power-up está activo
            batch.draw(frames[currentFrame], x, y); // Dibuja el frame actual
        }
    }
}
