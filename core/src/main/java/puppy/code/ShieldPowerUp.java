package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShieldPowerUp extends PowerUp implements Updatable {
    private Texture[] frames; // Array de texturas para la animación
    private int currentFrame;
    private float animationTimer;
    private float frameDuration = 0.1f; // Duración de cada frame en segundos

    public ShieldPowerUp(float x, float y, float duration, Texture[] frames) {
        super(x, y, duration, frames); // Inicializa usando el constructor de PowerUp
        this.frames = frames;
        this.currentFrame = 0;
        this.animationTimer = 0f;
    }

    /**
     * Activa el efecto del escudo cuando el power-up es recogido.
     */
    @Override
    protected void activar(Nave4 nave) {
        nave.activateShield(); // Llama al método de la nave para activar el escudo.
    }

    /**
     * Aplica continuamente el efecto del escudo mientras el power-up está activo.
     */
    @Override
    protected void aplicarEfecto(Nave4 nave, float delta) {
        // Aquí puedes agregar lógica adicional si se necesita un comportamiento continuo mientras está activo.
    }

    /**
     * Actualiza la animación del power-up.
     */
    @Override
    public void update(float delta) {
        animationTimer += delta;
        if (animationTimer > frameDuration) {
            animationTimer -= frameDuration;
            currentFrame = (currentFrame + 1) % frames.length;
        }
    }

    /**
     * Dibuja el power-up en la pantalla con animación.
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (isActive()) { // Verifica si el power-up está activo
            batch.draw(frames[currentFrame], x, y); // Dibuja el frame actual
        }
    }
}
