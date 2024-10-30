package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class SpeedBoostPowerUp extends PowerUp {
    private float speedMultiplier;

    public SpeedBoostPowerUp(float x, float y, float duration, float multiplier, Texture texture) {
        super(x, y, duration, texture);
        this.speedMultiplier = multiplier;
    }

    @Override
    public void applyEffect(Nave4 nave) {
        nave.setSpeedMultiplier(speedMultiplier);
    }
}
